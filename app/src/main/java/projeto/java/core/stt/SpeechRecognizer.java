package projeto.java.core.stt;

import projeto.java.AppSettings;

import javax.sound.sampled.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class SpeechRecognizer {
    private static final float SAMPLE_RATE = 16000.0f;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private TargetDataLine line;
    private ByteArrayOutputStream audioBuffer;
    private volatile boolean capturing = false;

    
    private Process serverProcess;
    private BufferedWriter serverIn;
    private BufferedReader serverOut;

   
    private final File pythonWorkingDir = new File("C:\\Users\\Pichau\\Desktop\\Projeto Java");

    public SpeechRecognizer(String ignoredModelPath) throws IOException {
       
        if (AppSettings.isServerAlwaysOn()) {
            ensureServerStarted();
        }
    }

    

    public void startListening() throws LineUnavailableException {
        if (capturing) return;

        AudioFormat format = new AudioFormat(
                SAMPLE_RATE,
                16,
                1,
                true,
                false
        );

        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        line = (TargetDataLine) AudioSystem.getLine(info);
        line.open(format);
        line.start();

        audioBuffer = new ByteArrayOutputStream();
        capturing = true;

        System.out.println("[SpeechRecognizer] Captura iniciada (ouvindo...)");

        executor.submit(() -> {
            byte[] data = new byte[4096];
            try {
                while (capturing && line.isOpen()) {
                    int n = line.read(data, 0, data.length);
                    if (n > 0) {
                        audioBuffer.write(data, 0, n);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void stopAndRecognize(Consumer<String> onResult) {
        if (!capturing) return;

        capturing = false;

        try {
            if (line != null) {
                line.stop();
                line.close();
            }
        } catch (Exception ignored) {}

        if (audioBuffer == null) {
            System.out.println("[SpeechRecognizer] Nenhum áudio capturado (buffer nulo).");
            if (onResult != null) onResult.accept("");
            return;
        }

        byte[] audioBytes = audioBuffer.toByteArray();
        System.out.println("[SpeechRecognizer] Captura finalizada. Tamanho do áudio: " + audioBytes.length + " bytes");

        if (audioBytes.length < 1000) {
            System.out.println("[SpeechRecognizer] Áudio muito curto, ignorando.");
            if (onResult != null) onResult.accept("");
            return;
        }

        executor.submit(() -> {
            String text = "";

            try {
                Path tempWav = saveToWav(audioBytes);
                System.out.println("[SpeechRecognizer] WAV salvo em: " + tempWav);

               
                if (AppSettings.isServerAlwaysOn()) {
                    text = transcribeWithServer(tempWav);
                } else {
                    text = transcribeOnce(tempWav);
                }

                try {
                    Files.deleteIfExists(tempWav);
                } catch (IOException ignored) {}

                System.out.println("[SpeechRecognizer] Texto reconhecido (Whisper): \"" + text + "\"");

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (onResult != null) {
                onResult.accept(text == null ? "" : text.trim());
            }
        });
    }

    private Path saveToWav(byte[] audioBytes) throws IOException {
        Path tempFile = Files.createTempFile("va_audio_", ".wav");
        AudioFormat format = new AudioFormat(
                SAMPLE_RATE,
                16,
                1,
                true,
                false
        );

        try (
                ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);
                AudioInputStream ais = new AudioInputStream(
                        bais,
                        format,
                        audioBytes.length / format.getFrameSize()
                )
        ) {
            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, tempFile.toFile());
        }

        return tempFile;
    }

   

    private void ensureServerStarted() throws IOException {
        if (serverProcess != null && serverProcess.isAlive()) {
            return;
        }

        String modelName = AppSettings.isModelMedium() ? "medium" : "small";
        String lang = AppSettings.getLanguage();  

        ProcessBuilder pb = new ProcessBuilder(
                "python",
                "whisper_server.py",
                modelName,
                lang
        );
        pb.directory(pythonWorkingDir);
       

        serverProcess = pb.start();

        serverIn = new BufferedWriter(
                new OutputStreamWriter(serverProcess.getOutputStream(), StandardCharsets.UTF_8)
        );
        serverOut = new BufferedReader(
                new InputStreamReader(serverProcess.getInputStream(), StandardCharsets.UTF_8)
        );

        System.out.println("[SpeechRecognizer] Whisper server iniciado. PID=" + serverProcess.pid());
    }

    private String transcribeWithServer(Path wavPath) throws IOException {
        ensureServerStarted();

        String text;

        synchronized (serverIn) {
            serverIn.write(wavPath.toAbsolutePath().toString());
            serverIn.newLine();
            serverIn.flush();
        }

        text = serverOut.readLine();
        if (text == null) {
            text = "";
        }

        return text.trim();
    }

   

    private String transcribeOnce(Path wavPath) throws IOException, InterruptedException {
        String modelName = AppSettings.isModelMedium() ? "medium" : "small";
        String lang = AppSettings.getLanguage(); 

        ProcessBuilder pb = new ProcessBuilder(
                "python",
                "whisper_transcribe.py",
                wavPath.toAbsolutePath().toString(),
                modelName,
                lang
        );
        pb.directory(pythonWorkingDir);
        

        Process p = pb.start();

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(' ');
            }
        }

        p.waitFor();

        return sb.toString().trim();
    }

  

    public void shutdown() {
        try {
            if (serverIn != null) {
                synchronized (serverIn) {
                    serverIn.write("EXIT");
                    serverIn.newLine();
                    serverIn.flush();
                }
            }
        } catch (IOException ignored) {}

        if (serverProcess != null) {
            serverProcess.destroy();
        }

        executor.shutdownNow();
    }
}

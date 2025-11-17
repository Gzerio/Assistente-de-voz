package projeto.java.core.comands;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CommandExecutor {

   
    private final CustomCommandManager customManager = new CustomCommandManager();

    public void execute(String rawCommand) {
        if (rawCommand == null) return;

        String command = rawCommand.toLowerCase().trim();
        if (command.isEmpty()) return;

        System.out.println("[VoiceAssistant] Comando recebido: " + rawCommand);

    
        if (executeCustomCommand(command)) {
            return;
        }


        if (containsAny(command,
                "abre o google", "abrir google", "open google", "open the google")) {
            openUrl("https://www.google.com");
            return;
        }

       
        if (containsAny(command,
                "abrir youtube", "abre o youtube", "abrir o youtube",
                "open youtube", "open the youtube")) {
            openUrl("https://www.youtube.com");
            return;
        }

      
        if (containsAny(command,
                "abrir gmail", "abre o gmail", "abrir e-mail", "abrir email",
                "open gmail", "open my email", "open mail")) {
            openUrl("https://mail.google.com");
            return;
        }

        if (containsAny(command,
                "abrir whatsapp", "abrir whatsapp web", "abre o whatsapp", "abre o zap",
                "open whatsapp", "open whatsapp web")) {
            openUrl("https://web.whatsapp.com");
            return;
        }

      
        if (containsAny(command,
                "abrir instagram", "abre o instagram",
                "open instagram")) {
            openUrl("https://www.instagram.com");
            return;
        }

      
        if (containsAny(command,
                "abrir facebook", "abre o facebook",
                "open facebook")) {
            openUrl("https://www.facebook.com");
            return;
        }

        
        if (containsAny(command,
                "abrir tiktok", "abre o tiktok",
                "open tiktok")) {
            openUrl("https://www.tiktok.com");
            return;
        }

      
        if (containsAny(command,
                "abrir spotify", "abre o spotify",
                "open spotify")) {
            openUrl("https://open.spotify.com");
            return;
        }


        if (containsAny(command,
                "abrir netflix", "abre a netflix",
                "open netflix")) {
            openUrl("https://www.netflix.com");
            return;
        }


        if (containsAny(command,
                "abrir navegador", "abrir o navegador",
                "abrir chrome", "abre o chrome", "open browser", "open the browser")) {
            openUrl("https://www.google.com");
            return;
        }

        if (containsAny(command,
                "open downloads", "abrir pasta downloads", "pasta downloads",
                "open download folder", "open downloads folder")) {
            openDownloadsFolder();
            return;
        }

   
        if (containsAny(command,
                "abrir documentos", "abrir pasta documentos", "pasta documentos",
                "open documents", "open my documents", "documents folder")) {
            openUserFolder("Documents", "Documentos");
            return;
        }


        if (containsAny(command,
                "abrir imagens", "abrir pasta imagens", "pasta imagens", "abrir fotos",
                "open pictures", "open my pictures", "pictures folder", "photos folder")) {
            openUserFolder("Pictures", "Imagens");
            return;
        }


        if (containsAny(command,
                "abrir músicas", "abrir musica", "abrir pasta músicas", "pasta músicas",
                "open music", "open my music", "music folder")) {
            openUserFolder("Music", "Músicas");
            return;
        }

    
        if (containsAny(command,
                "abrir área de trabalho", "abrir area de trabalho", "abrir desktop",
                "open desktop", "open my desktop", "desktop folder")) {
            openUserFolder("Desktop", "Área de Trabalho");
            return;
        }

   
        if (containsAny(command,
                "abrir explorador", "abrir explorador de arquivos", "abrir arquivos",
                "open explorer", "open file explorer", "open files")) {
            openFileExplorer();
            return;
        }




        if (containsAny(command,
                "abrir bloco de notas", "abre o bloco de notas", "bloco de notas",
                "open notepad", "open the notepad")) {
            openNotepad();
            return;
        }


        if (containsAny(command,
                "abrir calculadora", "abre a calculadora", "abrir a calculadora",
                "open calculator", "open the calculator")) {
            openCalculator();
            return;
        }


        if (containsAny(command,
                "abrir painel de controle", "abre o painel de controle",
                "open control panel", "open the control panel")) {
            openControlPanel();
            return;
        }

        if (containsAny(command,
                "abrir configurações", "abre as configurações", "abrir as configurações",
                "open settings", "open windows settings", "open system settings")) {
            openWindowsSettings();
            return;
        }


        if (containsAny(command,
                "que horas são", "que horas sao", "me diga as horas", "me fala as horas",
                "what time is it", "tell me the time", "current time")) {
            sayCurrentTime();
            return;
        }

        if (containsAny(command,
                "que dia é hoje", "que dia e hoje", "qual é a data de hoje", "qual a data de hoje",
                "what day is today", "what is today's date", "today's date")) {
            sayTodayDate();
            return;
        }

        System.out.println("[VoiceAssistant] Comando não reconhecido: " + rawCommand);
    }

    private boolean executeCustomCommand(String command) {
        List<CustomCommand> list = customManager.getCommands();
        for (CustomCommand cc : list) {
            for (String phrase : cc.getPhraseList()) {
                if (command.contains(phrase.toLowerCase())) {
                    openUrl(cc.getTargetUrl());
                    System.out.println("[VoiceAssistant] Comando personalizado executado: " + cc.getName());
                    return true;
                }
            }
        }
        return false;
    }

    private boolean containsAny(String text, String... patterns) {
        for (String p : patterns) {
            if (text.contains(p)) {
                return true;
            }
        }
        return false;
    }

    private void openUrl(String url) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(url));
            } else {
                Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start", url});
            }
            System.out.println("[VoiceAssistant] Abrindo URL: " + url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openDownloadsFolder() {
        String userHome = System.getProperty("user.home");
        Path downloadsPath = Paths.get(userHome, "Downloads");
        openFolder(downloadsPath, "Downloads");
    }

    private void openUserFolder(String folderName, String labelPt) {
        String userHome = System.getProperty("user.home");
        Path path = Paths.get(userHome, folderName);
        openFolder(path, labelPt);
    }

    private void openFolder(Path path, String label) {
        try {
            if (!Files.exists(path)) {
                System.out.println("[VoiceAssistant] Pasta " + label + " não encontrada: " + path);
                return;
            }

            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                Runtime.getRuntime().exec(
                        new String[]{"explorer", path.toAbsolutePath().toString()}
                );
            } else if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(path.toFile());
            }

            System.out.println("[VoiceAssistant] Abrindo pasta " + label + ": " + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openFileExplorer() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                String userHome = System.getProperty("user.home");
                new ProcessBuilder("explorer.exe", userHome)
                        .inheritIO()
                        .start();
            } else {
                System.out.println("[VoiceAssistant] Explorador de arquivos não implementado para este SO.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openNotepad() {
        try {
            new ProcessBuilder("notepad.exe")
                    .inheritIO()
                    .start();
            System.out.println("[VoiceAssistant] Abrindo Bloco de Notas");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openCalculator() {
        try {
            new ProcessBuilder("calc.exe")
                    .inheritIO()
                    .start();
            System.out.println("[VoiceAssistant] Abrindo Calculadora");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openControlPanel() {
        try {
            new ProcessBuilder("control.exe")
                    .inheritIO()
                    .start();
            System.out.println("[VoiceAssistant] Abrindo Painel de Controle");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openWindowsSettings() {
        try {
            new ProcessBuilder("cmd", "/c", "start", "ms-settings:")
                    .inheritIO()
                    .start();
            System.out.println("[VoiceAssistant] Abrindo Configurações do Windows");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sayCurrentTime() {
        LocalTime now = LocalTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm");
        String timeStr = now.format(fmt);
        System.out.println("[VoiceAssistant] Agora são " + timeStr);
    }

    private void sayTodayDate() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateStr = today.format(fmt);
        System.out.println("[VoiceAssistant] Hoje é " + dateStr);
    }
}

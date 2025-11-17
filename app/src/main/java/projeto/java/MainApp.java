package projeto.java;

import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import projeto.java.ui.MainView;
import projeto.java.core.stt.SpeechRecognizer;
import projeto.java.core.comands.CommandExecutor;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.Random;

public class MainApp extends Application {

    private double xOffset;
    private double yOffset;

    private ScaleTransition pulseAnim;
    private Timeline waveAnim;
    private ScaleTransition lineAnim;

    private boolean listening = false;

    private SpeechRecognizer speechRecognizer;
    private final CommandExecutor commandExecutor = new CommandExecutor();

    @Override
    public void start(Stage stage) {
        // deixa a janela sem borda do sistema (pra usar sua title bar custom)
        stage.initStyle(StageStyle.TRANSPARENT);

        // opcional: já criar o SpeechRecognizer aqui
        try {
            speechRecognizer = new SpeechRecognizer(null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // vai direto pra UI principal (sem splash)
        showMainUI(stage);
    }

    /**
     * Monta a janela principal (a UI que você já tinha).
     */
    private void showMainUI(Stage stage) {
        MainView mainView = new MainView();

        Scene scene = new Scene(mainView.getRoot(), 900, 550);
        scene.setFill(Color.TRANSPARENT);

        var url = getClass().getResource("/styles/app.css");
        if (url != null) scene.getStylesheets().add(url.toExternalForm());

        stage.setScene(scene);
        stage.setResizable(false);

        // arrastar pela barra
        mainView.getTitleBar().setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        mainView.getTitleBar().setOnMouseDragged(e -> {
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });

        // botões janela
        mainView.getBtnMinimize().setOnAction(e -> stage.setIconified(true));
        mainView.getBtnClose().setOnAction(e -> {
            // se quiser, aqui você pode chamar speechRecognizer.shutdown()
            stage.close();
        });

        // texto inicial no campo de comando
        mainView.getRecognizedField().setText(
                "Reconhecimento de voz pronto.\nClique no microfone e fale um comando."
        );

        // ----- animação de PULSE no botão -----
        pulseAnim = new ScaleTransition(Duration.millis(600), mainView.getMicButton());
        pulseAnim.setFromX(1.0);
        pulseAnim.setFromY(1.0);
        pulseAnim.setToX(1.12);
        pulseAnim.setToY(1.12);
        pulseAnim.setAutoReverse(true);
        pulseAnim.setCycleCount(Animation.INDEFINITE);

        // ----- animação das ONDAS -----
        Random random = new Random();
        waveAnim = new Timeline();
        waveAnim.setCycleCount(Animation.INDEFINITE);
        waveAnim.setAutoReverse(true);

        for (var node : mainView.getWaveBars().getChildren()) {
            Rectangle bar = (Rectangle) node;

            double base = 10;
            double variation = 18; // entre 10 e 28
            KeyValue kvUp = new KeyValue(
                    bar.heightProperty(),
                    base + random.nextInt((int) variation),
                    Interpolator.EASE_BOTH
            );

            KeyFrame kf = new KeyFrame(Duration.millis(250 + random.nextInt(200)), kvUp);
            waveAnim.getKeyFrames().add(kf);
        }

        // linha central "respirando"
        lineAnim = new ScaleTransition(Duration.millis(700), mainView.getWaveLine());
        lineAnim.setFromY(1.0);
        lineAnim.setToY(1.7);
        lineAnim.setAutoReverse(true);
        lineAnim.setCycleCount(Animation.INDEFINITE);

        // ----- clique no botão de mic -----
        mainView.getMicButton().setOnMouseClicked(e -> {
            if (speechRecognizer == null) {
                mainView.getRecognizedField().setText(
                        "Reconhecimento de voz não inicializado.\n" +
                        "Verifique o Whisper / configuração do microfone."
                );
                return;
            }

            listening = !listening;

            if (listening) {
                // estado ATIVO (gravando)
                pulseAnim.play();
                waveAnim.play();
                lineAnim.play();

                mainView.getMicButton().getStyleClass().add("mic-active");
                mainView.getWaveContainer().setVisible(true);
                mainView.getMicLabel().setVisible(false);

                try {
                    speechRecognizer.startListening();
                    mainView.getRecognizedField().setText(
                            "Ouvindo...\nFale o comando perto do microfone."
                    );
                } catch (LineUnavailableException ex) {
                    ex.printStackTrace();
                    listening = false;

                    // reverte UI
                    pulseAnim.stop();
                    waveAnim.stop();
                    lineAnim.stop();
                    mainView.getMicButton().getStyleClass().remove("mic-active");
                    mainView.getWaveContainer().setVisible(false);
                    mainView.getMicLabel().setVisible(true);

                    mainView.getRecognizedField().setText(
                            "Erro ao acessar o microfone.\n" +
                            "Verifique se há um microfone disponível."
                    );
                }

            } else {
                // estado PARADO
                pulseAnim.stop();
                waveAnim.stop();
                lineAnim.stop();

                mainView.getMicButton().getStyleClass().remove("mic-active");
                mainView.getWaveContainer().setVisible(false);
                mainView.getMicLabel().setVisible(true);

                speechRecognizer.stopAndRecognize(text -> {
                    Platform.runLater(() -> {
                        if (text == null || text.isBlank()) {
                            mainView.getRecognizedField().setText(
                                    "Não entendi nada.\n" +
                                    "Tente falar novamente, de forma clara e próxima ao microfone."
                            );
                        } else {
                            mainView.getRecognizedField().setText(text);
                            commandExecutor.execute(text);
                        }
                    });
                });
            }
        });

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

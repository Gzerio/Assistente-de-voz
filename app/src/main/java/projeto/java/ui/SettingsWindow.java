package projeto.java.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import projeto.java.AppSettings;
import projeto.java.ui.components.ToggleSwitch;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;

public class SettingsWindow {

    public static void show(Stage owner) {
        Stage stage = new Stage();
        stage.setTitle("Configurações");
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initStyle(StageStyle.TRANSPARENT);

     
        Label modelLabel = new Label("Modelo médio (mais pesado, mais preciso)");
        Label modelHelp = new Label("?");
        modelHelp.getStyleClass().add("help-icon");

        Tooltip modelTip = new Tooltip(
                "Quando desativado usa o modelo SMALL (padrão, mais leve).\n" +
                "Quando ativado usa o modelo MEDIUM (mais pesado, mais preciso)."
        );

        ToggleSwitch modelSwitch = new ToggleSwitch(AppSettings.isModelMedium());
        Label modelToggleLabel = new Label("Ativar modelo médio");
        HBox modelToggleRow = new HBox(10, modelToggleLabel, modelSwitch);
        modelToggleRow.setAlignment(Pos.CENTER_LEFT);

        HBox modelHeader = new HBox(8, modelLabel, modelHelp);
        modelHeader.setAlignment(Pos.CENTER_LEFT);
        VBox modelBox = new VBox(6, modelHeader, modelToggleRow);

    
        Label langLabel = new Label("Idioma da transcrição");
        Label langHelp = new Label("?");
        langHelp.getStyleClass().add("help-icon");

        Tooltip langTip = new Tooltip(
                "Define em qual idioma o Whisper vai tentar reconhecer.\n" +
                "Português BR ou Inglês."
        );

        ComboBox<String> langCombo = new ComboBox<>();
        langCombo.getItems().addAll("Português (BR)", "English");
        langCombo.setValue(AppSettings.getLanguage().equals("en")
                ? "English"
                : "Português (BR)");

        HBox langHeader = new HBox(8, langLabel, langHelp);
        langHeader.setAlignment(Pos.CENTER_LEFT);
        VBox langBox = new VBox(6, langHeader, langCombo);

       
        Label modeLabel = new Label("Execução do Whisper");
        Label modeHelp = new Label("?");
        modeHelp.getStyleClass().add("help-icon");

        Tooltip modeTip = new Tooltip(
                "Ativado: o servidor do Whisper inicia junto com o programa\n" +
                "e fica rodando o tempo todo (mais rápido, usa mais RAM).\n\n" +
                "Desativado: o Whisper é iniciado apenas quando você grava,\n" +
                "transcreve o áudio e fecha em seguida (mais lento, mais leve)."
        );

        ToggleSwitch alwaysOnSwitch = new ToggleSwitch(AppSettings.isServerAlwaysOn());
        Label modeToggleLabel = new Label("Manter Whisper rodando o tempo todo");
        HBox modeToggleRow = new HBox(10, modeToggleLabel, alwaysOnSwitch);
        modeToggleRow.setAlignment(Pos.CENTER_LEFT);

        HBox modeHeader = new HBox(8, modeLabel, modeHelp);
        modeHeader.setAlignment(Pos.CENTER_LEFT);
        VBox modeBox = new VBox(6, modeHeader, modeToggleRow);

    
        Button btnSalvar = new Button("Salvar");
        Button btnCancelar = new Button("Cancelar");

        btnCancelar.getStyleClass().add("settings-cancel-button");
        btnSalvar.getStyleClass().add("settings-save-button");

        btnSalvar.setOnAction(e -> {
            AppSettings.setModelMedium(modelSwitch.isOn());
            AppSettings.setServerAlwaysOn(alwaysOnSwitch.isOn());
            String lang = langCombo.getValue().startsWith("English") ? "en" : "pt";
            AppSettings.setLanguage(lang);

            
            showCustomInfoDialog(stage, "Alterações salvas. Reinicie o aplicativo para aplicar.");

            stage.close();
        });

        btnCancelar.setOnAction(e -> stage.close());

        HBox buttons = new HBox(10, btnCancelar, btnSalvar);
        buttons.setAlignment(Pos.CENTER_RIGHT);

       
        Label headerTitle = new Label("Configurações");
        headerTitle.getStyleClass().add("settings-title");

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        Button closeBtn = new Button("×");
        closeBtn.getStyleClass().add("settings-close-button");
        closeBtn.setOnAction(e -> stage.close());

        HBox header = new HBox(8, headerTitle, headerSpacer, closeBtn);
        header.getStyleClass().add("settings-header");
        header.setAlignment(Pos.CENTER_LEFT);

     
        VBox root = new VBox(12,
                header,
                modelBox,
                new Separator(),
                langBox,
                new Separator(),
                modeBox,
                buttons
        );
        root.setPadding(new Insets(16));
        root.getStyleClass().add("settings-root");

       
        Scene scene = new Scene(root, 480, 350);
        scene.setFill(Color.TRANSPARENT);

        var css = SettingsWindow.class.getResource("/styles/app.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }

        stage.setScene(scene);

        
        stage.setOnShown(ev -> {
            double centerX = owner.getX() + (owner.getWidth() - stage.getWidth()) / 2;
            double centerY = owner.getY() + (owner.getHeight() - stage.getHeight()) / 2;
            stage.setX(centerX);
            stage.setY(centerY);
        });

       

        final boolean[] modelVisible = {false};
        final boolean[] langVisible  = {false};
        final boolean[] modeVisible  = {false};

        modelHelp.setOnMouseClicked(e -> {
            if (!modelVisible[0]) {
                modelTip.show(modelHelp,
                        e.getScreenX() + 8,
                        e.getScreenY() + 8);
                modelVisible[0] = true;
            } else {
                modelTip.hide();
                modelVisible[0] = false;
            }
            e.consume();
        });

        langHelp.setOnMouseClicked(e -> {
            if (!langVisible[0]) {
                langTip.show(langHelp,
                        e.getScreenX() + 8,
                        e.getScreenY() + 8);
                langVisible[0] = true;
            } else {
                langTip.hide();
                langVisible[0] = false;
            }
            e.consume();
        });

        modeHelp.setOnMouseClicked(e -> {
            if (!modeVisible[0]) {
                modeTip.show(modeHelp,
                        e.getScreenX() + 8,
                        e.getScreenY() + 8);
                modeVisible[0] = true;
            } else {
                modeTip.hide();
                modeVisible[0] = false;
            }
            e.consume();
        });

       
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            Node target = e.getPickResult().getIntersectedNode();

            boolean clickedOnHelp =
                    isNodeOrChild(target, modelHelp) ||
                    isNodeOrChild(target, langHelp)  ||
                    isNodeOrChild(target, modeHelp);

            if (!clickedOnHelp) {
                if (modelVisible[0]) {
                    modelTip.hide();
                    modelVisible[0] = false;
                }
                if (langVisible[0]) {
                    langTip.hide();
                    langVisible[0] = false;
                }
                if (modeVisible[0]) {
                    modeTip.hide();
                    modeVisible[0] = false;
                }
            }
        });

        

        final double[] dragDelta = new double[2];
        header.setOnMousePressed(e -> {
            dragDelta[0] = e.getSceneX();
            dragDelta[1] = e.getSceneY();
        });
        header.setOnMouseDragged(e -> {
            stage.setX(e.getScreenX() - dragDelta[0]);
            stage.setY(e.getScreenY() - dragDelta[1]);
        });

        stage.showAndWait();
    }

   
    private static boolean isNodeOrChild(Node target, Node parent) {
        while (target != null) {
            if (target == parent) return true;
            target = target.getParent();
        }
        return false;
    }

    private static void showCustomInfoDialog(Stage owner, String message) {
        Stage dialog = new Stage(StageStyle.TRANSPARENT);
        dialog.initOwner(owner);
        dialog.initModality(Modality.WINDOW_MODAL);

        Label msgLabel = new Label(message);
        msgLabel.getStyleClass().add("custom-alert-text");
        msgLabel.setWrapText(true);

        Button okButton = new Button("OK");
        okButton.getStyleClass().add("custom-alert-button");
        okButton.setOnAction(e -> dialog.close());

        HBox buttonBox = new HBox(okButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        VBox card = new VBox(12, msgLabel, buttonBox);
        card.getStyleClass().add("custom-alert-root");
        card.setPadding(new Insets(14));

        javafx.scene.layout.StackPane wrapper = new javafx.scene.layout.StackPane(card);
        wrapper.setStyle("-fx-background-color: transparent;");

        Scene scene = new Scene(wrapper);
        scene.setFill(Color.TRANSPARENT);

        var css = SettingsWindow.class.getResource("/styles/app.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }

        dialog.setScene(scene);

       
        dialog.setOnShown(ev -> {
            double centerX = owner.getX() + (owner.getWidth() - dialog.getWidth()) / 2;
            double centerY = owner.getY() + (owner.getHeight() - dialog.getHeight()) / 2;
            dialog.setX(centerX);
            dialog.setY(centerY);
        });

        dialog.showAndWait();
    }
}

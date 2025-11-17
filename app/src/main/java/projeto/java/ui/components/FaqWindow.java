package projeto.java.ui.components;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import projeto.java.core.comands.CustomCommand;
import projeto.java.core.comands.CustomCommandManager;

public class FaqWindow {

    public static void show(Stage owner) {
        Stage stage = new Stage(StageStyle.TRANSPARENT);
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Comandos");

        CustomCommandManager manager = new CustomCommandManager();

        
        Label headerTitle = new Label("Comandos");
        headerTitle.getStyleClass().add("settings-title");

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        Button closeBtn = new Button("×");
        closeBtn.getStyleClass().add("settings-close-button");
        closeBtn.setOnAction(e -> stage.close());

        HBox header = new HBox(8, headerTitle, headerSpacer, closeBtn);
        header.getStyleClass().add("settings-header");

       
        final double[] dragDelta = new double[2];
        header.setOnMousePressed(e -> {
            dragDelta[0] = e.getSceneX();
            dragDelta[1] = e.getSceneY();
        });
        header.setOnMouseDragged(e -> {
            stage.setX(e.getScreenX() - dragDelta[0]);
            stage.setY(e.getScreenY() - dragDelta[1]);
        });

        
        Label titleBuiltIn = new Label("Comandos padrão disponíveis");
        titleBuiltIn.getStyleClass().add("settings-title");

        TextArea builtInArea = new TextArea();
        builtInArea.setEditable(false);
        builtInArea.setWrapText(true);
        builtInArea.getStyleClass().add("recognized-input");
        builtInArea.setPrefRowCount(10);
        builtInArea.setText(
                """
                Exemplos de comandos (PT / EN):

                • "abre o google" / "open google"
                • "abrir youtube" / "open youtube"
                • "abrir gmail" / "open gmail"
                • "abrir whatsapp web" / "open whatsapp"
                • "abrir spotify" / "open spotify"
                • "abrir pasta downloads" / "open downloads folder"
                • "abrir documentos" / "open documents"
                • "abrir bloco de notas" / "open notepad"
                • "abrir calculadora" / "open calculator"
                • "que horas são" / "what time is it"
                • "que dia é hoje" / "what day is today"
                """
        );

        VBox builtInBox = new VBox(6, titleBuiltIn, builtInArea);

        
        Label titleCustom = new Label("Comandos personalizados");
        titleCustom.getStyleClass().add("settings-title");

        ListView<CustomCommand> customList = new ListView<>();
        customList.getItems().addAll(manager.getCommands());
        customList.setPrefHeight(120);

        VBox customListBox = new VBox(6, titleCustom, customList);

       
        Label formTitle = new Label("Adicionar novo comando (abrir URL)");
        formTitle.getStyleClass().add("settings-title");

        TextField nameField = new TextField();
        nameField.setPromptText("Nome do comando (ex.: Abrir Youtube)");

        TextField phrasesField = new TextField();
        phrasesField.setPromptText("Frases de ativação (separadas por vírgula)");

        TextField urlField = new TextField();
        urlField.setPromptText("URL para abrir (ex.: https://youtube.com)");

        Button btnAdd = new Button("Adicionar comando");
        btnAdd.getStyleClass().add("settings-save-button");

        Label feedback = new Label();
        feedback.getStyleClass().add("settings-help-text");

        btnAdd.setOnAction(e -> {
            String name = nameField.getText().trim();
            String phrases = phrasesField.getText().toLowerCase().trim();
            String url = urlField.getText().trim();

            if (name.isEmpty() || phrases.isEmpty() || url.isEmpty()) {
                feedback.setText("Preencha nome, frases e URL.");
                return;
            }

            CustomCommand cmd = new CustomCommand(name, phrases, url);
            manager.addCommand(cmd);

            customList.getItems().setAll(manager.getCommands());

            nameField.clear();
            phrasesField.clear();
            urlField.clear();
            feedback.setText("Comando salvo com sucesso!");
        });

        VBox formBox = new VBox(6, formTitle, nameField, phrasesField, urlField, btnAdd, feedback);

       
        VBox root = new VBox(12,
                header,
                builtInBox,
                new Separator(),
                customListBox,
                new Separator(),
                formBox
        );
        root.setPadding(new Insets(16));
        root.getStyleClass().add("settings-root");

        Scene scene = new Scene(root, 600, 540);
        scene.setFill(Color.TRANSPARENT);

        var css = FaqWindow.class.getResource("/styles/app.css");
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

        stage.showAndWait();
    }
}

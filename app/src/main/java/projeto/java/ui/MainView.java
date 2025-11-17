package projeto.java.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import projeto.java.ui.components.FaqWindow;


public class MainView {

    private final StackPane root;
    private final HBox titleBar;

    private final Button btnMinimize;
    private final Button btnClose;

    private final StackPane micButton;
    private final Label micLabel;

    private final HBox waveBars;
    private final StackPane waveContainer;
    private final Rectangle waveLine;

    private final Label recognizedTitleLabel;
    private final TextArea recognizedField;

    public MainView() {
        root = new StackPane();
        root.getStyleClass().add("root");

        // ---------- TOP BAR ----------
        Label title = new Label("Voice Assistant");
        title.getStyleClass().add("window-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        btnMinimize = new Button("–");
        btnMinimize.getStyleClass().addAll("window-button", "window-button-min");

        btnClose = new Button("×");
        btnClose.getStyleClass().addAll("window-button", "window-button-close");

        titleBar = new HBox(10, title, spacer, btnMinimize, btnClose);
        titleBar.getStyleClass().add("title-bar");
        titleBar.setAlignment(Pos.CENTER_LEFT);
        titleBar.setPadding(new Insets(6, 10, 6, 14));

        // ---------- ICON BAR ----------
        ImageView iconSettings = new ImageView(
                new Image(getClass().getResourceAsStream("/images/settings.png"))
        );
        iconSettings.setFitWidth(30);
        iconSettings.setFitHeight(30);

        Button btnSettings = new Button();
        btnSettings.setGraphic(iconSettings);
        btnSettings.getStyleClass().add("icon-button");
        btnSettings.setTooltip(new Tooltip("Configurações"));

        ImageView iconFaq = new ImageView(
                new Image(getClass().getResourceAsStream("/images/FAQ.png"))
        );
        iconFaq.setFitWidth(30);
        iconFaq.setFitHeight(30);

        Button btnFaq = new Button();
        btnFaq.setGraphic(iconFaq);
        btnFaq.getStyleClass().add("icon-button");
        btnFaq.setTooltip(new Tooltip("FAQ"));

        ImageView iconContact = new ImageView(
                new Image(getClass().getResourceAsStream("/images/suporte.png"))
        );
        iconContact.setFitWidth(30);
        iconContact.setFitHeight(30);

        Button btnContact = new Button();
        btnContact.setGraphic(iconContact);
        btnContact.getStyleClass().add("icon-button");
        btnContact.setTooltip(new Tooltip("Contato"));

        HBox iconBar = new HBox(8, btnContact, btnFaq, btnSettings);
        iconBar.getStyleClass().add("icon-bar");
        iconBar.setAlignment(Pos.CENTER_RIGHT);
        iconBar.setPadding(new Insets(6, 18, 0, 0));

        VBox topContainer = new VBox(titleBar, iconBar);

        // ---------- MIC ----------
        Circle circle = new Circle(70);
        circle.getStyleClass().add("mic-circle");

        Label micIcon = new Label("\uD83C\uDF99");
        micIcon.getStyleClass().add("mic-icon");

        micButton = new StackPane(circle, micIcon);
        micButton.setAlignment(Pos.CENTER);
        micButton.getStyleClass().add("mic-button");

        micLabel = new Label("Pressione para começar");
        micLabel.getStyleClass().add("mic-label");

        // ---------- WAVES ----------
        waveBars = new HBox(4);
        waveBars.setAlignment(Pos.CENTER);
        for (int i = 0; i < 18; i++) {
            Rectangle bar = new Rectangle(6, 20);
            bar.getStyleClass().add("wave-bar");
            waveBars.getChildren().add(bar);
        }

        waveLine = new Rectangle(180, 2);
        waveLine.setArcWidth(4);
        waveLine.setArcHeight(4);
        waveLine.getStyleClass().add("wave-line");

        waveContainer = new StackPane(waveBars, waveLine);
        waveContainer.getStyleClass().add("wave-container");
        waveContainer.setVisible(false);
        waveContainer.setMinHeight(40);
        waveContainer.setPrefHeight(40);
        waveContainer.setMaxHeight(40);

        StackPane labelWrapper = new StackPane(micLabel);
        labelWrapper.setMinHeight(40);
        labelWrapper.setPrefHeight(40);
        labelWrapper.setMaxHeight(40);

        StackPane feedbackArea = new StackPane(labelWrapper, waveContainer);
        feedbackArea.setAlignment(Pos.CENTER);

        // ---------- “VOCÊ DISSE” ----------
        recognizedTitleLabel = new Label("Você disse");
        recognizedTitleLabel.getStyleClass().add("recognized-title");

        recognizedField = new TextArea();
        recognizedField.setPromptText("Seu comando vai aparecer aqui...");
        recognizedField.setEditable(false);
        recognizedField.setFocusTraversable(false);
        recognizedField.setWrapText(true);
        recognizedField.setPrefRowCount(3);
        recognizedField.setMaxWidth(480);
        recognizedField.getStyleClass().add("recognized-input");

        VBox commandBox = new VBox(4, recognizedTitleLabel, recognizedField);
commandBox.setAlignment(Pos.TOP_LEFT);
commandBox.getStyleClass().add("command-box");
commandBox.setMaxWidth(520);


        // ---------- CENTRO ----------
        VBox centerArea = new VBox(24, micButton, feedbackArea, commandBox);
        centerArea.setAlignment(Pos.CENTER);
        centerArea.setPadding(new Insets(40, 0, 0, 0));

        BorderPane layout = new BorderPane();
        layout.setTop(topContainer);
        layout.setCenter(centerArea);

        root.getChildren().add(layout);

        // ---------- AÇÕES ----------
        btnContact.setOnAction(e -> System.out.println("Contato clicado"));
        btnFaq.setOnAction(e -> {
    Stage stage = (Stage) root.getScene().getWindow();
    FaqWindow.show(stage);
});

        btnSettings.setOnAction(e -> {
            Stage stage = (Stage) root.getScene().getWindow();
            SettingsWindow.show(stage);
        });
    }

    public StackPane getRoot() { return root; }
    public HBox getTitleBar() { return titleBar; }
    public Button getBtnMinimize() { return btnMinimize; }
    public Button getBtnClose() { return btnClose; }

    public StackPane getMicButton() { return micButton; }
    public Label getMicLabel() { return micLabel; }

    public HBox getWaveBars() { return waveBars; }
    public StackPane getWaveContainer() { return waveContainer; }
    public Rectangle getWaveLine() { return waveLine; }

    public Label getRecognizedTitleLabel() { return recognizedTitleLabel; }
    public TextArea getRecognizedField() { return recognizedField; }
}

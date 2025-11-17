package projeto.java.ui.components;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.css.PseudoClass;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

public class ToggleSwitch extends StackPane {

    private static final PseudoClass ON_PSEUDO = PseudoClass.getPseudoClass("on");

    private final BooleanProperty on = new SimpleBooleanProperty(false);
    private final Region thumb = new Region();
    private final Label label = new Label();

    public ToggleSwitch(boolean initialState) {
        getStyleClass().add("toggle-switch");
        setMinSize(46, 24);
        setMaxSize(46, 24);

        thumb.getStyleClass().add("toggle-thumb");
        label.getStyleClass().add("toggle-switch-label");

        getChildren().addAll(thumb, label);
        setAlignment(thumb, Pos.CENTER_LEFT);
        setAlignment(label, Pos.CENTER_RIGHT);

        setCursor(Cursor.HAND);

        on.addListener((obs, oldVal, newVal) -> updateVisual());
        setOn(initialState);
        updateVisual();

        setOnMouseClicked(e -> setOn(!isOn()));
    }

    private void updateVisual() {
        if (isOn()) {
            pseudoClassStateChanged(ON_PSEUDO, true);
            label.setText("On");
            StackPane.setAlignment(thumb, Pos.CENTER_RIGHT);
        } else {
            pseudoClassStateChanged(ON_PSEUDO, false);
            label.setText("Off");
            StackPane.setAlignment(thumb, Pos.CENTER_LEFT);
        }
    }

    public boolean isOn() {
        return on.get();
    }

    public void setOn(boolean value) {
        on.set(value);
    }

    public BooleanProperty onProperty() {
        return on;
    }
}

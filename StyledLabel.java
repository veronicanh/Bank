import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.layout.*;
import javafx.geometry.Insets;

class StyledLabel extends Label {
    public StyledLabel(String tekst) {
        super(tekst);
        setFont(new Font("Courier New", 16));
        setTextFill(Color.valueOf("7D5242"));
    }

    public StyledLabel(String tekst, int bareTull) {
        super(tekst);
        setFont(new Font("Courier New", 16));
        setTextFill(Color.BLACK);
    }

    public void settSomOverskrift() {
        setFont(new Font("Courier New", 20));
        setAlignment(Pos.CENTER);
        setBorder(new Border(new BorderStroke(Color.valueOf("E5D5CE"), BorderStrokeStyle.SOLID,CornerRadii.EMPTY, new BorderWidths(2))));
        setBackground(new Background(new BackgroundFill(Color.valueOf("E7DBD6"), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    // Saa alle kan vaere like store
    public void settSomMenuItem() {
        setPrefWidth(120);
    }

    // Hoyre-justerer beloepet
    public void settSomKrLabel() {
        if (getText().contains("-")) {
            setTextFill(Color.valueOf("753D37")); // moerk roed
        } else {
            setTextFill(Color.valueOf("5A6650")); // moerk groenn
        }
        setMinWidth(80);
        setAlignment(Pos.BASELINE_RIGHT);
    }

    public void settSomKodeOrd() {
        setBackground(new Background(new BackgroundFill(Color.valueOf("EEE8E5"), CornerRadii.EMPTY, Insets.EMPTY)));
        setBorder(new Border(new BorderStroke(Color.valueOf("E5D5CE"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
    }
}
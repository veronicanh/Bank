import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.layout.*;
import javafx.geometry.Insets;

class StyledButton extends Button {
    public StyledButton(String tekst) {
        super(tekst);
        setFont(new Font("Courier New", 16));
        setTextFill(Color.BLACK);
    }

    public void settSomKodeOrd() {
        setBackground(new Background(new BackgroundFill(Color.valueOf("EEE8E5"), CornerRadii.EMPTY, Insets.EMPTY)));
        setBorder(new Border(new BorderStroke(Color.valueOf("E5D5CE"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
    }

    public boolean fargelegg() {
        if (getBackground().getFills().get(0).getFill().equals(Color.valueOf("EEE8E5"))) {
            setBackground(new Background(new BackgroundFill(Color.valueOf("F4F4F4"), CornerRadii.EMPTY, Insets.EMPTY)));
            setBorder(new Border(new BorderStroke(Color.valueOf("EBEBEB"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                    new BorderWidths(1))));
            setTextFill(Color.valueOf("CCCCCC"));
            return false;
        }
        setBackground(new Background(new BackgroundFill(Color.valueOf("EEE8E5"), CornerRadii.EMPTY, Insets.EMPTY)));
        setBorder(new Border(new BorderStroke(Color.valueOf("E5D5CE"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
        setTextFill(Color.BLACK);
        return true;
    }
}
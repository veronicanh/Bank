import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
// Elementer
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.*;
import javafx.scene.paint.Color;
// For aa haandtere hendelser
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

// Klasse for aa vise feilmeldinger
class ErrorPopup extends Stage {
    // Stoerrelsen paa vinduet
    private final int WIDTH = 300+50;
    private final int HEIGHT = 150+10;
    private Linje kilden;

    public ErrorPopup(String feilmelding) {
        setTitle("Error");
        Pane kulisser = byggPane(feilmelding);

        // Tilbake-knapp
        Button lukkVinduKnapp = new StyledButton("Tilbake");
        lukkVinduKnapp.setOnAction(new LukkVindu());
        lukkVinduKnapp.setLayoutX(WIDTH-100);
        lukkVinduKnapp.setLayoutY(HEIGHT-40);      
        kulisser.getChildren().add(lukkVinduKnapp);
        // "Lager" og viser vinduet
        Scene scene = new Scene(kulisser);
        setScene(scene);
        show();
    }

    public ErrorPopup(Linje kilde, String feilmelding) {
        kilden = kilde;
        Pane kulisser = byggPane(feilmelding);

        // Tilbake-knapp
        Button lukkVinduKnapp = new StyledButton("Avbryt");
        lukkVinduKnapp.setOnAction(new LukkVindu());
        lukkVinduKnapp.setLayoutX(WIDTH - 200);
        lukkVinduKnapp.setLayoutY(HEIGHT - 40);

        // Bekreft-knapp
        Button bekreft = new StyledButton("Bekreft");
        bekreft.setOnAction(new BekreftBehandler());
        bekreft.setLayoutX(WIDTH - 100);
        bekreft.setLayoutY(HEIGHT - 40);

        kulisser.getChildren().addAll(lukkVinduKnapp, bekreft);
        // "Lager" og viser vinduet
        Scene scene = new Scene(kulisser);
        setScene(scene);
        show();
    }

    private class LukkVindu implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            close();
        }
    }


    private class BekreftBehandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            close();
            kilden.bekreftSletting();
        }
    }


    private Pane byggPane(String feilmelding) {
        // Roed sirkel med hvitt kryss inni
        Circle runding = new Circle(50, 50, 30);
        runding.setFill(Color.valueOf("DF2403")); // roed
        // Krysset
        Line strek1 = new Line(62, 62, 38, 38);
        strek1.setStroke(Color.valueOf("F4F4F4")); // bakgrunnens hvit-farge
        strek1.setStrokeWidth(10);
        Line strek2 = new Line(38, 62, 62, 38);
        strek2.setStroke(Color.valueOf("F4F4F4")); // bakgrunnens hvit-farge
        strek2.setStrokeWidth(10);

        // Label med feilmeldingen
        Label feilLabel = new StyledLabel(feilmelding);
        feilLabel.setLayoutX(95);
        feilLabel.setLayoutY(10);
        feilLabel.setWrapText(true);
        feilLabel.setMaxWidth(WIDTH - 110);

        // Graatt felt der knappen er
        Rectangle boks = new Rectangle(WIDTH, 50);
        boks.setLayoutY(HEIGHT - 50);
        boks.setFill(Color.valueOf("D7D7D7"));
        boks.setStroke(Color.valueOf("D5D5D5"));

        Pane kulisser = new Pane(runding, strek1, strek2, feilLabel, boks);
        kulisser.setMaxSize(WIDTH, HEIGHT);
        kulisser.setMinSize(WIDTH, HEIGHT);
        return kulisser;
    }
}
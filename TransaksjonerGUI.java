import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ScrollPane;

import javafx.scene.paint.Color;
import javafx.scene.layout.*;

// Et GUI-element for aa vise en liste av Transaksjoner i et Scrolle-vindu, med en overskrift
class TransaksjonerGUI extends Pane {
    // Stoerrelse
    private final int WIDTH = 745;
    private final int HEIGHT = 700+30;

    // Label for aa gi beskjed om hva som vises naa
    private StyledLabel overskrift;
    // GridPane som holder paa alle transaksjonene som vises naa
    private GridPane transaksjonerSomVises;

    public TransaksjonerGUI() {
        transaksjonerSomVises = new GridPane();
        //skjulte = new List<Transaksjon>();
        
        overskrift = new StyledLabel("Ingen kontoutskrift valgt");
        overskrift.setMinSize(WIDTH, 52);
        overskrift.setMaxSize(WIDTH, 52);
        overskrift.settSomOverskrift();

        // Greier som har med ScrollBar/ScrollPane aa gjoere
        ScrollPane scrolleVindu = new ScrollPane(transaksjonerSomVises);
        scrolleVindu.fitToWidthProperty().set(true);
        scrolleVindu.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        // Stoerrelsen til Scrolle-vinduet elementet
        scrolleVindu.setMaxSize(WIDTH-10, HEIGHT - 60);
        scrolleVindu.setMinSize(WIDTH-10, HEIGHT - 60);
        scrolleVindu.setLayoutY(50);
        scrolleVindu.setLayoutX(5);
        scrolleVindu.setBorder(new Border(new BorderStroke(Color.valueOf("E5D5CE"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                new BorderWidths(2))));

        setWidth(WIDTH);
        setHeight(HEIGHT);

        getChildren().addAll(overskrift, scrolleVindu);
    }


    // Metode for aa oppdatere hvordan Transaksjoner som skal vises, med tilhoerende overskrift
    public void visDisse(String overskriften, List<Transaksjon> transaksjoner) {
        overskrift.setText(overskriften);

        transaksjonerSomVises.getChildren().clear();
        for (int i = 0; i < transaksjoner.length(); i++) {
            transaksjonerSomVises.addRow(i, transaksjoner.get(i));
        }
    }
}
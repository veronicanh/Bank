import javafx.scene.layout.Pane;
import javafx.scene.control.CheckBox;
import javafx.scene.shape.Line;

import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

// Klasse som representerer en transaksjon, er sin egen GUI-node
class Transaksjon extends Pane {
    private StyledLabel dato, tekst, beloep;
    private CheckBox avstemt;
    // private Kategori minKategori = null;
    private Sorteringsterminal terminal;
    private KategoriVelger velger;


    private final int VGAP = 5;

    public Transaksjon(String[] data, Sorteringsterminal terminalen) {
        terminal = terminalen;

        // Dato (format "dd.mm.aaaa")
        dato = new StyledLabel(data[0]);
        dato.setLayoutX(10);
        dato.setLayoutY(VGAP);

        // Tekst (format "vipps mat")
        tekst = new StyledLabel(data[1]);
        tekst.setLayoutX(120);
        tekst.setLayoutY(VGAP);

        // Beloep (format "2.395,90")
        beloep = new StyledLabel(data[2]);
        beloep.setLayoutX(420);
        beloep.setLayoutY(VGAP);
        beloep.settSomKrLabel();

        // Avstemt (format "ja"/"nei")
        avstemt = new CheckBox();
        avstemt.setLayoutX(520);
        avstemt.setLayoutY(VGAP);
        if (data[3].equals("ja")) {
            avstemt.setSelected(true);
        } else {
            avstemt.setSelected(false);
        }
        avstemt.setOnAction(new AvstemBehandler());

        // Tekstfelt der man velger kategorien til denne Transaksjonen
        velger = new KategoriVelger(terminal.hentAlleKategorier(), this);
        velger.setLayoutX(550);

        // Skillelinje som er nederst i hver Transaksjon
        Line skilleLinje = new Line(0, ((VGAP*2)+20), 735, ((VGAP * 2) + 20));
        skilleLinje.setStrokeWidth(0.5);
        skilleLinje.setStroke(Color.valueOf("D7D7D7"));

        getChildren().addAll(dato, tekst, beloep, avstemt, velger, skilleLinje);
    }

    // Metoder for aa hente/manipulere instans-variabler
    public String hentTekst() {
        return tekst.getText();
    }

    public double hentBeloep() {
        return Double.parseDouble(beloep.getText().replace(",", "."));
    }

    public boolean erAvstemt() {
        return avstemt.isSelected();
    }

    public String hentDato() {
        return dato.getText();
    }

    // Bytter verdien av avstemt til det motsatte, kalles fra en Kategori
    public KategoriVelger byttAvstemt() {
        if (avstemt.isSelected()) {
            avstemt.setSelected(false);
        } else {
            avstemt.setSelected(true);
        }
        return velger;
    }
    

    // Naar man trykker paa check-boxen for aa avstemme en Transaksjon
    private class AvstemBehandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent ae) {
            // Kan ikke gaa fra ikke valgt -> valgt
            if (avstemt.isSelected()) {
                avstemt.setSelected(false);
            }
            // Fjerner kategorien som er valgt
            else if (! avstemt.isSelected()) {
                avstemt.setSelected(true);
                velger.setKategori(null);
            }
        }
    }
}
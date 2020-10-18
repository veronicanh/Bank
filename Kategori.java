import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

// Klasse som holder paa informasjon om en kategori, samt transaksjonene knyttet til den
class Kategori extends Pane implements Comparable<Kategori> {
    // Kategoriens navn, og labler for de ulike summene
    //private StyledButton navn;
    private StyledLabel navn, utgift, inntekt, total;
    private final double VGAP = 2.5;

    // Liste med kodeord tilknyttet katoegorien, eks "rema" i Mat
    private SortedList<String> kodeord;
    protected List<Transaksjon> transaksjoner;

    // Inndata er paa formatet:
    // {"kategori-navn", "kodeord, kodeord"}
    public Kategori(String[] data) {
        // Legger inn kodeord
        kodeord = new SortedList<String>();
        if (data.length != 1) {
            String[] koder = data[1].split(",");
            for (String s : koder) {
                kodeord.add(s);
            }
        }
        transaksjoner = new List<Transaksjon>();

        // GUI-del
        navn = new StyledLabel(data[0]);
        navn.setLayoutX(10);
        navn.setLayoutY(VGAP);
        navn.setTextFill(Color.BLACK);

        utgift = new StyledLabel("0,00");
        utgift.setLayoutX(170);
        utgift.setLayoutY(VGAP);
        utgift.settSomKrLabel();
        utgift.setTextFill(Color.valueOf("753D37")); // moerk roed

        inntekt = new StyledLabel("0,00");
        inntekt.setLayoutX(260);
        inntekt.setLayoutY(VGAP);
        inntekt.settSomKrLabel();
        inntekt.setTextFill(Color.valueOf("5A6650")); // moerk groenn

        total = new StyledLabel("0,00");
        total.setLayoutX(360);
        total.setLayoutY(VGAP);
        total.settSomKrLabel();
        total.setTextFill(Color.BLACK);

        getChildren().addAll(navn, utgift, inntekt, total);
        setMinSize(450, 25);
        setMaxSize(450, 25);
    }

    // Metode som brukes for aa oeke eller minske verdien av inntekt/utgift-labelene
    public void oppdaterLabel(Transaksjon t, boolean plussePaa) {
        // Hvordan label skal oppdateres?
        StyledLabel label;
        if (t.hentBeloep() < 0) {
            label = utgift;
        } else {
            label = inntekt;
        }
        // Hva er det nye tallet til labelen?
        Double tall;
        if (plussePaa) {
            tall = Double.parseDouble(label.getText().replace(",", ".")) + t.hentBeloep();
        } else {
            tall = Double.parseDouble(label.getText().replace(",", ".")) - t.hentBeloep();
        }
        // Oppdaterer labelen
        label.setText(String.format("%.2f", tall));
        // Oppdaterer totalen
        double inn = Double.parseDouble(inntekt.getText().replace(",", "."));
        double ut = Double.parseDouble(utgift.getText().replace(",", "."));
        total.setText(String.format("%.2f", (inn + ut)));
    }


    // Sjekker om transaksjonen hoerer hjemme i denne kategorien (basert paa kodeordene)
    // Dersom den gjoer det, saa legges den til, transaksjonen blir avstemt, og returnerer true
    public boolean sjekkOmRiktigKategori(Transaksjon transaksjon) {
        for (String kode : kodeord) {
            if (transaksjon.hentTekst().contains(kode)) {
                leggTil(transaksjon);
                return true;
            }
        }
        return false;
    }

    // Legger til transaksjonen i denne kategorien og gjoer noedvendige aktiviteter
    public void leggTil(Transaksjon transaksjon) {
        // Legger til beloepet i variablene
        oppdaterLabel(transaksjon, true);
        // Legger til Transaksjonen i lista
        transaksjoner.add(transaksjon);
        // Avstemmer Transaksjonen
        KategoriVelger kv = transaksjon.byttAvstemt();
        kv.setKategori(this);
    }

    // Fjerner transaksjonen i denne kategorien og gjoer noedvendige aktiviteter
    public void fjern(Transaksjon transaksjon) {
        // Fjerner beloepet fra variablene
        oppdaterLabel(transaksjon, false);
        // Fjerner Transaksjonen fra lista
        transaksjoner.remove(transaksjon);
        // Av-avstemmer Transaksjonen
        transaksjon.byttAvstemt();
    }

    public String hentNavn() {
        return navn.getText();
    }

    // Sorteres etter navn
    public int compareTo(Kategori annen) {
        return (this.navn.getText().compareTo(annen.navn.getText()));
    }

    public void toem() {
        utgift.setText("0,00");
        inntekt.setText("0,00");
        total.setText("0,00");
        transaksjoner.clear();
    }

    public List<Transaksjon> hentTransaksjoner() {
        return transaksjoner;
    }

    public Pane forkortet() {
        if (total.getText().equals("0,00")) {
            return null;
        }
        CheckBox avsjekk = new CheckBox();
        avsjekk.setLayoutX(10);
        avsjekk.setLayoutY(VGAP + 2.5);

        StyledLabel navnet = new StyledLabel(navn.getText());
        navnet.setLayoutX(40);
        navnet.setLayoutY(VGAP + 2.5);
        navnet.setTextFill(Color.BLACK);

        StyledLabel totalen = new StyledLabel(total.getText());
        totalen.setLayoutX(160);
        totalen.setLayoutY(VGAP + 2.5);
        totalen.settSomKrLabel();

        avsjekk.setOnAction(new AvsjekkBehandler(navnet, totalen));

        Line skilleLinje = new Line(0, 30, 250, 30);
        skilleLinje.setStrokeWidth(0.5);
        skilleLinje.setStroke(Color.valueOf("ABA6A1"));

        Pane forkorta = new Pane(avsjekk,totalen, navnet, skilleLinje);
        forkorta.setMinSize(250, 30);
        forkorta.setMaxSize(250, 30);

        return forkorta;
    }


    private class AvsjekkBehandler implements EventHandler<ActionEvent> {
        private StyledLabel navnet, totalen;

        public AvsjekkBehandler(StyledLabel navnn, StyledLabel tot) {
            navnet = navnn;
            totalen = tot;
        }

        @Override
        public void handle(ActionEvent e) {
            if (navnet.getTextFill().equals(Color.BLACK)) {
                navnet.setTextFill(Color.valueOf("ABA6A1"));
                totalen.setTextFill(Color.valueOf("ABA6A1"));
            } else {
                navnet.setTextFill(Color.BLACK);
                totalen.settSomKrLabel();
            }
        }
    }
}
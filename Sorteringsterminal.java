import javafx.scene.layout.GridPane;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.geometry.Insets;

// Klasse som oppretter og tar vare paa kategorier. Brukes for aa sortere Transaksjoner
// Vaer obs paa spesielle kategorier (Avstemt fra foer og Ignorer)
class Sorteringsterminal extends Pane {
    private final int WIDTH = 464;

    private final String FILPATH = "/Users/Veronica/Documents/bank/Kategorier.txt";
    private SortedList<Kategori> kategorier;
    private Kategori avstemtFraFoer, ignorer;

    public Sorteringsterminal() throws FileNotFoundException {
        kategorier = new SortedList<Kategori>();
        lagKategorier();

        // GUI-del
        StyledLabel overskrift = new StyledLabel("Detaljert oversikt");
        overskrift.setMinSize(464, 52);
        overskrift.setMaxSize(464, 52);
        overskrift.settSomOverskrift();
        overskrift.setBackground(new Background(new BackgroundFill(Color.valueOf("B4AFAC"), CornerRadii.EMPTY, Insets.EMPTY)));
        overskrift.setBorder(new Border(new BorderStroke(Color.valueOf("ABA6A1"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        overskrift.setTextFill(Color.BLACK);

        GridPane kategoriGUI = new GridPane();
        kategoriGUI.addRow(0, new StyledLabel(" ")); // luft
        int i = 1;
        for (i = 1; i < kategorier.length() ; i++) {
            kategoriGUI.addRow(i, kategorier.get(i));
        }
        kategoriGUI.addRow(i++, new StyledLabel("")); // Luft
        // Legger til spesielle kategorier
        kategoriGUI.addRow(i++, ignorer);
        kategoriGUI.addRow(i++, avstemtFraFoer);

        setWidth(WIDTH);
        kategoriGUI.setBackground(new Background(new BackgroundFill(Color.valueOf("D0CDCC"), CornerRadii.EMPTY, Insets.EMPTY)));
        kategoriGUI.setLayoutY(37);
        kategoriGUI.setLayoutX(5);
        kategoriGUI.setBorder(new Border(new BorderStroke(Color.valueOf("ABA6A1"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));

        getChildren().addAll(kategoriGUI, overskrift);
    }

    private void lagKategorier() throws FileNotFoundException {
        // Leser inn kategorier fra fil
        Scanner fil = new Scanner(new File(FILPATH), "ISO-8859-1");
        fil.nextLine(); // Fjerner overskrift

        // Spesiell: Avstemt fra foer
        String[] avstemtListe = {"Avstemt fra før", ""};
        avstemtFraFoer = new Kategori(avstemtListe);
        // Spesiell: Ignorer
        String[] ignorerListe = {"Ignorer", ""};
        ignorer = new Kategori(ignorerListe);

        // Normale
        while (fil.hasNextLine()) {
            String[] data = fil.nextLine().split(";");
            kategorier.add(new Kategori(data));
        }
        fil.close();
    }

    public List<Kategori> hentAlleKategorier() {
        List<Kategori> alleKategorier = new List<>();
        for (Kategori k : kategorier) {
            alleKategorier.add(k);
        }
        alleKategorier.add(ignorer);
        alleKategorier.add(avstemtFraFoer);
        return alleKategorier;
    }

    // Mottar en transaksjon, og sorterer den (dersom det mulig) til riktig kategori
    public void sorterDenne(Transaksjon transaksjon) {
        // Sorterer kun ikke-avstemte transaksjoner
        if (transaksjon.erAvstemt()) {
            // Tilordnes kategorien "Avstemt fra foer"
            transaksjon.byttAvstemt();
            avstemtFraFoer.leggTil(transaksjon);
        } else {
            int indeks = 0;
            boolean plassert = false;
            // Itererer gjennom kategoriene til man finner en som matcher
            while (!plassert && indeks < kategorier.length()) {
                plassert = kategorier.get(indeks).sjekkOmRiktigKategori(transaksjon);
                indeks++;
            }
        }
    }

    public void toemKategorier() {
        for (Kategori k : kategorier) {
            k.toem();
            ignorer.toem();
            avstemtFraFoer.toem();
        }
    }

    public GridPane hentOppsummering() {
        GridPane oppsummering = new GridPane();
        for (int i = 0; i < kategorier.length(); i++) {
            Pane k = kategorier.get(i).forkortet();
            if (k != null) {
                oppsummering.addRow(i, k);
            }
        }
        oppsummering.setBackground(new Background(new BackgroundFill(Color.valueOf("D0CDCC"), CornerRadii.EMPTY, Insets.EMPTY)));
        return oppsummering;
    }
}
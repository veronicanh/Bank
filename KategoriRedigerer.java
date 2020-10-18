import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.Node;

// Lese til og fra fil
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

// Event
import javafx.event.ActionEvent;
import javafx.event.EventHandler;


// Klasse for aa kunne endre de forhandsdefinerte kategoriene
// Helt uavhengig fra alt annet
class KategoriRedigerer extends Stage implements BerOmInput {
    private GridPane linjePane;
    private int indeks = 0;
    private final String FILPATH = "/Users/Veronica/Documents/bank/Kategorier.txt";

    public KategoriRedigerer() {
        linjePane = new GridPane();
        ScrollPane scrolleVindu = new ScrollPane(linjePane);
        scrolleVindu.setMaxSize(1440, 710);
        scrolleVindu.setMinSize(1440, 710);
        scrolleVindu.fitToWidthProperty().set(true);
        scrolleVindu.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrolleVindu.setLayoutY(42);

        // Legger til alle linjene, lest inn fra fil
        try {
            lesInnFil();
        } catch (Exception e) {}
        Pane kulisser = new Pane(scrolleVindu, byggToolBar());
        Scene scene = new Scene(kulisser);
        setTitle("Test");
        setScene(scene);
        show();
    }

    private void lesInnFil() throws FileNotFoundException {
        Scanner fil = new Scanner(new File(FILPATH), "ISO-8859-1");
        fil.nextLine(); // Fjerner overskrift
        while (fil.hasNextLine()) {
            linjePane.addRow(indeks++, new Linje(fil.nextLine()));
            linjePane.addRow(indeks++, new StyledLabel("......................................................................................................................................................"));
        }
        fil.close();
    }
   

    private class LagNyBehandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            new SkrivInnInfo(KategoriRedigerer.this);
        }
    }

    // Lager ny Linje
    public void ferdigAaSkriveInn(String svar) {
        linjePane.addRow(indeks++, new Linje(svar));
        linjePane.addRow(indeks++, new StyledLabel("......................................................................................................................................................"));

    }

    private class AvbrytBehandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            close();
        }
    }

    private class LagreBehandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            String nyFil = "Kategori-navn;Kodeord,separert med komma,kan fint bruke mellomrom i kodeordet\n";
            for (Node n : linjePane.getChildren()) {
                if (n instanceof Linje && n.isVisible()) {
                    nyFil += n.toString() + "\n";
                }
            }
            // Fjerner overfloedig "\n"
            nyFil = nyFil.trim();

            // Skriver til fil
            try {
                Writer p = new OutputStreamWriter(new FileOutputStream(FILPATH), StandardCharsets.ISO_8859_1);
                p.write(nyFil);
                p.close();
            } catch (Exception ee) {
            }
            close();
        }
    }

    private ToolBar byggToolBar() {
        // Knapp for aa lage en ny kategori
        StyledButton lagNyLinje = new StyledButton("Lag ny kategori");
        lagNyLinje.setOnAction(new LagNyBehandler());

        // Knapp for aa avbryte
        StyledButton avbryt = new StyledButton("Avbryt");
        avbryt.setOnAction(new AvbrytBehandler());

        // Knapp for aa lagre endringene
        StyledButton lagre = new StyledButton("Lagre endringer");
        lagre.setOnAction(new LagreBehandler());

        // Beholder for alle knappene
        ToolBar toolBar = new ToolBar(lagNyLinje);
        toolBar.getItems().addAll(new StyledLabel(""), new Separator(), new StyledLabel("")); // Skillelinje
        toolBar.getItems().addAll(lagre);
        toolBar.getItems().addAll(new StyledLabel(""), new Separator(), new StyledLabel("")); // Skillelinje
        toolBar.getItems().addAll(avbryt);
        toolBar.setPrefWidth(1440);
        return toolBar;
    }
}
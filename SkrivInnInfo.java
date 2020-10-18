import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

class SkrivInnInfo extends Stage {
    BerOmInput eier;
    // Liste som tar vare paa alle tekst-feltene
    List<TextField> bokser;
    GridPane kulisser;
    StyledButton plusser;

    // Naar man kun skal lage en helt ny kategori
    public SkrivInnInfo(KategoriRedigerer eieren) {
        eier = eieren;
        int plassering = 0;

        bokser = new List<TextField>();

        kulisser = new GridPane();
        // Venstre kolonne
        kulisser.add(new StyledLabel("Navn:", 1), 0, 0);
        kulisser.add(new StyledLabel(""), 0, 1); // Luft
        kulisser.add(new StyledLabel("Kodeord:", 1), 0, 2);

        // Hoyre kolonne
        // Navn
        TextField skrivInnNavn = new TextField();
        skrivInnNavn.setFont(new Font("Courier New", 16));
        bokser.add(skrivInnNavn);
        kulisser.add(skrivInnNavn, 1, plassering++);
        plassering++;
        kulisser.setVgap(5);

        // Kodeord
        TextField skrivInnKode = new TextField();
        skrivInnKode.setFont(new Font("Courier New", 16));
        bokser.add(skrivInnKode);
        kulisser.add(skrivInnKode, 1, plassering++);
        // Knapp for aa legge til nytt kodeord
        plusser = new StyledButton("+");
        plusser.setOnAction(new PlusserBehandler());
        kulisser.add(plusser, 1, plassering);

        kulisser.setLayoutY(50);
        kulisser.setLayoutX(10);
        kulisser.setMinHeight(300);
        kulisser.setMaxHeight(300);
        Scene scene = new Scene(new Pane(byggToolBar(), kulisser));
        setTitle("Lag ny kategori");
        setScene(scene);
        show();
    }

    // Naar man kun skal skrive inn ett kodeord
    public SkrivInnInfo(Linje eieren) {
        eier = eieren;
        int plassering = 0;

        bokser = new List<TextField>();
        kulisser = new GridPane();
        // Venstre kolonne
        kulisser.add(new StyledLabel("Kodeord:",1), 0, 0);

        // Hoyre kolonne
        // Kodeord
        TextField skrivInnKode = new TextField();
        skrivInnKode.setFont(new Font("Courier New", 16));
        bokser.add(skrivInnKode);
        kulisser.add(skrivInnKode, 1, plassering++);

        kulisser.setLayoutY(50);
        kulisser.setLayoutX(10);
        kulisser.setMinHeight(38);
        kulisser.setMaxHeight(38);
        Scene scene = new Scene(new Pane(byggToolBar(), kulisser));
        setTitle("Legg til kodeord");
        setScene(scene);
        show();
    }


    private class PlusserBehandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            int indeks = GridPane.getRowIndex(plusser);
            kulisser.getChildren().remove(plusser);

            // Lager ny Tekstboks
            TextField skrivInnKode = new TextField();
            skrivInnKode.setFont(new Font("Courier New", 16));
            bokser.add(skrivInnKode);
            kulisser.add(skrivInnKode, 1, indeks++);

            // Legger til plusseren igjen
            kulisser.add(plusser, 1, indeks);
        }
    }

    private class FerdigBehandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            String svarene = "";
            // Navnet, med stor foerste-bokstav
            String navn = bokser.get(0).getText().toLowerCase();

            if (eier instanceof Linje) {
                if (navn.length() == 0) {
                    new ErrorPopup("Obs! Du må skrive inn noe i feltet");
                    return;
                }
                svarene += navn;
            } else {
                if (navn.length() == 0) {
                    new ErrorPopup("Obs! Du må skrive inn noe i feltet 'Navn'");
                    return;
                }
                navn = navn.substring(0, 1).toUpperCase() + navn.substring(1);
                svarene += navn + ";";
            }

            // Kodeordene (smaa bokstaver), adskilt av ","
            for (int i = 1; i < bokser.length(); i++) {
                String kodeOrd = bokser.get(i).getText().toLowerCase();
                svarene += kodeOrd + ",";
            }
            
            eier.ferdigAaSkriveInn(svarene);
            close();
        }
    }

    private class AvbrytBehandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            close();
        }
    }



    private ToolBar byggToolBar() {
        StyledButton ferdig = new StyledButton("Ferdig");
        ferdig.setOnAction(new FerdigBehandler());

        StyledButton avbryt = new StyledButton("Avbryt");
        avbryt.setOnAction(new AvbrytBehandler());

        // Beholder for alle knappene
        ToolBar toolBar = new ToolBar(ferdig);
        toolBar.getItems().addAll(new StyledLabel(""), new Separator(), new StyledLabel("")); // Skillelinje
        toolBar.getItems().addAll(avbryt);
        toolBar.setPrefWidth(235);
        return toolBar;
    }
}
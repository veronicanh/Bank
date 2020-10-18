import javafx.scene.layout.GridPane;
// Event
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

// Klasse som viser informasjonen fra en linje, og lar brukeren redigere informasjonen
class Linje extends GridPane implements BerOmInput {
    private StyledLabel navn;
    private StyledButton trykketPaaDenne;
    private List<String> alleKodeOrd;

    public Linje(String info) {
        StyledButton slett = new StyledButton("Slett");
        slett.settSomKodeOrd();
        slett.setOnAction(new SlettLinjeBehandler());
        addColumn(0, slett);
        setHgap(5);

        String[] data = info.split(";");

        navn = new StyledLabel(data[0]);
        navn.setMinSize(150, 40);
        navn.setMaxSize(150, 40);
        navn.settSomOverskrift();
        addColumn(1, navn);
        
        alleKodeOrd = new List<String>();
        // Legger til evt. kodeord til linjen
        int plassering = 2;
        if (data.length != 1) {
            String[] kodeOrd = data[1].split(",");
            for (int i = 0; i < kodeOrd.length; i++) {
                alleKodeOrd.add(kodeOrd[i]);
                StyledButton kode = new StyledButton(kodeOrd[i]);
                // Fjerner kodeordet dersom man trykker paa det
                kode.setOnAction(new FjernKodeBehandler());
                kode.settSomKodeOrd();
                //kode.setPickOnBounds(value);
                addColumn(plassering++, kode);
            }
        }

        StyledButton nyKodeKnapp = new StyledButton("+");
        nyKodeKnapp.setOnAction(new NyKodeBehandler());
        nyKodeKnapp.settSomKodeOrd();
        addColumn(plassering++, nyKodeKnapp);
    }

    public String hentNavn() {
        return navn.getText();
    }
    
    // For aa slette hele linjen
    private class SlettLinjeBehandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            String tekst = "Er du sikker på at du vil slette kategorien '" + navn.getText() + "'?";
            new ErrorPopup(Linje.this, tekst);
        }
    }

    public void bekreftSletting() {
        setManaged(false);
        setVisible(false);
    }

    // Fjerne et kodeord dersom man trykker paa det
    private class FjernKodeBehandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            StyledButton slettDenne = (StyledButton) e.getSource();
            boolean fjernetFraFoer = slettDenne.fargelegg();

            if (fjernetFraFoer) {
                alleKodeOrd.add(slettDenne.getText());
            } else {
                alleKodeOrd.remove(slettDenne.getText());
            }
        }
    }

    // Legge til et nytt kodeord
    private class NyKodeBehandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            trykketPaaDenne = (StyledButton) e.getSource();
            new SkrivInnInfo(Linje.this);
        }
    }

    public void ferdigAaSkriveInn(String innskrevet) {
        alleKodeOrd.add(innskrevet);
        // Fjerner "+"-knappen midlertidlig
        int indeks = GridPane.getColumnIndex(trykketPaaDenne);
        Linje.this.getChildren().remove(trykketPaaDenne);

        StyledButton nyKode = new StyledButton(innskrevet);
        nyKode.settSomKodeOrd();
        nyKode.setOnAction(new FjernKodeBehandler());

        // Legger "+"- og kodeOrd-knappene til igjen
        addColumn(indeks++, nyKode);
        addColumn(indeks, trykketPaaDenne);
    }

    @Override
    public String toString() {
        String str = navn.getText() + ";";
        for (String s : alleKodeOrd) {
            str += s + ",";
        }
        // Fjerner overfloedig ","
        if (str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }
}
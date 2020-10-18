import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.control.Button;

import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
// For aa haandtere hendelser
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
// For aa finne fram til data-filene
import java.io.File;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.geometry.Insets;

// Fillesing
import java.util.Scanner;
import java.io.FileNotFoundException;

import javafx.scene.text.Font;
// Til popup-menyen
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.geometry.Side;
// Overvaaker
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;




public class Banksystem extends Application {
    // For aa kunne bruke Stagen i EventHandler-klasser
    private Stage hovedteater;
    // Filen som systemet jobber med for oeyeblikket
    private File fil = null;
    // Kontoen og terminalen
    private Sorteringsterminal terminal;

    private TransaksjonerGUI scrolleTing;
    private List<Transaksjon> alleTransaksjoner;

  
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage teater) {     
        hovedteater = teater;
        // Sorteringsterminalen, bla.a. GUI for Kategorier
        try {
            terminal = new Sorteringsterminal();
        } catch (Exception e) {
            new ErrorPopup("Noe gikk galt i terminal: " + e);
        }
        terminal.setLayoutX(790); // *denna e random og maa endres
        terminal.setLayoutY(55); // * denna e random og maa endres

        // Beholder for alle knappene
        ToolBar toolBar = byggToolBar();

        // GUI-element som viser Transaksjonene
        alleTransaksjoner = new List<Transaksjon>();
        scrolleTing = new TransaksjonerGUI();
        scrolleTing.setLayoutX(15);
        scrolleTing.setLayoutY(55);

        Pane kulisser = new Pane(toolBar, scrolleTing, terminal);
        kulisser.setBackground(
                new Background(new BackgroundFill(Color.valueOf("C8C2BF"), CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(kulisser, scrolleTing.getWidth() + terminal.getWidth() + 60, scrolleTing.getHeight() + 10 + 50);
        //scene.getStylesheets().add("farger.css"); // proevde meg litt paa CSS     
        teater.setTitle("Banksystem");
        teater.setScene(scene);
        teater.show();
    }

    // Utskilt her sia start blei saa lang
    private ToolBar byggToolBar() {
        // Knapp for aa velge fil
        Button filVelger = new StyledButton("Velg kontoutskrift");
        filVelger.setOnAction(new VelgFilBehandler());

        // Knapp for aa skjule avstemte transaksjoner
        Button skjulAvstemte = new StyledButton("Skjul avstemte");
        skjulAvstemte.setOnAction(new SkjulBehandler());

        // Knapp for aa vise alle transaksjoner
        Button visAlle = new StyledButton("Vis alle");
        visAlle.setOnAction(new VisAlleBehandler());

        // Knapp for aa velge hvordan Kategori man vil se Transaksjoner fra
        Button bakgrunn = new StyledButton("");
        bakgrunn.setMinSize(150, 30);
        bakgrunn.setMaxSize(150, 30);
        MenyKategoriVelger forgrunn = new MenyKategoriVelger(terminal.hentAlleKategorier());
        Pane velgKat = new Pane(bakgrunn, forgrunn);
  
        // Knapp for aa vise oversikt over alle kategorier osv
        Button oppsummering = new StyledButton("Oppsummering");
        oppsummering.setOnAction(new OppsummeringBehandler());

        // Knapp for aa kunne endre kategoriene
        Button redigerKategori = new StyledButton("Rediger kategorier");
        redigerKategori.setOnAction(new KategoriRedigererBehandler());        

        // Knapp for aa avslutte
        Button avslutt = new StyledButton("Avslutt");
        avslutt.setOnAction(new AvsluttBehandler());

        // Beholder for alle knappene
        ToolBar toolBar = new ToolBar(filVelger);
        toolBar.getItems().addAll(new StyledLabel(""), new Separator(), new StyledLabel("")); // Skillelinje
        toolBar.getItems().addAll(visAlle, new StyledLabel(""), skjulAvstemte, new StyledLabel(""), velgKat);
        toolBar.getItems().addAll(new StyledLabel(""), new Separator(), new StyledLabel("")); // Skillelinje
        toolBar.getItems().addAll(oppsummering);
        toolBar.getItems().addAll(new StyledLabel(""), new Separator(), new StyledLabel("")); // Skillelinje
        toolBar.getItems().addAll(redigerKategori);
        toolBar.getItems().addAll(new StyledLabel(""), new Separator(), new StyledLabel("")); // Skillelinje
        toolBar.getItems().addAll(avslutt);
        toolBar.setPrefWidth(1440);
        return toolBar;
    }

    private class VelgFilBehandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            // Bruker velger fil
            FileChooser filVelger = new FileChooser();
            // Kan kun velge "-csv"-filer
            filVelger.getExtensionFilters().add(new ExtensionFilter("CSV (kontoutskrifter)", "*.csv"));
            // Default mappe er nedlastinger (siden jeg henter utskriftene fra nett)
            filVelger.setInitialDirectory(new File("/Users/Veronica/Downloads"));
            // Bruker velger fil
            fil = filVelger.showOpenDialog(hovedteater);

            if (fil != null) {
                try {
                    // Toemmer sorterings-terminalen slik at den kan fylles av nye transaksjoner
                    terminal.toemKategorier();
                    // Leser inn Transaksjonene
                    lesInnFil(fil);
                } catch (ArrayIndexOutOfBoundsException ee) {
                    new ErrorPopup("Valgt fil har feil format. Rett format:\nDato;Tekst;Beløp;Saldo;Status;Avstemt");
                } catch (Exception ee) {
                    new ErrorPopup("Noe gikk galt i konto: " + e);
                }
                
                // Viser transaksjonene i GUI-et
                scrolleTing.visDisse("Alle transaksjoner (" + alleTransaksjoner.length() + " stk)", alleTransaksjoner);
                // Sorterer transaksjonene
                for (Transaksjon t : alleTransaksjoner) {
                    terminal.sorterDenne(t);
                }
            }
        }
    }

    private class SkjulBehandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            List<Transaksjon> ikkeAvstemte = new List<Transaksjon>();
            for (Transaksjon t : alleTransaksjoner) {
                if (! t.erAvstemt()) {
                    ikkeAvstemte.add(t);
                }
            }
            scrolleTing.visDisse("Ikke avstemte transaksjoner (" + ikkeAvstemte.length() + " stk)", ikkeAvstemte);
        }
    }

    private class VisAlleBehandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            scrolleTing.visDisse("Alle transaksjoner (" + alleTransaksjoner.length() + " stk)", alleTransaksjoner);
        }
    }

    // For et pop-up-vindu med kategorier som har en total != 0
    private class OppsummeringBehandler implements EventHandler<ActionEvent> {
        private Stage teateret;

        @Override
        public void handle(ActionEvent e) {
            GridPane oppsum = terminal.hentOppsummering();
            oppsum.setLayoutX(10);
            oppsum.setLayoutY(10);

            StyledButton tilbake = new StyledButton("Tilbake");
            tilbake.setLayoutX(10+25);
            tilbake.setLayoutY(((oppsum.getChildren().size()) *30) + 20);
            tilbake.setOnAction(new TilbakeBehandler());

            
            StyledButton avslutt = new StyledButton("Avslutt");
            avslutt.setLayoutX(120+25);
            avslutt.setLayoutY(((oppsum.getChildren().size()) *30) + 20);
            avslutt.setOnAction(new TilbakeBehandler());            

            Pane kulisser = new Pane(oppsum, tilbake, avslutt);
            kulisser.setBackground(
                    new Background(new BackgroundFill(Color.valueOf("C8C2BF"), CornerRadii.EMPTY, Insets.EMPTY)));
            Scene scene = new Scene(kulisser, 270, ((oppsum.getChildren().size()) * 30) + 20 + 40);
            teateret = new Stage();
            teateret.setTitle("Oppsummering");
            teateret.setScene(scene);
            teateret.show();
        }

        private class TilbakeBehandler implements EventHandler<ActionEvent> {
            @Override
            public void handle(ActionEvent e) {
                teateret.close();
                StyledButton knapp = (StyledButton) e.getSource();
                if (knapp.getText().equals("Avslutt")) {
                    hovedteater.close();
                }
            }
        }
    }

    private class AvsluttBehandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            hovedteater.close();
        }
    }

    // Metode for aa lese inn og opprette transaksjoner basert paa en .csv-fil
    public void lesInnFil(File fil) throws FileNotFoundException {
        alleTransaksjoner.clear();
        // Leser inn transaksjoner fra fil
        Scanner filLeser = new Scanner(fil, "ISO-8859-1");
        // Fjerner foerste raden (overskrift)
        filLeser.nextLine();

        while (filLeser.hasNextLine()) {
            String[] linje = filLeser.nextLine().split(";");
            String[] data = { linje[0], linje[1], linje[2], linje[5] };
            // Formatering
            for (int i = 0; i < data.length; i++) {
                data[i] = data[i].replaceAll("\"", "");
                data[i] = data[i].trim();
                data[i] = data[i].toLowerCase();
            }
            // Fjerner 1000-markering fra beloep-stringen
            data[2] = data[2].replaceAll("\\.", "");
            // Legger den nye Transaksjonen til i GridPanet/kontoen
            alleTransaksjoner.add(new Transaksjon(data, terminal));
        }
        filLeser.close();
    }

    // Okei saa dether blei mye tull, men det her e modifisert versjon av KategoriVelger,
    // som e putta inni her som en indre klasse for aa kunne endre ting i TransaksjonGUI.
    // Mye tull, og finnes garantert en myye bedre maate å gjøre det paa. Men gadd ikke aa
    // finne ut av kordan arv av indre klassa fungerte :)
    private class MenyKategoriVelger extends TextField {
        //public TextField skrivInn;
        private ContextMenu kategorierPopup;
        private List<Kategori> kategorier;

        public MenyKategoriVelger(List<Kategori> alleKategorier) {
            super("Velg kategori");
            setFont(new Font("Courier New", 16));
            Color gjennomsiktig = Color.rgb(0, 0, 0, 0);
            setBackground(new Background(new BackgroundFill(gjennomsiktig, CornerRadii.EMPTY, Insets.EMPTY)));
            setMinSize(150, 30);
            setMaxSize(150, 30);
            setOnMouseClicked(new KlikkBehandler());
            textProperty().addListener(new TextFieldListener());

            kategorierPopup = new ContextMenu();

            kategorier = alleKategorier;
        }
        
        // Klasse som overvaaker TextFielden, og oppdaterer popup-menyen
        private class TextFieldListener implements ChangeListener<String> {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s1, String s2) {
                // Dersom TextField-en er tom, saa vises alle alternativene
                if (getText().length() == 0) {
                    populatePopup(kategorier);
                    if (!kategorierPopup.isShowing()) {
                        kategorierPopup.show(MenyKategoriVelger.this, Side.BOTTOM, 0, 0);
                    }
                }
                // Dersom det skrives noe inn i TextField-en
                else {
                    List<Kategori> searchResult = new List<Kategori>();
                    // Leter gjennom alternativene etter de som matcher det som er skrevet inn i
                    // feltet
                    for (Kategori kategori : kategorier) {
                        // Dersom navnet til kategorien starter med det som er skrevet inn
                        if ((kategori.hentNavn().toLowerCase()).startsWith(getText().toLowerCase())) {
                            searchResult.add(kategori);
                        }
                    }
                    // Putter matchene inn i popup-menyen
                    populatePopup(searchResult);
                    // Viser popup-en
                    if (!kategorierPopup.isShowing()) {
                        kategorierPopup.show(MenyKategoriVelger.this, Side.BOTTOM, 0, 0);
                    }
                }
            }
        }

        // Fyller menyen med alternativene som matcher det som er skrevet inn
        private void populatePopup(List<Kategori> searchResult) {
            kategorierPopup.getItems().clear();
            // Lager menu-items for alle treff, og legger dem til i popup-menyen
            for (int i = 0; i < searchResult.length(); i++) {
                String result = searchResult.get(i).hentNavn();
                StyledLabel label = new StyledLabel(result);
                label.settSomMenuItem();
                CustomMenuItem item = new CustomMenuItem(label, true);
                item.setOnAction(new TrykketPaaAlternativ(searchResult.get(i)));
                kategorierPopup.getItems().add(item);
            }
        }

        // Dette skjer naar man velger et av alternativene fra popup-menyen
        private class TrykketPaaAlternativ implements EventHandler<ActionEvent> {
            Kategori kategori;

            public TrykketPaaAlternativ(Kategori kategorien) {
                kategori = kategorien;
            }

            @Override
            public void handle(ActionEvent e) {
                setText("Velg kategori");
                scrolleTing.visDisse(kategori.hentNavn() + " (" + kategori.hentTransaksjoner().length() + " stk)", kategori.hentTransaksjoner());
            }
        }
        
        private class KlikkBehandler implements EventHandler<MouseEvent>{
            @Override
            public void handle(MouseEvent e) {
                setText("");
                                    populatePopup(kategorier);
                    if (!kategorierPopup.isShowing()) {
                        kategorierPopup.show(MenyKategoriVelger.this, Side.BOTTOM, 0, 0);
                    }
            }
        }
    }

    private class KategoriRedigererBehandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            new KategoriRedigerer();
        }
    }
}
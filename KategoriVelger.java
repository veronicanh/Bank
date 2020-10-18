import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
// Til popup-menyen
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.geometry.Side;
// Hendelser
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
// Overvaaker
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

// Huff, haape du aldri treng aa se over koden her igjen noen gang
class KategoriVelger extends TextField {
    // Alternativene man kan velge mellom
    private final List<Kategori> alternativer;
    // Popup-menyen som kommer opp naar man skriver noe
    private ContextMenu alternativerPopup;
    // Transaksjonen denne velgeren tilhoerer
    private final Transaksjon minTransaksjon;

    private Kategori valgtKategori = null;

    public KategoriVelger(List<Kategori> alternativ, Transaksjon transaksjon) {
        alternativer = alternativ;
        minTransaksjon = transaksjon;

        alternativerPopup = new ContextMenu();

        textProperty().addListener(new TextFieldListener());
        setText("-");
        setOnAction(new TrykkeEnterBehandler());
        setFont(new Font("Courier New", 16));
        setOnMousePressed(new ToemFelt());

        setMinWidth(170);
        setMaxWidth(170);
        setMinHeight(30);
        setMaxHeight(30);
    }

    // Klasse som overvaaker TextFielden, og oppdaterer popup-menyen
    private class TextFieldListener implements ChangeListener<String> {
        @Override
        public void changed(ObservableValue<? extends String> observableValue, String s1, String s2) {
            // Dersom man endrer paa teksten i en Transaksjon som har en Kategori
            if (valgtKategori != null) {
                if (!getText().equals(valgtKategori.hentNavn())) {
                    valgtKategori.fjern(minTransaksjon);
                    valgtKategori = null;
                }
            }
            // Dersom TextField-en er tom, saa vises alle alternativene
            if (getText().length() == 0) {
                populatePopup(alternativer);
                if (!alternativerPopup.isShowing()) {
                    alternativerPopup.show(KategoriVelger.this, Side.RIGHT, 0, 0);
                }
            }
            // Dersom det skrives noe inn i TextField-en
            else {
                List<Kategori> searchResult = new List<>();
                // Leter gjennom alternativene etter de som matcher det som er skrevet inn i feltet
                for (Kategori kategori : alternativer) {
                    // Dersom navnet til kategorien starter med det som er skrevet inn
                    if ((kategori.hentNavn().toLowerCase()).startsWith(getText().toLowerCase())) {
                        searchResult.add(kategori);
                    }
                }
                // Putter matchene inn i popup-menyen             
                populatePopup(searchResult);
                // Viser popup-en
                if (!alternativerPopup.isShowing()) {
                    alternativerPopup.show(KategoriVelger.this, Side.RIGHT, 0, 0);
                }
            }
        }
    }

    // Fyller menyen med alternativene som matcher det som er skrevet inn
    private void populatePopup(List<Kategori> searchResult) {
        alternativerPopup.getItems().clear();
        // Lager menu-items for alle treff, og legger dem til i popup-menyen
        for (int i = 0; i < searchResult.length(); i++) {
            String result = searchResult.get(i).hentNavn();
            StyledLabel label = new StyledLabel(result);
            label.settSomMenuItem();
            CustomMenuItem item = new CustomMenuItem(label, true);
            item.setOnAction(new TrykketPaaAlternativ(searchResult.get(i)));
            alternativerPopup.getItems().add(item);
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
            if (valgtKategori == null) {
                kategori.leggTil(minTransaksjon);
            } else {
                valgtKategori.fjern(minTransaksjon);
                kategori.leggTil(minTransaksjon);
            }
        }
    }

    private class TrykkeEnterBehandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            if (!alternativerPopup.isShowing()) {
                alternativerPopup.show(KategoriVelger.this, Side.RIGHT, 0, 0);
            }
        }
    }

    // DENNA E BUGGY !!!!!

    // Sjekker om er noen gyldige Kategorier etter man skriver inn og trykker enter
    /*private class TrykkeEnterBehandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            // For aa unngaa at man kan trykke enter dersom man allerede har en gyldig Kategori
            if (valgtKategori == null) {
                // Sjekker om vi har en gyldig kategori
                for (Kategori kategori : alternativer) {
                    // Dersom navnet til kategorien er det samme som skrevet inn naar man trykker enter
                    if ((kategori.hentNavn().toLowerCase()).startsWith(getText().toLowerCase())) {
                        valgtKategori = kategori;
                        //setText(valgtKategori.hentNavn());
                        valgtKategori.leggTil(minTransaksjon);
                    }
                }
            }
        }
    }*/


    // Valgt kategori settes fra en Kategori
    public void setKategori(Kategori kategori) {
        if (kategori == null) {
            setText("-");
        } else {
            setText(kategori.hentNavn());
        }
        valgtKategori = kategori;
    }

    private class ToemFelt implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent e) {
            if (getText().equals("-")) {
                setText("");
            }
        }
    }
}
package UI;

import Rezeptteile.Rezeptkopf;
import Rezeptteile.Rezeptzutat;
import Rezeptteile.Zutat;
import controller.RezeptkopfController;
import controller.ZutatenController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public class ZutatHinzufuegenController {
    public Button btnReturnHome;
    public Button btnDefinitionsbuch;
    public Button btnNeuesRezept;
    public Button btnExit;
    public Button btnZutatHinzufuegen;
    public Button btnBearbeiten;
    public Button btnSpeichern;
    public Button btnLoeschen;

    public TextArea textMenge;
    public TextArea textEinheit;

    public ListView listZutaten;
    public TextField textTitel;
    public Button btnZurueck;

    private Stage stage;
    private Scene scene;
    private boolean neueZutat = false;

    ZutatenController zutatenController = ZutatenController.getInstance();
    RezeptkopfController rezeptkopfController = RezeptkopfController.getInstance();

    public void initialize() throws IOException {
        if (RezeptDetailsController.rezeptzutatUebertrag != null) {
            textTitel.setText(RezeptDetailsController.rezeptzutatUebertrag.getrZuZutat().getZutName());
            textMenge.setText("" + RezeptDetailsController.rezeptzutatUebertrag.getrZuMenge());
            textEinheit.setText(RezeptDetailsController.rezeptzutatUebertrag.getrZuEinheit());
        }
        RezeptDetailsController.rezeptzutatUebertrag = null;//TODO ergibt es Sinnn hier den Übertrag zu nullen?
        updateList();
    }

    public void returnHome(ActionEvent actionEvent) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/HauptmenuV3.fxml")));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Kochbuch: Startseite");
        stage.show();
    }

    public void openDefinitionsbuch(ActionEvent actionEvent) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/DefinitionsbuchV2.fxml")));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Kochbuch: Definitionsbuch");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void addRezept(ActionEvent actionEvent) throws Exception {
        UIController.neuesRezept = true;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/RezeptAnsehen.fxml")));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Kochbuch: Rezeptansicht");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void programmSchliessen(ActionEvent actionEvent) {
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.close();
    }

    public void zutatHinzufügen(ActionEvent actionEvent) {
        textLoeschen();
        neueZutat = true;
        textfelderEditierbar(true);
    }

    private void updateList() throws IOException {
        zutatenController.leseDatei();
        ObservableList zutaten = FXCollections.observableArrayList();
        for (Zutat zutat : zutatenController.getAlleZutaten()) {
            zutaten.add(zutat.getZutName());
        }
        sortierenListe(zutaten);
        listZutaten.setItems(zutaten);
        listZutaten.refresh();
    }

    private ObservableList sortierenListe(ObservableList<String> liste) {
        liste.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        return liste;
    }

    public void listZutatenKlicked(MouseEvent mouseEvent) {
        textLoeschen();
        if (!Objects.isNull(listZutaten.getSelectionModel().getSelectedItem())) {
            Rezeptkopf zugehoerigRezeptkopf = UIController.uebertrag;
            String zutat = listZutaten.getSelectionModel().getSelectedItem().toString();
            textTitel.setText(zutat.trim());
            for (int i = 0; i < zugehoerigRezeptkopf.getrKoRezeptzutat().size(); i++) {
                Rezeptzutat rezeptzutat = zugehoerigRezeptkopf.getrKoRezeptzutat().get(i);
                String zutatname = rezeptzutat.getrZuZutat().getZutName();
                if (zutat.equals(zutatname)) {
                    textMenge.setText("" + rezeptzutat.getrZuMenge());
                    textEinheit.setText(rezeptzutat.getrZuEinheit());
                }
            }
        }
    }

    public void zutatBearbeiten(ActionEvent actionEvent) {
        textfelderEditierbar(true);
    }

    public void zutatSpeichern(ActionEvent actionEvent) throws IOException { //TODO ändern der Dateien reparierren
        if (textTitel.getText().matches(".*\\S+.*")) {//Regex: Enthält mindestens ein nicht Leerzeichen
            if (neueZutat) {
                if (zutatenController.existiertZutat(textTitel.getText().trim())) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Ein Problem ist aufgetreten");
                    alert.setHeaderText("Zutat bereits vorhanden");
                    alert.setContentText("Diese Zutat existiert bereits. Falls Sie den Inhalt ändern wollen wählen Sie Diese bitte aus und klicken Sie Bearbeiten");
                    alert.showAndWait();
                } else {
                    Zutat neueZutat = zutatenController.neueZutat(textTitel.getText().trim());
                    zutatenController.speichernDatei();
                }
            }
            Rezeptkopf zugehoerigerRezeptkopf = UIController.uebertrag;
            boolean zutatGefunden = false;
            if (textMenge.getText().matches("^\\d+\\.?\\d*$")) { //regex: ein oder mehr Zahlen, ein Punkt, null bis beliebig viele Zahlen, Ende
                if (zugehoerigerRezeptkopf.getrKoRezeptzutat().size() > 0) {
                    for (Rezeptzutat rezZutat : zugehoerigerRezeptkopf.getrKoRezeptzutat()) {
                        if (rezZutat.getrZuZutat().getZutName().equals(textTitel.getText().trim())) {
                            zutatGefunden = true;
                            rezZutat.setrZuEinheit(textEinheit.getText().trim());
                            rezZutat.setrZuMenge(Double.parseDouble(textMenge.getText().trim()));
                            textfelderEditierbar(false);
                            rezeptkopfController.speichernDatei();
                        }
                    }
                }
                if (!zutatGefunden) {
                    double menge = Double.parseDouble(textMenge.getText().trim());
                    Rezeptzutat neueRezeptzutat = new Rezeptzutat(menge, textEinheit.getText(), zutatenController.getZutat(textTitel.getText().trim()));
                    zugehoerigerRezeptkopf.zutatHinzufuegen(neueRezeptzutat);
                    textfelderEditierbar(false);
                    rezeptkopfController.speichernDatei();
                }
            } else {
                Alert keineZahl = new Alert(Alert.AlertType.ERROR);
                keineZahl.setTitle("ungültige Eingabe");
                keineZahl.setHeaderText("Eine Menge muss eine Zahl sein");
                keineZahl.setContentText("Bitte Tragen Sie eine gültige Zahl ein.\n" +
                        "Dezimalbrüche(Kommazahlen) werden mit Punkt(.) angegeben. Beispiel 2.5\n" +
                        "Die Zutat wurde schon für Sie erstellt");
                keineZahl.showAndWait();
            }
        }
        updateList();
        textfelderEditierbar(false);
    }

    public void zutatLoeschen(ActionEvent actionEvent) throws IOException {
        Boolean zutatInNutzung = false;
        ArrayList<Rezeptkopf> rezepteMitZutat = new ArrayList<>();
        String rezeptAusgabe = "Rezepte mit der Zutat:\n";
        if (listZutaten.getSelectionModel().getSelectedItem() != null) {
            for (Rezeptkopf rez : rezeptkopfController.getAlleRezeptkopf()) {
                for (Rezeptzutat zutat : rez.getrKoRezeptzutat()) {
                    if (zutat.getrZuZutat().getZutName().equals(listZutaten.getSelectionModel().getSelectedItem().toString())) {
                        zutatInNutzung = true;
                        rezepteMitZutat.add(rez);
                        rezeptAusgabe += rez.getrKoRezeptname() + "\n";
                    }
                }
            }
            if (zutatInNutzung) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Zutat in Benutzung");
                alert.setHeaderText("Bevor eine Zutat gelösht werden kann muss sie bitte aus allen Rezepten entfernt werden");
                alert.setContentText(rezeptAusgabe);
                alert.showAndWait();
            } else {
                zutatenController.löschenZutat(listZutaten.getSelectionModel().getSelectedItem().toString());
                zutatenController.speichernDatei();
                textLoeschen();
                updateList();
            }
        }
    }

    private void textLoeschen() {
        textTitel.setText("");
        textMenge.setText("");
        textEinheit.setText("");
    }

    private void textfelderEditierbar(boolean zustand) {
        textTitel.setEditable(zustand);
        textMenge.setEditable(zustand);
        textEinheit.setEditable(zustand);
    }

    public void zurueckZuAktuellesRezept(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/RezeptAnsehen.fxml")));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Kochbuch: Rezeptansicht: " + UIController.uebertrag.getrKoRezeptname());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void kategorienAnzeigen(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/KategorienAnsehen.fxml")));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Kochbuch: Kategorien");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
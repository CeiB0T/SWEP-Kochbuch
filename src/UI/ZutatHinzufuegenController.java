package UI;

import Rezeptteile.Rezeptkopf;
import Rezeptteile.Rezeptzutat;
import Rezeptteile.Zubereitungsmethode;
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
        updateList();
       // zutatenController.speichenDatei(); -> Fehler wenn die Zutaten.json noch nicht existiert. Hilft das?
    }

    public void returnHome (ActionEvent actionEvent) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/HauptmenuV3.fxml")));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Kochbuch: Startseite");
        stage.show();
    }

    public void openDefinitionsbuch(ActionEvent actionEvent) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/DefinitionsbuchV2.fxml")));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Kochbuch: Definitionsbuch");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void addRezept(ActionEvent actionEvent) throws Exception {
        UIController.neuesRezept = true;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/RezeptAnsehen.fxml")));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
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
        for (Zutat zutat: zutatenController.getAlleZutaten()) {
            zutaten.add(zutat.getZutName());
        }
        sortierenListe(zutaten);
        listZutaten.setItems(zutaten);
        listZutaten.refresh();
    }

    private ObservableList sortierenListe(ObservableList<String> liste){
        liste.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        return liste;
    }

    public void listZutatenKlicked(MouseEvent mouseEvent) {
        if (!Objects.isNull(listZutaten.getSelectionModel().getSelectedItem())) {
            Rezeptkopf zugehoerigRezeptkopf = UIController.uebertrag;
            String zutat = listZutaten.getSelectionModel().getSelectedItem().toString();
            textTitel.setText(zutat.trim());
            for( int i = 0; i < zugehoerigRezeptkopf.getrKoRezeptzutat().size(); i++){
                Rezeptzutat rezeptzutat = zugehoerigRezeptkopf.getrKoRezeptzutat().get(i);
               String zutatname = rezeptzutat.getrZuZutat().getZutName();
               if(zutat.equals(zutatname)){
                   textMenge.setText(""+rezeptzutat.getrZuMenge());
                   textEinheit.setText(rezeptzutat.getrZuEinheit());
               }
            }
        }
    }

    public void zutatBearbeiten(ActionEvent actionEvent) {
        textfelderEditierbar(true);
    }

    public void zutatSpeichern(ActionEvent actionEvent) throws IOException {
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
                    zutatenController.speichenDatei();
                }
            }
            Rezeptkopf zugehoerigerRezeptkopf = UIController.uebertrag;
            if (textMenge.getText().matches("^\\d+\\.?\\d*$")) { //regex: ein oder mehr Zahlen, ein Punkt, null bis beliebig viele Zahlen, Ende
                if (zugehoerigerRezeptkopf.getrKoRezeptzutat().size() > 0) {
                    for (Rezeptzutat rezZutat: zugehoerigerRezeptkopf.getrKoRezeptzutat()) {
                        if (rezZutat.getrZuZutat().getZutName().equals(textTitel.getText().trim())){
                            rezZutat.setrZuEinheit(textEinheit.getText().trim());
                            rezZutat.setrZuMenge(Double.parseDouble(textMenge.getText().trim()));
                            textfelderEditierbar(false);
                            rezeptkopfController.speichernDatei();
                        }
                    }
                } else {
                    double menge = Double.parseDouble(textMenge.getText().trim());
                    Rezeptzutat neueRezeptzutat = new Rezeptzutat(menge, textEinheit.getText(), zutatenController.getZutat(textTitel.getText().trim()));
                    zugehoerigerRezeptkopf.zutatHinzufügen(neueRezeptzutat);
                    textfelderEditierbar(false);
                    rezeptkopfController.speichernDatei();
                }
            }else {
            Alert keineZahl = new Alert(Alert.AlertType.ERROR);
            keineZahl.setTitle("ungültige Eingabe");
            keineZahl.setHeaderText("Eine Menge muss eine Zahl sein");
            keineZahl.setContentText("Bitte Tragen Sie eine gültige Zahl ein. \nDezimalbrüche(Kommazahlen) werden mit Punkt(.) angegeben. \nBeispiel 2.5");
            keineZahl.showAndWait();
        }
        }
        updateList();
    }

    private void zulaessigeMenge(Rezeptkopf rezeptkopf, String text) throws IOException {
        if(textMenge.getText().matches("^\\d+\\.?\\d*$")){ //regex: ein oder mehr Zahlen, ein Punkt, null bis beliebig viele Zahlen, Ende
            System.out.println("ist kommazahl");
            double menge = Double.parseDouble(text);
            Rezeptzutat neueRezeptzutat = new Rezeptzutat(menge,textEinheit.getText(),zutatenController.getZutat(textTitel.getText().trim()));
            rezeptkopf.zutatHinzufügen(neueRezeptzutat);
            rezeptkopfController.speichernDatei();
            textfelderEditierbar(false);
        }else {
            Alert keineZahl = new Alert(Alert.AlertType.ERROR);
            keineZahl.setTitle("ungültige Eingabe");
            keineZahl.setHeaderText("Eine Menge muss eine Zahl sein");
            keineZahl.setContentText("Bitte Tragen Sie eine gültige Zahl ein. \nDezimalbrüche(Kommazahlen) werden mit Punkt(.) angegeben. \nBeispiel 2.5");
            keineZahl.showAndWait();
        }
    }

    public void zutatLoeschen(ActionEvent actionEvent) {
    }

    private void textLoeschen(){
        textTitel.setText("");
        textMenge.setText("");
        textEinheit.setText("");
    }

    private void textfelderEditierbar(boolean zustand){
        textTitel.setEditable(zustand);
        textMenge.setEditable(zustand);
        textEinheit.setEditable(zustand);
    }

    public void zurueckZuAktuellesRezept(ActionEvent actionEvent) {
    }
}

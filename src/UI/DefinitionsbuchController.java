package UI;

import Rezeptteile.Zubereitungsmethode;
import controller.ZubereitungsmethodeController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Comparator;
import java.util.Objects;

public class DefinitionsbuchController {

    public ListView listDefinitionen;
    public TextField textTitel;
    public TextArea textInhalt;

    public Button btnDefinitionHinzufuegen;
    public Button btnBearbeiten;
    public Button btnSpeichern;
    public Button btnLoeschen;
    public Button btnNeuesRezept;
    public Button btnReturnHome;
    public Button btnExit;

    private Stage stage;
    private Scene scene;

    ZubereitungsmethodeController zubController = ZubereitungsmethodeController.getInstance();

    public void definitionHinzufügen(ActionEvent actionEvent) { //TODO Definition: Test ob Felder editierbar uns leer sind
        textTitel.setText("");
        textTitel.setStyle("-fx-background-color: rgb(240, 255, 240)");
        textInhalt.setText("");
        textfelderEditierbar();
    }

    //TODO error fixen
    public void definitionBearbeiten(ActionEvent actionEvent) { //TODO Definition: Test ob Daten der ausgewählten Definition korrekt angezeigt werden
        Zubereitungsmethode zubBearbeiten = zubController.getZubereitungsmethodeByName(listDefinitionen.getSelectionModel().getSelectedItem().toString());
        if (zubBearbeiten != null) {
            textTitel.setText(zubBearbeiten.getzMeName());
            textInhalt.setText(zubBearbeiten.getzMeDefinition());
            textfelderEditierbar();
        }
    }

    public void definitionSpeichern(ActionEvent actionEvent) throws IOException { //TODO Definition: Test ob Daten korrekt in der Datei gespeichert + in Liste angezeigt werden
            if (!textTitel.getText().isEmpty() || !textTitel.getText().trim().isEmpty()) {
                if (zubController.existiertName(textTitel.getText())){ //Überschreiben von bestehender Zubereitungsmethode
                    Zubereitungsmethode zubAktualisieren = zubController.getZubereitungsmethodeByName(textTitel.getText());
                    zubAktualisieren.setzMeName(textTitel.getText());
                    zubAktualisieren.setzMeDefinition(textInhalt.getText());
                    zubController.speichenDatei();
                }else { //Speichern neuer Zubereitungsmethode
                    Zubereitungsmethode zubNeu = zubController.neueZubereitungsmethode(textTitel.getText());
                    zubNeu.setzMeDefinition(textInhalt.getText());
                    zubController.speichenDatei();
                }
                updateListe();
            }
        textfelderNichtEditierbar();
        textFeldreset();
    }

    public void definitionLoeschen(ActionEvent actionEvent) throws IOException {
        Zubereitungsmethode zubLoeschen = (Zubereitungsmethode) listDefinitionen.getSelectionModel().getSelectedItem();
        zubController.löschenZubereitungsmethode(zubLoeschen.getzMeID());
        zubController.speichenDatei();
        updateListe();
        textfelderNichtEditierbar();
        textFeldreset();
    }

    public void updateListe() throws IOException {
        ObservableList<String> definitionenListe = FXCollections.observableArrayList();
        zubController.leseDatei();
        for (Zubereitungsmethode zub: zubController.getAlleZubereitungsmethoden()) {
            definitionenListe.add(zub.getzMeName());
        }
        sortierenListe(definitionenListe);
        listDefinitionen.setItems(definitionenListe);
        listDefinitionen.refresh();
    }

    public ObservableList sortierenListe(ObservableList<String> liste){
        liste.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        return liste;
    }

    public void initialize() throws IOException {
        updateListe();
    }

    public void textfelderNichtEditierbar(){
            textTitel.setEditable(false);
            textInhalt.setEditable(false);
    }

    public void textfelderEditierbar(){
        textTitel.setEditable(true);
        textInhalt.setEditable(true);
    }

    public void textFeldreset(){
        textTitel.setStyle("-fx-background-color: rgb(255,255,255)");
        textInhalt.setStyle("-fx-background-color: white");
    }

    public void addRezept(ActionEvent actionEvent) {//TODO neues Rezept Maske öffnen
    }

    public void returnHome(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/HauptmenuV3.fxml")));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void programmSchliessen(ActionEvent actionEvent) {
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.close();
    }

    public void listDefinitionenKlicked(MouseEvent mouseEvent) {
        Zubereitungsmethode zub = zubController.getZubereitungsmethodeByName(listDefinitionen.getSelectionModel().getSelectedItem().toString());
        textTitel.setText(zub.getzMeName());
        textInhalt.setText(zub.getzMeDefinition());
    }
}

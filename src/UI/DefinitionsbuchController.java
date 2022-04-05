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
import javafx.scene.control.*;
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

    private Boolean neueDefinition = false;

    ZubereitungsmethodeController zubController = ZubereitungsmethodeController.getInstance();

    public void definitionHinzufügen(ActionEvent actionEvent) {
        textLoeschen();
        textTitel.setStyle("-fx-background-color: rgb(240, 255, 240)");
        neueDefinition = true;
        textfelderEditierbar();
    }

    public void definitionBearbeiten(ActionEvent actionEvent) {
        neueDefinition = false;
        Zubereitungsmethode zubBearbeiten = zubController.getZubereitungsmethodeByName(listDefinitionen.getSelectionModel().getSelectedItem().toString());
        if (zubBearbeiten != null) {
            textfelderEditierbar();
        }
    }

    public void definitionSpeichern(ActionEvent actionEvent) throws IOException {
            if (textTitel.getText().matches(".*\\S+.*")) {//Regex: Enthält mindestens ein nicht Leerzeichen
                System.out.println("RegEx wurde akzeptiert");
                if (zubController.existiertName(textTitel.getText())){//Überschreiben von bestehender Zubereitungsmethode
                    if (neueDefinition){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Ein Problem ist aufgetreten");
                        alert.setHeaderText("Definition bereits vorhanden");
                        alert.setContentText("Diese Definition existiert bereits. Falls Sie den Inhalt ändern wollen wählen Sie Diese bitte aus und klicken Sie Bearbeiten");
                        alert.showAndWait();
                    }else {
                        Zubereitungsmethode zubAktualisieren = zubController.getZubereitungsmethodeByName(textTitel.getText());
                        zubAktualisieren.setzMeName(textTitel.getText().trim());
                        zubAktualisieren.setzMeDefinition(textInhalt.getText().trim());
                        zubController.speichenDatei();
                    }
                }else { //Speichern neuer Zubereitungsmethode
                    Zubereitungsmethode zubNeu = zubController.neueZubereitungsmethode(textTitel.getText().trim());
                    zubNeu.setzMeDefinition(textInhalt.getText().trim());
                    zubController.speichenDatei();
                }
                updateListe();
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ein Fehler ist aufgetreten");
                alert.setHeaderText("Dieser Definitionsname ist nicht zulässig!");
                alert.setContentText("Eine Definition darf nicht nur aus Leerzeichen bestehen");
                alert.showAndWait();
            }
        textfelderNichtEditierbar();
        textFeldreset();
        neueDefinition = false;
    }

    public void definitionLoeschen(ActionEvent actionEvent) throws IOException {
        Zubereitungsmethode zubLoeschen = zubController.getZubereitungsmethodeByName(textTitel.getText());
        zubController.löschenZubereitungsmethode(zubLoeschen.getzMeID());
        zubController.speichenDatei();
        updateListe();
        textfelderNichtEditierbar();
        textFeldreset();
        textLoeschen();
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
        textTitel.setStyle("-fx-background-color: white");
        textInhalt.setStyle("-fx-background-color: white");
    }

    public void addRezept(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/RezeptAnsehen.fxml")));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Kochbuch: Rezeptansicht");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void returnHome(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/HauptmenuV3.fxml")));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Kochbuch: Startseite");
        stage.show();
    }

    public void programmSchliessen(ActionEvent actionEvent) {
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.close();
    }

    public void listDefinitionenKlicked(MouseEvent mouseEvent) {
        textfelderNichtEditierbar();
        Zubereitungsmethode zub = zubController.getZubereitungsmethodeByName(listDefinitionen.getSelectionModel().getSelectedItem().toString());
        textTitel.setText(zub.getzMeName());
        textInhalt.setText(zub.getzMeDefinition());
    }

    public void textLoeschen(){
        textTitel.setText("");
        textInhalt.setText("");
    }
}
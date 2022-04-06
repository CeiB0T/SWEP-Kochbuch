package UI;

import Rezeptteile.Rezeptkopf;
import Rezeptteile.Rezeptzutat;
import Rezeptteile.Zutat;
import controller.RezeptkopfController;
import controller.ZutatenController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
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

    private Stage stage;
    private Scene scene;
    private boolean neueZutat = false;

    ZutatenController zutatenController = ZutatenController.getInstance();
    RezeptkopfController rezeptkopfController = RezeptkopfController.getInstance();


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
        stage.setResizable(false);//TODO überall machen
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

    public void listZutatenKlicked(MouseEvent mouseEvent) {
        if (!Objects.isNull(listZutaten.getSelectionModel().getSelectedItem())) {
            

        }

    }

    public void zutatBearbeiten(ActionEvent actionEvent) {
    }

    public void zutatSpeichern(ActionEvent actionEvent) throws IOException {
        if (textTitel.getText().matches(".*\\S+.*")) {//Regex: Enthält mindestens ein nicht Leerzeichen
         if(neueZutat){
            if(zutatenController.existiertZutat(textTitel.getText())){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Ein Problem ist aufgetreten");
                alert.setHeaderText("Zutat bereits vorhanden");
                alert.setContentText("Diese Zutat existiert bereits. Falls Sie den Inhalt ändern wollen wählen Sie Diese bitte aus und klicken Sie Bearbeiten");
                alert.showAndWait();
            }else{
                Zutat neueZutat = zutatenController.neueZutat(textTitel.getText().trim());
                zutatenController.speichenDatei();
            }
            }
         Rezeptkopf zugehoerigerRezeptkopf = UIController.uebertrag;
         if(textMenge.getText().matches("\\d+\\.?\\d*$")){ //regex: ein oder mehr Zahlen, ein Punkt, null bis beliebig viele Zahlen, Ende
             double menge = Double.valueOf(textMenge.getText());
             Rezeptzutat neueRezeptzutat = new Rezeptzutat(menge,textEinheit.getText(),zutatenController.getZutat(textTitel.getText().trim()));
             zugehoerigerRezeptkopf.zutatHinzufügen(neueRezeptzutat);
             rezeptkopfController.speichernDatei();
         }else {
             Alert keineZahl = new Alert(Alert.AlertType.ERROR);
             keineZahl.setTitle("ungültige Eingabe");
             keineZahl.setHeaderText("Eine Menge muss eine Zahl sein");
             keineZahl.setContentText("Bitte Tragen Sie eine gültige Zahl ein. Dezimalbrüche(Kommazahlen) werden mit Punkt(.) angegeben. Beispiel 2.5");
         }


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
}
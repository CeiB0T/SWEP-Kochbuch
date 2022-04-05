package UI;

import Rezeptteile.Rezeptkopf;
import Rezeptteile.Rezeptzutat;
import Rezeptteile.Zutat;
import controller.RezeptkopfController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import qrcodegen.QrBufferedImage;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class RezeptDetailsController {
    
    public Button btnDefinitionsbuch;
    public Button btnReturnHome;
    public Button btnExit;
    public Button btnBearbeiten;
    public Button btnSpeichern;
    public Button btnLoeschen;

    public ImageView imgQR;
    public TextArea textRezeptName;
    public TextArea textZutaten;
    public TextArea textZubereitung;
    public ListView listZutaten;

    private Stage stage;
    private Scene scene;

    RezeptkopfController rezeptkopfController = RezeptkopfController.getInstance();

    public ObservableList zutatenAuflisten(Rezeptkopf rezeptkopf){
        ObservableList ret = FXCollections.observableArrayList();
        if (!rezeptkopf.getrKoRezeptzutat().isEmpty() || rezeptkopf.getrKoRezeptzutat() == null) {
            for (Rezeptzutat zut : rezeptkopf.getrKoRezeptzutat()) {
                ret.add(zut.listViewString());
            }
            return ret;
        }
        return null;
    }

    public void initialize() throws IOException {
        if (UIController.neuesRezept){ //Nur ausführen wenn ein neues Rezept erstellt wird
            textfelderEditierbar();
        }else { //Ausführen wenn ein bestehendes Rezept angezeigt wird
            try {
                Rezeptkopf rezeptkopf = UIController.uebertrag;
                textRezeptName.setText(rezeptkopf.getrKoRezeptname());
                listZutaten.setItems(zutatenAuflisten(rezeptkopf));
                textZubereitung.setText(rezeptkopf.getrKoRezeptinhalt());
            }catch (Exception e){}
        }
    }

    public void openDefinition(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/DefinitionsbuchV2.fxml")));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Kochbuch: Startseite");
        stage.setScene(scene);
        stage.setResizable(false);//TODO überall machen
        stage.show();
    }

    public void returnHome(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/HauptmenuV3.fxml")));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Kochbuch: Startseite");
        stage.setScene(scene);
        stage.setResizable(false);//TODO überall machen
        stage.show();
    }

    public void programmSchließen(ActionEvent actionEvent) {
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.close();
    }

    public void rezeptBearbeiten(ActionEvent actionEvent) {
        if(textRezeptName.getText().matches(".*\\S+.*")) {
            UIController.uebertrag.setrKoRezeptname(textRezeptName.getText().trim());
            //TODO Zutaten bearbeitung einfügen basierend auf ZutatenListe selection Model
            UIController.uebertrag.setrKoRezeptinhalt(textZubereitung.getText().trim());
        }
        UIController.uebertrag = null;
    }

    public void rezeptSpeichern(ActionEvent actionEvent) throws IOException {
        if (UIController.neuesRezept){ //TODO neues Rezept speichern
            if (textRezeptName.getText().matches(".*\\S+.*")){
                Rezeptkopf rez = rezeptkopfController.neuerRezeptkopf(textRezeptName.getText().trim());
                rez.setrKoRezeptinhalt(textZubereitung.getText().trim());
                rezeptkopfController.speichernDatei();
            }else {
                alertNameUnzulässig();
            }
        }else { //Bestehendes Rezept wird gespeichert
            Rezeptkopf rez = UIController.uebertrag;
            if (textRezeptName.getText().matches(".*\\S+.*")){
                rez.setrKoRezeptname(textRezeptName.getText().trim());
                rez.setrKoRezeptinhalt(textZubereitung.getText().trim());
                rezeptkopfController.speichernDatei();
            }else {
                alertNameUnzulässig();
            }
        }
        UIController.neuesRezept = false; //Neues Rezept Bool zurücksetzen
    }

    public void alertNameUnzulässig(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ein Fehler ist aufgetreten");
        alert.setHeaderText("Dieser Rezeptname ist nicht zulässig!");
        alert.setContentText("Ein Rezeptname darf nicht nur aus Leerzeichen bestehen");
        alert.showAndWait();
    }

    public void rezeptLöschen(ActionEvent actionEvent) {
        //TODO alle Rezeptzutaten löschen und die Rezeptzutaten datei speichern. Dann den Rezeptkopf löschen.
    }

    public void qrAnzeigen() throws IOException {
        BufferedImage bufferedImage = QrBufferedImage.qrLinkGenerieren("https://www.youtube.com/watch?v=o-YBDTqX_ZU");
        imgQR.setImage(SwingFXUtils.toFXImage(bufferedImage,null));
    }

    public void textfelderEditierbar(){
        textRezeptName.setEditable(true);
        textZubereitung.setEditable(true);
    }

    public void textfelderNichtEditierbar(){
        textRezeptName.setEditable(false);
        textZubereitung.setEditable(false);
    }
}

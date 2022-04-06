package UI;

import Rezeptteile.Rezeptkopf;
import Rezeptteile.Rezeptzutat;
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
import javafx.scene.input.MouseEvent;
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
    public TextArea textZubereitung;
    public  TextArea textPersonenanzahl;
    public ListView listZutaten;
    public Button btnNeueZutat;
    public boolean bearbeitung;

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
                if(rezeptkopf.getrKoPersonenzahl() > 0) {
                    textPersonenanzahl.setText("Für " + Integer.toString(rezeptkopf.getrKoPersonenzahl()) + " Personen");
                }else{
                    textPersonenanzahl.setText("Personenanzahl: Keine Angabe");
                }
            }catch (Exception e){}
        }
    }

    public void openDefinition(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/DefinitionsbuchV2.fxml")));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Kochbuch: Startseite");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void returnHome(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/HauptmenuV3.fxml")));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Kochbuch: Startseite");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void programmSchließen(ActionEvent actionEvent) {
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.close();
    }

    public void rezeptBearbeiten(ActionEvent actionEvent) {
        if(textRezeptName.getText().matches(".*\\S+.*")) {
            bearbeitung = true;
            textfelderEditierbar();
            UIController.uebertrag.setrKoRezeptname(textRezeptName.getText().trim());
            //TODO Zutaten bearbeitung einfügen basierend auf ZutatenListe selection Model
            UIController.uebertrag.setrKoRezeptinhalt(textZubereitung.getText().trim());
        }
        UIController.uebertrag = null;
        textfelderNichtEditierbar();
        bearbeitung = false;
    }

    public void rezeptSpeichern(ActionEvent actionEvent) throws IOException {
        if (UIController.neuesRezept){ //TODO neues Rezept speichern
            if (textRezeptName.getText().matches(".*\\S+.*")){
                Rezeptkopf rez = rezeptkopfController.neuerRezeptkopf(textRezeptName.getText().trim());
                rez.setrKoRezeptinhalt(textZubereitung.getText().trim());
                if (textPersonenanzahl.getText().matches("\\d+")) {
                    rez.setrKoPersonenzahl(Integer.parseInt(textPersonenanzahl.getText()));
                }
                rezeptkopfController.speichernDatei();
            }else {
                alertNameUnzulässig();
            }
        }else { //Bestehendes Rezept wird gespeichert
            Rezeptkopf rez = UIController.uebertrag;
            if (textRezeptName.getText().matches(".*\\S+.*")){
                rez.setrKoRezeptname(textRezeptName.getText().trim());
                rez.setrKoRezeptinhalt(textZubereitung.getText().trim());
                if (textPersonenanzahl.getText().matches("\\d+")) {
                    rez.setrKoPersonenzahl(Integer.parseInt(textPersonenanzahl.getText()));
                }
                rezeptkopfController.speichernDatei();
            }else {
                alertNameUnzulässig();
            }
        }
        textfelderNichtEditierbar();
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
        //TODO Rezeptkopf löschen.
    }

    public void qrAnzeigen() throws IOException {
        BufferedImage bufferedImage = QrBufferedImage.qrLinkGenerieren("https://www.youtube.com/watch?v=o-YBDTqX_ZU");
        imgQR.setImage(SwingFXUtils.toFXImage(bufferedImage,null));
    }

    public void textfelderEditierbar(){
        textRezeptName.setEditable(true);
        textZubereitung.setEditable(true);
        textPersonenanzahl.setEditable(true);
    }

    public void textfelderNichtEditierbar(){
        textRezeptName.setEditable(false);
        textZubereitung.setEditable(false);
        textPersonenanzahl.setEditable(true);
    }

    public void openNeueZutat(ActionEvent actionEvent) {
    }

    public void zutatBearbeiten(MouseEvent mouseEvent) { //TODO Zutaten Fenster öffnen
        if (mouseEvent.getClickCount() == 2){

        }
    }
}

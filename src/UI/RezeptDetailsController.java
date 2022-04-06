package UI;

import Rezeptteile.Rezeptkopf;
import Rezeptteile.Rezeptzutat;
import Rezeptteile.Zutat;
import controller.RezeptkopfController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    public TextField textPersonenanzahl;
    public ListView listZutaten;
    public Button btnNeueZutat;
    public boolean bearbeitung;

    private Stage stage;
    private Scene scene;

    public static Rezeptzutat rezeptzutatUebertrag;
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
                    textPersonenanzahl.setText(Integer.toString(rezeptkopf.getrKoPersonenzahl()));
                }
            }catch (Exception e){}
        }
    }

    public void openDefinition(ActionEvent actionEvent) throws IOException {
        UIController.neuesRezept = false;
        bearbeitung = false;
        textfelderEditierbar();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/DefinitionsbuchV2.fxml")));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Kochbuch: Startseite");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void returnHome(ActionEvent actionEvent) throws IOException {
        bearbeitung = false;
        textfelderNichtEditierbar();
        startseiteAufrufen(actionEvent);
    }

    public void startseiteAufrufen(Event event) throws IOException {
        UIController.neuesRezept = false;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/HauptmenuV3.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
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
        if (!UIController.neuesRezept) {
            if (textRezeptName.getText().matches(".*\\S+.*")) {
                bearbeitung = true;
                textfelderEditierbar();
                UIController.uebertrag.setrKoRezeptname(textRezeptName.getText().trim());
                UIController.uebertrag.setrKoRezeptinhalt(textZubereitung.getText().trim());
            }
            UIController.uebertrag = null;
            textfelderNichtEditierbar();
            bearbeitung = false;
        }
    }

    public void rezeptSpeichern(ActionEvent actionEvent) throws IOException {
        if (UIController.neuesRezept){
            if (textRezeptName.getText().matches(".*\\S+.*")){
                Rezeptkopf rez = rezeptkopfController.neuerRezeptkopf(textRezeptName.getText().trim());
                rez.setrKoRezeptinhalt(textZubereitung.getText().trim());
                if (textPersonenanzahl.getText().matches("\\d+")) {
                    rez.setrKoPersonenzahl(Integer.parseInt(textPersonenanzahl.getText()));
                }
                UIController.neuesRezept = false;
                rezeptkopfController.speichernDatei();
                textfelderNichtEditierbar();
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
                textfelderNichtEditierbar();
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

    public void rezeptLöschen(ActionEvent actionEvent) throws IOException { //TODO check ob funktioniert
        if (UIController.uebertrag != null) {
            rezeptkopfController.löschenRezeptkopf(UIController.uebertrag.getrKoID());
            bearbeitung = false;
            textfelderNichtEditierbar();
            rezeptkopfController.speichernDatei();
            startseiteAufrufen(actionEvent);
        }else if (rezeptkopfController.getRezeptkopfByName(textRezeptName.getText()) != null){
            rezeptkopfController.löschenRezeptkopf(rezeptkopfController.getRezeptkopfByName(textRezeptName.getText()).getrKoID());
            bearbeitung = false;
            textfelderNichtEditierbar();
            rezeptkopfController.speichernDatei();
            startseiteAufrufen(actionEvent);
        }
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
        textPersonenanzahl.setEditable(false);
    }

    public void openNeueZutat(ActionEvent actionEvent) throws IOException {
        neueZutatFenster(actionEvent, null);
    }

    public void zutatBearbeiten(MouseEvent mouseEvent) throws IOException { //TODO Zutaten Fenster öffnen
        if (mouseEvent.getClickCount() == 2){
            if (listZutaten.getSelectionModel().getSelectedItem() != null) {
                String[] zutatWerte = listZutaten.getSelectionModel().getSelectedItem().toString().split(":");
                for (Rezeptzutat rezeptzutat: UIController.uebertrag.getrKoRezeptzutat()) {
                    if (rezeptzutat.getrZuZutat().getZutName() == zutatWerte[0]){
                        neueZutatFenster(mouseEvent, rezeptzutat); break;
                    }
                }
            }
        }
    }

    public void neueZutatFenster(Event event, Rezeptzutat zutat) throws IOException {
        UIController.neuesRezept = false;
        textfelderNichtEditierbar();
        bearbeitung = false;
        textfelderNichtEditierbar();
        rezeptzutatUebertrag = zutat;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/ZutatHinzufuegen.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Kochbuch: Startseite");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}

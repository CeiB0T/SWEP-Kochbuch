package UI;

import Rezeptteile.Rezeptkopf;
import Rezeptteile.Rezeptzutat;
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
import java.util.Optional;

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

    private ObservableList zutatenAuflisten(Rezeptkopf rezeptkopf) {
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
        if (UIController.neuesRezept) { //Nur ausführen wenn ein neues Rezept erstellt wird
            textfelderEditierbar(true);
        } else { //Ausführen wenn ein bestehendes Rezept angezeigt wird
            try {
                Rezeptkopf rezeptkopf = UIController.uebertrag;
                textRezeptName.setText(rezeptkopf.getrKoRezeptname());
                listZutaten.setItems(zutatenAuflisten(rezeptkopf));
                textZubereitung.setText(rezeptkopf.getrKoRezeptinhalt());
                if (rezeptkopf.getrKoPersonenzahl() > 0) {
                    textPersonenanzahl.setText(Integer.toString(rezeptkopf.getrKoPersonenzahl()));
                }
                Tooltip reZutatWeg = new Tooltip("Zutat zum löschen makieren(anklicken), dann löschen drücken");
                listZutaten.setTooltip(reZutatWeg);
                qrAnzeigen();
            } catch (Exception e) {
            }
        }
    }

    public void openDefinition(ActionEvent actionEvent) throws IOException {
        UIController.neuesRezept = false;
        bearbeitung = false;
        textfelderEditierbar(true);
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/DefinitionsbuchV2.fxml")));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Kochbuch: Startseite");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void returnHome(ActionEvent actionEvent) throws IOException {
        bearbeitung = false;
        textfelderEditierbar(false);
        startseiteAufrufen(actionEvent);
    }

    private void startseiteAufrufen(Event event) throws IOException {
        UIController.neuesRezept = false;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/HauptmenuV3.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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

    private void buttonsDeaktivierenBeiBearbeitung(Boolean bool) {
        btnNeueZutat.setDisable(bool);
        btnBearbeiten.setDisable(bool);
        btnDefinitionsbuch.setDisable(bool);
        btnLoeschen.setDisable(bool);
        btnReturnHome.setDisable(bool);
    }

    public void rezeptBearbeiten(ActionEvent actionEvent) {
        if (!UIController.neuesRezept) {
            if (textRezeptName.getText().matches(".*\\S+.*")) {
                bearbeitung = true;
                textfelderEditierbar(true);
                buttonsDeaktivierenBeiBearbeitung(true);
            }
        }
    }

    public void rezeptSpeichern(ActionEvent actionEvent) throws IOException {
        if (UIController.neuesRezept) {
            if (textRezeptName.getText().matches(".*\\S+.*")) {
                Rezeptkopf rez = rezeptkopfController.neuerRezeptkopf(textRezeptName.getText().trim());
                rez.setrKoRezeptinhalt(textZubereitung.getText().trim());
                if (textPersonenanzahl.getText().matches("\\d+")) {
                    rez.setrKoPersonenzahl(Integer.parseInt(textPersonenanzahl.getText()));
                }
                UIController.neuesRezept = false;
                UIController.uebertrag = rez;
                rezeptkopfController.speichernDatei();
                textfelderEditierbar(false);
                buttonsDeaktivierenBeiBearbeitung(false);
            } else {
                alertNameUnzulässig();
            }
        } else { //Bestehendes Rezept wird gespeichert
            Rezeptkopf rez = UIController.uebertrag;
            if (textRezeptName.getText().matches(".*\\S+.*")) {
                rez.setrKoRezeptname(textRezeptName.getText().trim());
                rez.setrKoRezeptinhalt(textZubereitung.getText().trim());
                if (textPersonenanzahl.getText().matches("\\d+")) {
                    rez.setrKoPersonenzahl(Integer.parseInt(textPersonenanzahl.getText()));
                }
                rezeptkopfController.speichernDatei();
                textfelderEditierbar(false);
                buttonsDeaktivierenBeiBearbeitung(false);
            } else {
                alertNameUnzulässig();
            }
        }
        qrAnzeigen();
        UIController.neuesRezept = false; //Neues Rezept Bool zurücksetzen
    }

    private void alertNameUnzulässig() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ein Fehler ist aufgetreten");
        alert.setHeaderText("Dieser Rezeptname ist nicht zulässig!");
        alert.setContentText("Ein Rezeptname darf nicht nur aus Leerzeichen bestehen");
        alert.showAndWait();
    }

    public void rezeptLöschen(ActionEvent actionEvent) throws IOException {
        if (listZutaten.getSelectionModel().getSelectedItem() == null) {
            Alert wirklichLoeschen = new Alert(Alert.AlertType.CONFIRMATION);
            wirklichLoeschen.setTitle("Löschen bestätigen");
            wirklichLoeschen.setHeaderText("Wollen Sie das Rezept wirklich löschen");
            wirklichLoeschen.setContentText("Wenn Sie nur eine Zutat löschen möchten:\nMakieren Sie die Zutat in der Liste(anklicken),\ndrücken Sie dann löschen.");

            Optional<ButtonType> result = wirklichLoeschen.showAndWait();
            if (result.get() == ButtonType.OK) {

                if (UIController.uebertrag != null) {
                    rezeptkopfController.löschenRezeptkopf(UIController.uebertrag.getrKoID());
                    bearbeitung = false;
                    textfelderEditierbar(false);
                    rezeptkopfController.speichernDatei();
                    startseiteAufrufen(actionEvent);
                } else if (rezeptkopfController.getRezeptkopfByName(textRezeptName.getText()) != null) {
                    rezeptkopfController.löschenRezeptkopf(rezeptkopfController.getRezeptkopfByName(textRezeptName.getText()).getrKoID());
                    bearbeitung = false;
                    textfelderEditierbar(false);
                    rezeptkopfController.speichernDatei();
                    startseiteAufrufen(actionEvent);
                }
            }
        } else {
            if (textRezeptName.getText().matches(".*\\S+.*")) {
                Rezeptkopf rez = rezeptkopfController.getRezeptkopfByName(textRezeptName.getText().trim());
                String[] strings = listZutaten.getSelectionModel().getSelectedItem().toString().split(":");
                rez.zutatLöschen(strings[0]);
                rezeptkopfController.speichernDatei();
                listZutaten.setItems(zutatenAuflisten(rez));
            }
        }
    }

    private void qrAnzeigen() throws IOException {
        BufferedImage anzeige = null;
        if (UIController.uebertrag != null) {
            anzeige = QrBufferedImage.qrGenerieren(UIController.uebertrag);
        } else {
            anzeige = QrBufferedImage.qrGenerieren(rezeptkopfController.getRezeptkopfByName(textRezeptName.getText()));
        }
        if (anzeige != null) {
            imgQR.setImage(SwingFXUtils.toFXImage(anzeige, null));
        }
    }

    public void textfelderEditierbar(Boolean bool) {
        textRezeptName.setEditable(bool);
        textZubereitung.setEditable(bool);
        textPersonenanzahl.setEditable(bool);
    }

    public void openNeueZutat(ActionEvent actionEvent) throws IOException {
        if (UIController.uebertrag != null) {
            neueZutatFenster(actionEvent, null);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ein Problem ist aufgetreten");
            alert.setHeaderText("Ein Rezept muss erstellt werden");
            alert.setContentText("Bevor eine Zutat hinzugefügt werden kann, muss ein neues Rezept über den Speichern Button erstellt werden.");
            alert.showAndWait();
        }
    }

    public void zutatBearbeiten(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getClickCount() == 2) {
            if (listZutaten.getSelectionModel().getSelectedItem() != null) {
                String[] zutatWerte = listZutaten.getSelectionModel().getSelectedItem().toString().split(":");
                for (Rezeptzutat rezeptzutat : UIController.uebertrag.getrKoRezeptzutat()) {
                    if (rezeptzutat.getrZuZutat().getZutName().equals(zutatWerte[0])) {
                        neueZutatFenster(mouseEvent, rezeptzutat);
                        break;
                    }
                }
            }
        }
    }

    public void neueZutatFenster(Event event, Rezeptzutat zutat) throws IOException {
        UIController.neuesRezept = false;
        textfelderEditierbar(false);
        bearbeitung = false;
        textfelderEditierbar(false);
        rezeptzutatUebertrag = zutat;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/ZutatHinzufuegen.fxml")));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Kochbuch: Startseite");
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
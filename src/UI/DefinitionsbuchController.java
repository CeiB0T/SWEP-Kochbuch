package UI;

import Rezeptteile.Zubereitungsmethode;
import controller.ZubereitungsmethodeController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;

public class DefinitionsbuchController {

    public ListView listDefinitionen;
    public TextField textTitel;
    public TextArea textInhalt;

    public Button btnDefinitionHinzufuegen;
    public Button btnBearbeiten;
    public Button btnSpeichern;
    public Button btnLoeschen;

    ZubereitungsmethodeController zubController = ZubereitungsmethodeController.getInstance();

    public void definitionHinzufügen(ActionEvent actionEvent) { //TODO Definition: Test ob Felder editierbar uns leer sind
        textTitel.setText("");
        textInhalt.setText("");
        textfelderEditierbar();
    }

    //TODO error fixen
    public void definitionBearbeiten(ActionEvent actionEvent) { //TODO Definition: Test ob Daten der ausgewählten Definition korrekt angezeigt werden
        Zubereitungsmethode zubBearbeiten = (Zubereitungsmethode) listDefinitionen.getSelectionModel().getSelectedItem();
        if (!zubBearbeiten.equals(null)) {
            textTitel.setText(zubBearbeiten.getzMeName());
            textInhalt.setText(zubBearbeiten.getzMeDefinition());
            textfelderEditierbar();
        }
    }

    public void definitionSpeichern(ActionEvent actionEvent) throws IOException { //TODO Definition: Test ob Daten korrekt in der Datei gespeichert + in Liste angezeigt werden
        if (!textTitel.getText().isEmpty() || !textTitel.getText().equals(" ")){
            Zubereitungsmethode zub = zubController.neueZubereitungsmethode(textTitel.getText());
            zub.setzMeDefinition(textInhalt.getText());
            zubController.speichenDatei();
            updateListe();
        }
        textfelderNichtEditierbar();
    }

    //TODO error fixen
    public void definitionLoeschen(ActionEvent actionEvent) throws IOException { //TODO Definition: Test ob Daten gelöscht + aus der Liste entfernt werden
        Zubereitungsmethode zubLoeschen = (Zubereitungsmethode) listDefinitionen.getSelectionModel().getSelectedItem();
        zubController.löschenZubereitungsmethode(zubLoeschen.getzMeID());
        zubController.speichenDatei();
        updateListe();
        textfelderNichtEditierbar();
    }

    public void updateListe() throws IOException {
        ObservableList<String> definitionenListe = FXCollections.observableArrayList();
        zubController.leseDatei();
        for (Zubereitungsmethode zub: zubController.getAlleZubereitungsmethoden()) {
            definitionenListe.add(zub.getzMeName()); //TODO Wieso funzt getName() nicht
            System.out.println(zub.getzMeName());
        }
        listDefinitionen.setItems(definitionenListe);
        listDefinitionen.refresh();
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
}

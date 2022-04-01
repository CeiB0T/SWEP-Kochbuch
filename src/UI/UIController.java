package UI;

import Rezeptteile.Kategorie;
import Rezeptteile.Rezeptkopf;
import Rezeptteile.Rezeptzutat;
import controller.KategorieController;
import controller.RezeptkopfController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class UIController{

    public Button btnStartNeuRezept;
    public Button btnStartDefinitionsbuch;
    public Button btnStartProgrammBeenden;
    public Button btnStartUpdate;

    public TextField textStartSuche;
    public ListView listStartSuche;
    public ListView listStartRezepte;

    RezeptkopfController rezeptkopfController = RezeptkopfController.getInstance();

    public void updateListe(ActionEvent actionEvent) throws IOException {
        ObservableList<String> rezepteListe = FXCollections.observableArrayList();
        rezeptkopfController.leseDatei();
        for (Rezeptkopf rez: rezeptkopfController.getAlleRezeptkopf()) {
                rezepteListe.add(rez.listViewString());

        }

        listStartRezepte.setItems(rezepteListe);
        listStartRezepte.refresh();
    }

    public void neuesRezeptFenster(ActionEvent actionEvent) throws IOException {
        //TODO neue FXML für "neues Rezept" einbinden und Starten
    }

    public void definitionsbuchOeffnen(ActionEvent actionEvent) {
        //TODO neue FXML für "Definitionsbuch" einbinden und Starten
    }

    public void programmBeenden(ActionEvent actionEvent) {
        Stage stage = (Stage) btnStartProgrammBeenden.getScene().getWindow();
        stage.close();
    }

    public String zutatPrefix(Rezeptkopf rez){
        return "Zutat: " + rez.listViewString();
    }

    public String kategoriePrefix(Rezeptkopf rez){
        return "Kategorie: " + rez.listViewString();
    }

    public void sucheStarten(KeyEvent keyEvent) {
        if ((keyEvent.getCharacter().charAt(0) >= 'A' && keyEvent.getCharacter().charAt(0) <= 'z') ||
                (keyEvent.getCharacter().charAt(0) >= '0' && keyEvent.getCharacter().charAt(0) <= '9')) {
                    elementeInSucheAnzeigen();
        }
    }

    public void sucheBackspace(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.BACK_SPACE) && textStartSuche.getText().length() >= 1) {
            elementeInSucheAnzeigen();
        }
        if (keyEvent.getCode().equals(KeyCode.BACK_SPACE) && textStartSuche.getText().length() < 1){
            listStartSuche.getItems().clear();
        }
    }

    public void elementeInSucheAnzeigen(){
        listStartSuche.getItems().clear();

        ObservableList<String> gefundenListe = FXCollections.observableArrayList();
        String suchtext = textStartSuche.getText().toLowerCase();

            for (Kategorie kat : KategorieController.getInstance().getAlleKategorien()) { //Iteriert durch alle Kategorien auf der Suche nach zugehörigen gesuchten Rezepten
                if (kat.getKatName().toLowerCase().lastIndexOf(suchtext) != -1) {
                    for (Rezeptkopf rez : kat.getKatRezeptkopf()) {
                        gefundenListe.add(kategoriePrefix(rez));
                    }
                }
            }

            for (Rezeptkopf rez : rezeptkopfController.getAlleRezeptkopf()) { //Iteriert durch alle Rezeptköpfe für die Suche nach Zutaten übereinstimmungen
                for (Rezeptzutat zut : rez.getrKoRezeptzutat()) { //Iteriert durch alle Zutaten eines Rezepts
                    if (zut.getrZuZutat().getZutName().toLowerCase().lastIndexOf(suchtext) != -1) { //Prüft ob der Name der Zutat dem der suche gleicht
                        gefundenListe.add(zutatPrefix(rez));
                    }
                }
            }

            for (Rezeptkopf rez : rezeptkopfController.getAlleRezeptkopf()) { //Gleiche Iteration wie erste foreach, aber hier wird nur nach Rezeptnamen gesucht
                if (rez.getrKoRezeptname().toLowerCase().lastIndexOf(suchtext) != -1) { //Prüft ob Rezeptname dem Suchwort entspricht
                    System.out.println(suchtext);
                    gefundenListe.add(rez.listViewString());
                }
            }
                listStartSuche.setItems(gefundenListe);
        }
    }

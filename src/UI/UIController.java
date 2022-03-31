package UI;

import Rezeptteile.Kategorie;
import Rezeptteile.Rezeptkopf;
import Rezeptteile.Rezeptzutat;
import controller.KategorieController;
import controller.RezeptkopfController;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;

public class UIController {

    public Button btnStartNeuRezept;
    public Button btnStartDefinitionsbuch;
    public Button btnStartProgrammBeenden;
    public Button btnStartUpdate;

    public TextField textStartSuche;
    public ListView listStartSuche;
    public ListView listStartRezepte;


    public TableView tableStartRezepte;
    public TableColumn colName;
    //public TableColumn colZutaten;
    //public TableColumn colPersonen;
    //public TableColumn colBeschreibung;

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

        //if (actionEvent.equals(KeyEvent.VK_ENTER)){
            String suchtext = textStartSuche.getText();
            ObservableList<String> gefundenListe = FXCollections.observableArrayList();

            for (Kategorie kat: KategorieController.getInstance().getAlleKategorien()){ //Iteriert durch alle Kategorien auf der Suche nach zugehörigen gesuchten Rezepten
                if (kat.getKatName().equals(suchtext)){
                    for (Rezeptkopf rez: kat.getKatRezeptkopf()){
                        gefundenListe.add(kategoriePrefix(rez));
                    }
                }
            }

            for (Rezeptkopf rez: rezeptkopfController.getAlleRezeptkopf()) { //Iteriert durch alle Rezeptköpfe für die Suche nach Zutaten übereinstimmungen
                for (Rezeptzutat zut: rez.getrKoRezeptzutat()) { //Iteriert durch alle Zutaten eines Rezepts
                    if (zut.getrZuZutat().getZutName().equals(suchtext)){ //Prüft ob der Name der Zutat dem der suche gleicht
                        gefundenListe.add(zutatPrefix(rez));
                    }
                }
            }

            for (Rezeptkopf rez: rezeptkopfController.getAlleRezeptkopf()){ //Gleiche Iteration wie erste foreach, aber hier wird nur nach Rezeptnamen gesucht
                if (rez.getrKoRezeptname().equals(suchtext)){ //Prüft ob Rezeptname dem Suchwort entspricht
                    gefundenListe.add(rez.listViewString());
                }
            }
            listStartSuche.setItems(gefundenListe);
            listStartSuche.refresh();
        //}
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
}

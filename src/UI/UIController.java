package UI;

import Rezeptteile.Kategorie;
import Rezeptteile.Rezeptkopf;
import Rezeptteile.Rezeptzutat;
import controller.KategorieController;
import controller.RezeptkopfController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Comparator;
import java.util.Objects;

public class UIController{

    public Button btnStartNeuRezept;
    public Button btnStartDefinitionsbuch;
    public Button btnStartProgrammBeenden;

    public TextField textStartSuche;
    public ListView listStartSuche;
    public ListView listStartRezepte;

    private Stage stage;
    private Scene scene;

    RezeptkopfController rezeptkopfController = RezeptkopfController.getInstance();

    @FXML ListView getListStartRezepte;
    public void initialize() throws IOException {
        updateListe();
    }

    public void listeRezepteGeklickt(MouseEvent mouseEvent) { //TODO
        if (mouseEvent.getClickCount() == 2){ //Doppelklick abfrage

        }
    }

    public void listeSucheGeklickt(MouseEvent mouseEvent) { //TODO
        if (mouseEvent.getClickCount() == 2){ //Doppelklick abfrage

        }
    }

    public void updateListe() throws IOException { //TODO Button entfernen und das update im Hintergrund machen
        ObservableList<String> rezepteListe = FXCollections.observableArrayList();
        rezeptkopfController.leseDatei();
        for (Rezeptkopf rez: rezeptkopfController.getAlleRezeptkopf()) {
                rezepteListe.add(rez.listViewString());
        }
        sortierenListe(rezepteListe);
        listStartRezepte.setItems(rezepteListe);
        listStartRezepte.refresh();
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

    public void neuesRezeptFenster(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/RezeptAnsehen.fxml")));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Kochbuch: Rezeptansicht");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void definitionsbuchOeffnen(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/DefinitionsbuchV2.fxml")));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Kochbuch: Definitionsbuch");
        stage.setScene(scene);
        stage.setResizable(false);//TODO überall machen
        stage.show();
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

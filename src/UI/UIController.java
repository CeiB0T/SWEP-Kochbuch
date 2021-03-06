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

public class UIController {

    public Button btnStartNeuRezept;
    public Button btnStartDefinitionsbuch;
    public Button btnStartProgrammBeenden;

    public TextField textStartSuche;
    public ListView listStartSuche;
    public ListView listStartRezepte;

    private Stage stage;
    private Scene scene;

    public static Boolean neuesRezept = false;
    public static Rezeptkopf uebertrag = null;
    RezeptkopfController rezeptkopfController = RezeptkopfController.getInstance();

    @FXML
    ListView getListStartRezepte;

    public void initialize() throws IOException {
        updateListe();
    }

    public void listeRezepteGeklickt(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getClickCount() == 2) { //Doppelklick abfrage
            if (!Objects.isNull(listStartRezepte.getSelectionModel().getSelectedItem())) {
                String listenText = String.valueOf(listStartRezepte.getSelectionModel().getSelectedItem());
                String[] listenTextSplit = listenText.split(", ");
                String rezeptname = listenTextSplit[0];
                Rezeptkopf rezeptkopf = rezeptkopfController.getRezeptkopfByName(rezeptname);
                rezeptAnzeigen(mouseEvent, rezeptkopf);
            }
        }
    }

    public void listeSucheGeklickt(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getClickCount() == 2) { //Doppelklick abfrage
            if (listStartSuche.getSelectionModel().getSelectedItem() != null) {
                String element = listStartSuche.getSelectionModel().getSelectedItem().toString();
                String rezeptname;
                if (element.matches("^Zutat: .*")) {//Suche ob start mit Zutat
                    String[] strings = element.split(", ");
                    rezeptname = strings[0].substring("Zutat: ".length());
                    rezeptAnzeigen(mouseEvent, rezeptkopfController.getRezeptkopfByName(rezeptname));
                } else if (element.matches("^Kategorie: .*")) {
                    String[] strings = element.split(", ");
                    rezeptname = strings[0].substring("Kategorie: ".length());
                    rezeptAnzeigen(mouseEvent, rezeptkopfController.getRezeptkopfByName(rezeptname));
                } else {
                    String[] strings = element.split(", ");
                    rezeptname = strings[0];
                    rezeptAnzeigen(mouseEvent, rezeptkopfController.getRezeptkopfByName(rezeptname));
                }
            }
        }
    }

    public void rezeptAnzeigen(MouseEvent mouseEvent, Rezeptkopf rezeptkopf) throws IOException {
        uebertrag = rezeptkopf;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/RezeptAnsehen.fxml")));
        stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Kochbuch: Rezeptansicht: " + rezeptkopf.getrKoRezeptname());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }


    private void updateListe() throws IOException {
        ObservableList<String> rezepteListe = FXCollections.observableArrayList();
        rezeptkopfController.leseDatei();
        for (Rezeptkopf rez : rezeptkopfController.getAlleRezeptkopf()) {
            rezepteListe.add(rez.listViewString());
        }
        sortierenListe(rezepteListe);
        listStartRezepte.setItems(rezepteListe);
        listStartRezepte.refresh();
    }

    private ObservableList sortierenListe(ObservableList<String> liste) {
        liste.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        return liste;
    }

    public void neuesRezeptFenster(ActionEvent actionEvent) throws IOException {
        neuesRezept = true;
        UIController.uebertrag = null;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/RezeptAnsehen.fxml")));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Kochbuch: Rezeptansicht");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void definitionsbuchOeffnen(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/DefinitionsbuchV2.fxml")));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Kochbuch: Definitionsbuch");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void programmBeenden(ActionEvent actionEvent) {
        Stage stage = (Stage) btnStartProgrammBeenden.getScene().getWindow();
        stage.close();
    }

    private String zutatPrefix(Rezeptkopf rez) {
        return "Zutat: " + rez.listViewString();
    }

    private String kategoriePrefix(Rezeptkopf rez) {
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
        if (keyEvent.getCode().equals(KeyCode.BACK_SPACE) && textStartSuche.getText().length() < 1) {
            listStartSuche.getItems().clear();
        }
    }

    private void elementeInSucheAnzeigen() {
        listStartSuche.getItems().clear();

        ObservableList<String> gefundenListe = FXCollections.observableArrayList();
        String suchtext = textStartSuche.getText().toLowerCase();

        for (Kategorie kat : KategorieController.getInstance().getAlleKategorien()) { //Iteriert durch alle Kategorien auf der Suche nach zugeh??rigen gesuchten Rezepten
            if (kat.getKatName().toLowerCase().lastIndexOf(suchtext) != -1) {
                for (Rezeptkopf rez : kat.getKatRezeptkopf()) {
                    gefundenListe.add(kategoriePrefix(rez));
                }
            }
        }

        for (Rezeptkopf rez : rezeptkopfController.getAlleRezeptkopf()) { //Iteriert durch alle Rezeptk??pfe f??r die Suche nach Zutaten ??bereinstimmungen
            for (Rezeptzutat zut : rez.getrKoRezeptzutat()) { //Iteriert durch alle Zutaten eines Rezepts
                if (zut.getrZuZutat().getZutName().toLowerCase().lastIndexOf(suchtext) != -1) { //Pr??ft ob der Name der Zutat dem der suche gleicht
                    gefundenListe.add(zutatPrefix(rez));
                }
            }
        }

        for (Rezeptkopf rez : rezeptkopfController.getAlleRezeptkopf()) { //Gleiche Iteration wie erste foreach, aber hier wird nur nach Rezeptnamen gesucht
            if (rez.getrKoRezeptname().toLowerCase().lastIndexOf(suchtext) != -1) { //Pr??ft ob Rezeptname dem Suchwort entspricht
                gefundenListe.add(rez.listViewString());
            }
        }
        listStartSuche.setItems(gefundenListe);
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
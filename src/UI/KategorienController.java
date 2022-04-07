package UI;

import Rezeptteile.Kategorie;
import Rezeptteile.Rezeptkopf;
import controller.KategorieController;
import controller.RezeptkopfController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Comparator;
import java.util.Objects;

public class KategorienController {
    public Button btnStartseite;
    public Button btnDefinitionsbuch;
    public Button btnRezept;
    public Button btnExit;
    public TextField textKategorieName;
    public Button btnNeueKategorie;
    public ListView listKategorie;
    public Button btnBearbeiten;
    public Button btnSave;
    public Button btnDelete;
    public ListView listKategorieRezepte;
    public ListView listRezepte;
    public Button btnAddToKategorie;

    private Stage stage;
    private Scene scene;
    private Kategorie tempKategorie;
    private KategorieController kategorieController = KategorieController.getInstance();
    private RezeptkopfController rezeptkopfController = RezeptkopfController.getInstance();

    public void initialize(){
        alleRezepteAnzeigen();
    }

    public void zurStartseite(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/HauptmenuV3.fxml")));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Kochbuch: Startseite");
        stage.show();
    }

    public void zumDefinitionsbuch(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/DefinitionsbuchV2.fxml")));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Kochbuch: Definitionsbuch");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void neuesRezept(ActionEvent actionEvent) throws IOException {
        UIController.neuesRezept = true;
        UIController.uebertrag = null;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/RezeptAnsehen.fxml")));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Kochbuch: Rezeptansicht");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void programmVerlassen(ActionEvent actionEvent) {
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.close();
    }

    public void addKategorie(ActionEvent actionEvent) {
        textLoeschen();
        textFelderBearbeiten(true);
    }

    public void kategorienAnzeigen(MouseEvent mouseEvent) {
        updateListe();
    }

    public void kategorieBearbeiten(ActionEvent actionEvent) {
        if (kategorieController.existiertKategorie(textKategorieName.getText().trim())){
            tempKategorie = kategorieController.getKategorie(textKategorieName.getText().trim());
        }
        textFelderBearbeiten(true);
    }

    public void kategorieSpeichern(ActionEvent actionEvent) {
        textFelderBearbeiten(false);
        if (tempKategorie != null){
            bestehendeKategorieAktualisieren(tempKategorie);
        }else {
            String katName = textKategorieName.getText().trim();
            if (katName.matches(".*\\S+.*")){
                kategorieController.neueKategorie(katName);
            }
        }
        updateListe();
    }

    public void kategorieLoeschen(ActionEvent actionEvent) throws IOException { //TODO Confirm Dialog
        if (textKategorieName.getText().matches(".*\\S+.*")) {
            if (kategorieController.existiertKategorie(textKategorieName.getText().trim())){
                kategorieController.löschenKategorie(textKategorieName.getText().trim());
                kategorieController.speichenDatei();
                updateListe();
            }
        }
    }

    public void zugehoerigeRezepteAnzeigen(MouseEvent mouseEvent) {
        if (listKategorie.getSelectionModel().getSelectedItem() != null){
            ObservableList rezepteInKategorie = FXCollections.observableArrayList();
            for (Rezeptkopf rez: kategorieController.getKategorie(listKategorie.getSelectionModel().getSelectedItem().toString()).getKatRezeptkopf()) {
                rezepteInKategorie.add(rez);
            }
            sortierenListe(rezepteInKategorie);
            listKategorieRezepte.setItems(rezepteInKategorie);
        }
    }

    public void rezeptAnzeigen(MouseEvent mouseEvent) throws IOException {//Bei klick auf "alleRezepte" liste
        if(mouseEvent.getClickCount() == 2){
            String[] rezeptname = listRezepte.getSelectionModel().getSelectedItem().toString().split(",");
            Rezeptkopf rez = rezeptkopfController.getRezeptkopfByName(rezeptname[0]);
            UIController.uebertrag = rez;
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/RezeptAnsehen.fxml")));
            stage = (Stage)((Node)mouseEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setTitle("Kochbuch: Rezeptansicht: " + UIController.uebertrag.getrKoRezeptname());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }
    }

    public void alleRezepteAnzeigen(){
        ObservableList alleRezepte = FXCollections.observableArrayList();
        for (Rezeptkopf rez: rezeptkopfController.getAlleRezeptkopf()) {
            alleRezepte.add(rez.listViewString());
        }
        sortierenListe(alleRezepte);
        listRezepte.setItems(alleRezepte);
    }

    public void addRezeptZuKategorie(ActionEvent actionEvent) {
        String katName = textKategorieName.getText().trim();
        if (katName.matches(".*\\S+.*")){
            if (kategorieController.existiertKategorie(katName)){
                if (listRezepte.getSelectionModel().getSelectedItem() != null){
                    String[] werte = listRezepte.getSelectionModel().getSelectedItem().toString().split(",");
                    String rezeptname = werte[0];
                    if (!kategorieController.getKategorie(katName).getKatRezeptkopf().contains(rezeptkopfController.getRezeptkopfByName(rezeptname))){
                        kategorieController.getKategorie(katName).getKatRezeptkopf().add(rezeptkopfController.getRezeptkopfByName(rezeptname));
                        alleRezepteVonKategorieByName();
                    }
                }
            }
        }
    }

    private void textFelderBearbeiten(Boolean bool){
        textKategorieName.setEditable(bool);
    }

    private void textLoeschen(){
        textKategorieName.setText("");
    }

    private void updateListe(){
        ObservableList kategorien = FXCollections.observableArrayList();
        kategorien.add(kategorieController.getAlleKategorien());
        sortierenListe(kategorien);
        listKategorie.setItems(kategorien);
    }

    private ObservableList sortierenListe(ObservableList<String> liste){
        liste.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        return liste;
    }

    private void alleRezepteVonKategorieByName() {
        if (textKategorieName.getText().matches(".*\\S+.*")) {
            ObservableList rezepteInKategorie = FXCollections.observableArrayList();
            for (Rezeptkopf rez : kategorieController.getKategorie(textKategorieName.getText()).getKatRezeptkopf()) {
                rezepteInKategorie.add(rez);
            }
            listKategorieRezepte.setItems(rezepteInKategorie);
        }
    }

    private void bestehendeKategorieAktualisieren(Kategorie kategorie){
        kategorie.setKatName(textKategorieName.getText().trim());
    }
}

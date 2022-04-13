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
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

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

    public void initialize() throws IOException {
        Tooltip rezeptWeg = new Tooltip("Zum löschen eines Rezepts in einer Kategorie bitte eine Kategorie auswählen, dann ein zugehöriges Rezept anklicken und auf den Löschen Button drücken.");
        listKategorieRezepte.setTooltip(rezeptWeg);
        alleRezepteAnzeigen();
        updateListe();
    }

    public void zurStartseite(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/HauptmenuV3.fxml")));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Kochbuch: Startseite");
        stage.show();
    }

    public void zumDefinitionsbuch(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/DefinitionsbuchV2.fxml")));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
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
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
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

    public void kategorieansichtAufrufen(MouseEvent mouseEvent) throws IOException {
        updateListe();
        if (listKategorie.getSelectionModel().getSelectedItem() != null) {
            textKategorieName.setText(listKategorie.getSelectionModel().getSelectedItem().toString());
            zugehoerigeRezepteListeAnzeigen();
        }
    }

    public void kategorieBearbeiten(ActionEvent actionEvent) {
        if (kategorieController.existiertKategorie(textKategorieName.getText().trim())) {
            tempKategorie = kategorieController.getKategorie(textKategorieName.getText().trim());
        }
        textFelderBearbeiten(true);
    }

    public void kategorieSpeichern(ActionEvent actionEvent) throws IOException {
        textFelderBearbeiten(false);
        if (tempKategorie != null) {
            bestehendeKategorieAktualisieren(tempKategorie);
            kategorieController.speichenDatei();
        } else {
            String katName = textKategorieName.getText().trim();
            if (katName.matches(".*\\S+.*")) {
                kategorieController.neueKategorie(katName);
                kategorieController.speichenDatei();
            }
        }
        updateListe();
    }

    public void kategorieLoeschen(ActionEvent actionEvent) throws IOException {
        if (listKategorieRezepte.getSelectionModel().getSelectedItem() != null) {
            String[] daten = listKategorieRezepte.getSelectionModel().getSelectedItem().toString().split(",");
            Rezeptkopf rezept = rezeptkopfController.getRezeptkopfByName(daten[0]);
            kategorieController.getKategorie(textKategorieName.getText()).rezeptLöschen(rezept);
            kategorieController.speichenDatei();
            zugehoerigeRezepteListeAnzeigen();
        } else if (textKategorieName.getText().matches(".*\\S+.*")) {
            if (kategorieController.existiertKategorie(textKategorieName.getText().trim())) {
                Alert wirklichLoeschen = new Alert(Alert.AlertType.CONFIRMATION);
                wirklichLoeschen.setTitle("Löschen bestätigen");
                wirklichLoeschen.setHeaderText("Wollen Sie die Kategorie wirklich löschen?");
                wirklichLoeschen.setContentText("Es wird nur die Kategorie gelöscht, die Rezepe bleiben erhalten.");

                Optional<ButtonType> result = wirklichLoeschen.showAndWait();
                if (result.get() == ButtonType.OK) {
                    kategorieController.löschenKategorie(textKategorieName.getText().trim());
                    kategorieController.speichenDatei();
                    updateListe();
                }
            }
        }
    }

    public void zugehoerigesRezeptAnzeigen(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.getClickCount() == 2) {
            String[] rezeptname = listKategorieRezepte.getSelectionModel().getSelectedItem().toString().split(",");
            Rezeptkopf rez = rezeptkopfController.getRezeptkopfByName(rezeptname[0]);
            UIController.uebertrag = rez;
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/RezeptAnsehen.fxml")));
            stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setTitle("Kochbuch: Rezeptansicht: " + UIController.uebertrag.getrKoRezeptname());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }
    }

    public void rezeptAnzeigen(MouseEvent mouseEvent) throws IOException {//Bei klick auf "alleRezepte" liste
        if (mouseEvent.getClickCount() == 2) {
            String[] rezeptname = listRezepte.getSelectionModel().getSelectedItem().toString().split(",");
            Rezeptkopf rez = rezeptkopfController.getRezeptkopfByName(rezeptname[0]);
            UIController.uebertrag = rez;
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/RezeptAnsehen.fxml")));
            stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setTitle("Kochbuch: Rezeptansicht: " + UIController.uebertrag.getrKoRezeptname());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }
    }

    public void alleRezepteAnzeigen() {
        ObservableList alleRezepte = FXCollections.observableArrayList();
        for (Rezeptkopf rez : rezeptkopfController.getAlleRezeptkopf()) {
            alleRezepte.add(rez.listViewString());
        }
        sortierenListe(alleRezepte);
        listRezepte.setItems(alleRezepte);
    }

    public void addRezeptZuKategorie(ActionEvent actionEvent) throws IOException {
        String katName = textKategorieName.getText().trim();
        if (katName.matches(".*\\S+.*")) {
            if (kategorieController.existiertKategorie(katName)) {
                if (listRezepte.getSelectionModel().getSelectedItem() != null) {
                    String[] werte = listRezepte.getSelectionModel().getSelectedItem().toString().split(",");
                    String rezeptname = werte[0];
                    Boolean vorhanden = false;
                    for (Rezeptkopf rez : kategorieController.getKategorie(katName).getKatRezeptkopf()) {
                        if (rez.getrKoRezeptname().equals(rezeptname)) {
                            vorhanden = true;
                        }
                    }
                    if (!vorhanden) {
                        kategorieController.getKategorie(katName).getKatRezeptkopf().add(rezeptkopfController.getRezeptkopfByName(rezeptname));
                        alleRezepteVonKategorieByName();
                        kategorieController.speichenDatei();
                        zugehoerigeRezepteListeAnzeigen();
                    }
                }
            }
        }
    }

    private void textFelderBearbeiten(Boolean bool) {
        textKategorieName.setEditable(bool);
    }

    private void textLoeschen() {
        textKategorieName.setText("");
    }

    private void updateListe() throws IOException {
        kategorieController.leseDatei();
        ObservableList<String> kategorien = FXCollections.observableArrayList();
        for (Kategorie kat : kategorieController.getAlleKategorien()) {
            kategorien.add(kat.getKatName());
        }
        sortierenListe(kategorien);
        listKategorie.setItems(kategorien);
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

    private void alleRezepteVonKategorieByName() {
        if (textKategorieName.getText().matches(".*\\S+.*")) {
            ObservableList rezepteInKategorie = FXCollections.observableArrayList();
            for (Rezeptkopf rez : kategorieController.getKategorie(textKategorieName.getText()).getKatRezeptkopf()) {
                rezepteInKategorie.add(rez);
            }
            listKategorieRezepte.setItems(rezepteInKategorie);
        }
    }

    private void bestehendeKategorieAktualisieren(Kategorie kategorie) {
        kategorie.setKatName(textKategorieName.getText().trim());
    }

    public void zugehoerigeRezepteListeAnzeigen() throws IOException {
        kategorieController.leseDatei();
        if (listKategorie.getSelectionModel().getSelectedItem() != null) {
            ObservableList<String> rezepteInKategorie = FXCollections.observableArrayList();
            for (Rezeptkopf rez : kategorieController.getKategorie(listKategorie.getSelectionModel().getSelectedItem().toString()).getKatRezeptkopf()) {
                rezepteInKategorie.add(rez.listViewString());
            }
            sortierenListe(rezepteInKategorie);
            listKategorieRezepte.setItems(rezepteInKategorie);
        }
    }
}
package UI;

import Rezeptteile.Rezeptkopf;
import controller.RezeptkopfController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UIController {

    public Button btnStartProgrammBeenden;
    public Button btnStartNeuRezept;
    public Button btnStartDefinitionsbuch;

    public ListView listStartRezepte;
    public ListView listStartSuche;

    public TextField textStartSuche;

    RezeptkopfController rezeptkopfController = RezeptkopfController.getInstance();

    public void rezeptListeUpdate(){
        ObservableList<Rezeptkopf> rezepte = (ObservableList<Rezeptkopf>) rezeptkopfController.getAlleRezeptkopf();
        listStartRezepte.setItems(rezepte);
    }

    public void neuesRezeptFenster(ActionEvent actionEvent) {
        //TODO neue FXML für "neues Rezept" einbinden und Starten
    }

    public void definitionsbuchOeffnen(ActionEvent actionEvent) {
        //TODO neue FXML für "Definitionsbuch" einbinden und Starten
    }

    public void programmBeenden(ActionEvent actionEvent) {
        Stage stage = (Stage) btnStartProgrammBeenden.getScene().getWindow();
        stage.close();
    }
}
package UI;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.Objects;

public class ZutatHinzufuegenController {
    public Button btnReturnHome;
    public Button btnDefinitionsbuch;
    public Button btnNeuesRezept;
    public Button btnExit;
    public Button btnZutatHinzufuegen;
    public Button btnBearbeiten;
    public Button btnSpeichern;
    public Button btnLoeschen;

    public TextArea textMenge;
    public TextArea textEinheit;

    public ListView listZutaten;
    public TextField textTitel;

    private Stage stage;
    private Scene scene;

    public void returnHome (ActionEvent actionEvent) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/HauptmenuV3.fxml")));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Kochbuch: Startseite");
        stage.show();
    }

    public void opnDefinitionsbuch(ActionEvent actionEvent) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/DefinitionsbuchV2.fxml")));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Kochbuch: Definitionsbuch");
        stage.setScene(scene);
        stage.setResizable(false);//TODO überall machen
        stage.show();
    }

    public void addRezept(ActionEvent actionEvent) throws Exception {
        UIController.neuesRezept = true;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/RezeptAnsehen.fxml")));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Kochbuch: Rezeptansicht");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void programmSchliessen(ActionEvent actionEvent) {
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.close();
    }

    public void zutatHinzufügen(ActionEvent actionEvent) {
    }

    public void listDefinitionenKlicked(MouseEvent mouseEvent) {
    }

    public void zutatBearbeiten(ActionEvent actionEvent) {
    }

    public void zutatSpeichern(ActionEvent actionEvent) {
    }

    public void zutatLoeschen(ActionEvent actionEvent) {
    }
}

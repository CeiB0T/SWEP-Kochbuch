package UI;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class UIController {

    public Button btnStartProgrammBeenden;

    public void neuesRezeptFenster(ActionEvent actionEvent) {
    }

    public void definitionsbuchOeffnen(ActionEvent actionEvent) {
    }

    public void programmBeenden(ActionEvent actionEvent) {
        Stage stage = (Stage) btnStartProgrammBeenden.getScene().getWindow();
        stage.close();
    }
}

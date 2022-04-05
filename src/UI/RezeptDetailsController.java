package UI;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import qrcodegen.QrBufferedImage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class RezeptDetailsController {
    
    public Button btnDefinitionsbuch;
    public Button btnReturnHome;
    public Button btnExit;
    public Button btnBearbeiten;
    public Button btnSpeichern;
    public Button btnLoeschen;
    public Button btnNeuesRezept;

    public ImageView imgQR;
    public TextArea textRezeptNamen;
    public TextArea textZutaten;
    public TextArea textZubereitung;

    private Stage stage;
    private Scene scene;

    public void initialize(){
    }

    public void openDefinition(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/DefinitionsbuchV2.fxml")));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Kochbuch: Startseite");
        stage.setScene(scene);
        stage.setResizable(false);//TODO überall machen
        stage.show();
    }

    public void returnHome(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/resource/HauptmenuV3.fxml")));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Kochbuch: Startseite");
        stage.setScene(scene);
        stage.setResizable(false);//TODO überall machen
        stage.show();
    }

    public void programmSchließen(ActionEvent actionEvent) {
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.close();
    }

    public void rezeptBearbeiten(ActionEvent actionEvent) {
    }

    public void rezeptSpeichern(ActionEvent actionEvent) {
    }

    public void rezeptLöschen(ActionEvent actionEvent) {
    }

    public void neuesRezept(ActionEvent actionEvent) {
    }

    public void qrAnzeigen() throws IOException {
        BufferedImage bufferedImage = QrBufferedImage.qrLinkGenerieren("https://www.youtube.com/watch?v=o-YBDTqX_ZU");
        //imgQR.setImage(bufferedImage.getScaledInstance(0,0,0));
    }
}

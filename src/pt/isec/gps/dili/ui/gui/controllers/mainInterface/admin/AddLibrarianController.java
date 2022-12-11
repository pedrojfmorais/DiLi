package pt.isec.gps.dili.ui.gui.controllers.mainInterface.admin;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pt.isec.gps.dili.model.fsm.DiliContext;
import pt.isec.gps.dili.ui.gui.resources.ImageManager;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddLibrarianController implements Initializable {

    public BorderPane addLibrarian;
    public TextField tfName;
    public TextField tfEmail;
    public TextField tfPassword;
    public Button btnConfirm;
    public Button btnCancel;
    private DiliContext fsm;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addLibrarian.sceneProperty().addListener((observableValue, oldScene, newScene) -> {
            fsm = (DiliContext) newScene.getUserData();
            createViews();
            registerHandlers();
        });
    }

    private void createViews() {
    }

    private void registerHandlers() {
        btnCancel.setOnAction(ev -> {
            Stage stage = (Stage) addLibrarian.getScene().getWindow();
            stage.close();
        });
        btnConfirm.setOnAction(ev -> {
            System.out.println(tfName.getText());
            System.out.println(tfEmail.getText());
            System.out.println(tfPassword.getText());
            System.out.println(fsm.getState()); // Dá erro aqui porque o fsm é null

        });
    }
}

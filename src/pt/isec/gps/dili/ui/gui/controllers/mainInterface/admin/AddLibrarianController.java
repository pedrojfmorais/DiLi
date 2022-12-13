package pt.isec.gps.dili.ui.gui.controllers.mainInterface.admin;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pt.isec.gps.dili.model.data.DiLi;
import pt.isec.gps.dili.model.data.Message;
import pt.isec.gps.dili.model.data.MessageType;
import pt.isec.gps.dili.model.data.book.Book;
import pt.isec.gps.dili.model.fsm.DiliContext;
import pt.isec.gps.dili.ui.gui.resources.ImageManager;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddLibrarianController implements Initializable {

    public BorderPane addLibrarian;
    public TextField tfName;
    public TextField tfEmail;
    public PasswordField tfPassword;
    public Button btnConfirm;
    public Button btnCancel;
    public Label lbName;
    public Label lbEmail;
    public Label lbPassword;
    private DiliContext fsm;

    private String type = "new";

    public void initData() {
        tfName.setText(DiLi.getLoggedAccount().getName());
        tfEmail.setText(DiLi.getLoggedAccount().getEmail());
        type = "update";
    }

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
            Message message = null;
            if(type.equals("new"))
                 message = fsm.createLibrarian(tfName.getText(), tfEmail.getText(), tfPassword.getText());
            else if(type.equals("update"))
                 message = fsm.updateLibrarianInfo(tfName.getText(), tfEmail.getText(), tfPassword.getText());

            if(message == null)
                return;
            
            if (message.getType() == MessageType.SUCCESS) {
                Alert alert = new Alert(
                        Alert.AlertType.INFORMATION,
                        "",
                        ButtonType.OK
                );
                alert.setTitle(message.getField());
                alert.setHeaderText(message.getMessage());

                alert.showAndWait().ifPresent(response -> {
                    Stage stage = (Stage) addLibrarian.getScene().getWindow();
                    stage.close();
                });
            } else {

                Alert alert = new Alert(
                        Alert.AlertType.ERROR,
                        "",
                        ButtonType.OK
                );
                alert.setTitle("Error");
                alert.setHeaderText("Field: " + message.getField());
                alert.setContentText(message.getMessage());

                alert.showAndWait();
            }
        });
    }
}

package pt.isec.gps.dili.ui.gui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import pt.isec.gps.dili.model.fsm.DiliContext;
import pt.isec.gps.dili.model.fsm.DiliState;
import pt.isec.gps.dili.ui.gui.resources.ImageManager;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    private DiliContext fsm;
    @FXML
    private BorderPane borderPane;
    @FXML
    private ImageView ivLogo;
    @FXML
    private TextField tfEmail, tfPassword;
    @FXML
    private Label lbError;
    @FXML
    private Button btnLogin, btnExit;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        borderPane.sceneProperty().addListener((observableValue, oldScene, newScene) -> {
            fsm = (DiliContext) newScene.getUserData();
            createViews();
            registerHandlers();
            update();
        });
    }

    private void createViews() {
        ivLogo.setImage(ImageManager.getImage("logo.png"));
    }

    private void registerHandlers() {
        fsm.addPropertyChangeListener(DiliContext.PROP_FASE, evt -> update());

        btnLogin.setOnAction(ev -> lbError.setText(fsm.login(tfEmail.getText(), tfPassword.getText()).getMessage()));

        btnExit.setOnAction(ev -> Platform.exit());
    }

    private void update() {
        borderPane.setVisible(fsm != null && fsm.getState() == DiliState.LOGIN);
        lbError.setText("");
    }
}

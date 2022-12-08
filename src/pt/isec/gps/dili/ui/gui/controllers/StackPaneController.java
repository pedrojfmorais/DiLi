package pt.isec.gps.dili.ui.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pt.isec.gps.dili.model.fsm.DiliContext;

import java.net.URL;
import java.util.ResourceBundle;

public class StackPaneController implements Initializable {
    private DiliContext fsm;
    @FXML
    private StackPane stackPane;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stackPane.sceneProperty().addListener((observableValue, oldScene, newScene) -> {
            fsm = (DiliContext) newScene.getUserData();
            registerHandlers();
            update();
        });
    }

    private void registerHandlers() {
        fsm.addPropertyChangeListener(DiliContext.PROP_FASE, evt -> update());
    }

    private void update() {
        Stage stage = (Stage) stackPane.getScene().getWindow();
        String titulo;
        switch (fsm.getState()) {
            case LOGIN -> titulo = "DiLi - Login";
            default -> titulo = "";
        }
        stage.setTitle(titulo);
    }
}

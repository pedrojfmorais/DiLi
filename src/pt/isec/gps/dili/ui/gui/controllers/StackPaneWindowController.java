package pt.isec.gps.dili.ui.gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pt.isec.gps.dili.model.fsm.DiliContext;

import java.net.URL;
import java.util.ResourceBundle;

public class StackPaneWindowController implements Initializable {
    @FXML
    private BorderPane login;
    @FXML
    private BorderPane adminInterface;
    @FXML
    private BorderPane userInterface;
    private DiliContext fsm;
    @FXML
    private StackPane stackPane;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stackPane.sceneProperty().addListener((observableValue, oldScene, newScene) -> {
            fsm = (DiliContext) newScene.getUserData();
            registerHandlers();
        });
    }

    private void registerHandlers() {
        fsm.addPropertyChangeListener(DiliContext.PROP_FASE, evt -> update());

        stackPane.widthProperty().addListener(obs -> updateBoardSize());
        stackPane.heightProperty().addListener(obs -> updateBoardSize());
    }

    private void updateBoardSize() {
        Stage stage = (Stage) stackPane.getScene().getWindow();

        if(stage == null)
            return;

        double width = 0;
        double height = 0;

        switch (fsm.getState()){
            case LOGIN -> {
                width = login.getWidth();
                height = login.getHeight();
            }
            case MAIN_INTERFACE, PROFILE -> {
                width = adminInterface.getWidth();
                height = adminInterface.getHeight();
            }
        }
        stage.setWidth(width + 20);
        stage.setHeight(height + 20);
    }

    private void update() {
        Stage stage = (Stage) stackPane.getScene().getWindow();
        String titulo;
        switch (fsm.getState()) {
            case LOGIN -> titulo = "DiLi - Login";
            case MAIN_INTERFACE -> titulo = "DiLi";
            case PROFILE -> titulo = "DiLi - Profile";
            default -> titulo = "";
        }
        stage.setTitle(titulo);
        updateBoardSize();
    }
}

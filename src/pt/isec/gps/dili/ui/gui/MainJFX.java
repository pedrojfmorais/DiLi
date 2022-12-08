package pt.isec.gps.dili.ui.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pt.isec.gps.dili.model.fsm.DiliContext;
import pt.isec.gps.dili.ui.gui.resources.ImageManager;

import java.io.IOException;
import java.sql.SQLException;

public class MainJFX extends Application {
    DiliContext fsm;

    public MainJFX() throws SQLException {
        this.fsm = DiliContext.getInstance();
    }

    @Override
    public void init() throws Exception {
        super.init();
    }

    @Override
    public void start(Stage stage) throws IOException {
        configureGUI(stage);
    }

    private void configureGUI(Stage stage) throws IOException {
        Scene scene = new Scene(new Pane(), 500, 500, Color.BLACK);
        scene.setUserData(fsm);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/stackPane.fxml"));
        scene.setRoot(loader.load());

        stage.setScene(scene);
        stage.setTitle("DiLi");
        stage.getIcons().add(ImageManager.getImage("logo.png"));
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}

package pt.isec.gps.dili.ui.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pt.isec.gps.dili.model.fsm.DiliContext;
import pt.isec.gps.dili.ui.gui.resources.ImageManager;

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
    public void start(Stage stage) {
        configureGUI(stage);
    }

    private void configureGUI(Stage stage){
        Scene scene = new Scene(new DiliGUI(fsm), 500, 500, Color.BLACK);
        stage.setScene(scene);
        stage.setTitle("DiLi");
//        stage.getIcons().add(ImageManager.getImage("mini_logo_isec.png"));
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}

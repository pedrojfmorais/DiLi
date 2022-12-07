package pt.isec.gps.dili.ui.gui;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import pt.isec.gps.dili.model.fsm.DiliContext;
import pt.isec.gps.dili.ui.gui.fsmStates.LoginGUI;

public class DiliGUI extends BorderPane {
    DiliContext fsm;

    public DiliGUI(DiliContext fsm) {
        this.fsm = fsm;

        createViews();
        registerHandlers();
    }

    private void createViews() {
        StackPane stackPane = new StackPane(
                new LoginGUI(fsm)
        );
//        this.setTop(new AppMenuGUI(fsm));
        this.setCenter(stackPane);
//        this.setBottom(new RodapeGUI());
    }

    private void registerHandlers() {
        fsm.addPropertyChangeListener(DiliContext.PROP_FASE, evt -> update());
    }

    private void update() {
        Stage stage = (Stage) this.getScene().getWindow();
        String titulo;
        switch (fsm.getState()) {

            case LOGIN -> titulo = "DiLi - Login";
            default -> titulo = "";
        }
        stage.setTitle(titulo);
    }
}

package pt.isec.gps.dili.ui.gui.controllers.mainInterface;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import pt.isec.gps.dili.model.data.book.Book;
import pt.isec.gps.dili.model.fsm.DiliContext;
import pt.isec.gps.dili.model.fsm.DiliState;
import pt.isec.gps.dili.ui.gui.controllers.mainInterface.admin.ScrollPaneBookItemAdminController;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ScrollPaneBookItemsController implements Initializable {
    private DiliContext fsm;
    @FXML
    private ScrollPane scrollPaneBookItems;
    private String filtro = "";

    public void initData(String filtro) {
        this.filtro = filtro;

        try {
            createViews();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        registerHandlers();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        scrollPaneBookItems.sceneProperty().addListener((observableValue, oldScene, newScene) -> {
            if (newScene != null)
                fsm = (DiliContext) newScene.getUserData();
        });
    }

    private void createViews() throws IOException {

        scrollPaneBookItems.setFitToWidth(true);
        scrollPaneBookItems.setFitToHeight(true);

        VBox vBoxItems = new VBox();
        ArrayList<Book> books;
        try {
            books = fsm.getData().search(filtro);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (var oneBook : books) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../fxml/mainInterface/admin/scrollPaneBookItemAdmin.fxml"));

            Node node;
            try {
                node = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            ScrollPaneBookItemAdminController mibiad = loader.getController();
            mibiad.initData(oneBook);

            node.minWidth(scrollPaneBookItems.getWidth());

            vBoxItems.getChildren().add(node);
        }
        scrollPaneBookItems.setContent(vBoxItems);
    }

    private void registerHandlers() {
        fsm.addPropertyChangeListener(DiliContext.PROP_FASE, evt -> update());
    }

    private void update() {
        scrollPaneBookItems.setVisible(fsm != null && fsm.getState() == DiliState.MAIN_INTERFACE);
    }
}

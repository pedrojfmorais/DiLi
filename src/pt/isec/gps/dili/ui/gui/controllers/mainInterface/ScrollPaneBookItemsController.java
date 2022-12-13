package pt.isec.gps.dili.ui.gui.controllers.mainInterface;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import pt.isec.gps.dili.model.data.DiLi;
import pt.isec.gps.dili.model.data.book.Book;
import pt.isec.gps.dili.model.data.user.UserType;
import pt.isec.gps.dili.model.fsm.DiliContext;
import pt.isec.gps.dili.model.fsm.DiliState;
import pt.isec.gps.dili.ui.gui.controllers.mainInterface.admin.ScrollPaneBookItemAdminController;
import pt.isec.gps.dili.ui.gui.controllers.mainInterface.user.ScrollPaneBookItemUserController;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ScrollPaneBookItemsController implements Initializable {
    private DiliContext fsm;
    @FXML
    private ScrollPane scrollPaneBookItems;
    private List<Book> livros = new ArrayList<>();

    public void initData(List<Book> livros) {
        this.livros = livros;

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

        for (var oneBook : livros) {
            if(DiLi.getLoggedAccount().getTypeUser() == UserType.LIBRARIAN) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../../fxml/mainInterface/admin/scrollPaneBookItemAdmin.fxml"));

                Node node;
                try {
                    node = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                ScrollPaneBookItemAdminController spbiac = loader.getController();
                spbiac.initData(oneBook);

                node.minWidth(scrollPaneBookItems.getWidth());

                vBoxItems.getChildren().add(node);
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../../fxml/mainInterface/user/scrollPaneBookItemUser.fxml"));

                Node node;
                try {
                    node = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                ScrollPaneBookItemUserController spbiuc = loader.getController();
                spbiuc.initData(oneBook);

                node.minWidth(scrollPaneBookItems.getWidth());

                vBoxItems.getChildren().add(node);
            }
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

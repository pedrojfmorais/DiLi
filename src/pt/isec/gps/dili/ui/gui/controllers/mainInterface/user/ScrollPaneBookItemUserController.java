package pt.isec.gps.dili.ui.gui.controllers.mainInterface.user;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pt.isec.gps.dili.model.data.book.Book;
import pt.isec.gps.dili.model.fsm.DiliContext;
import pt.isec.gps.dili.model.fsm.DiliState;
import pt.isec.gps.dili.ui.gui.controllers.mainInterface.admin.AddBookController;
import pt.isec.gps.dili.ui.gui.controllers.mainInterface.admin.MainInterfaceAdminController;
import pt.isec.gps.dili.ui.gui.resources.ImageManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ScrollPaneBookItemUserController extends Node implements Initializable {
    private DiliContext fsm;
    public BorderPane bookInterfaceAdmin;
    public ImageView ivBook;
    public Label lbTitle;
    public Label lbSynopsis;
    public Button btnInfo;
    Book livro;

    public void initData(Book livro) {

        this.livro = livro;

        Image img = ImageManager.getExternalImage(livro.getImagePath());
        if (img == null)
            img = ImageManager.getImage("book_generic.png");

        ivBook.setImage(img);

        lbTitle.setText(livro.getTitle());
        lbSynopsis.setText(livro.getSynopsis());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bookInterfaceAdmin.sceneProperty().addListener((observableValue, oldScene, newScene) -> {
            if (newScene != null)
                fsm = (DiliContext) newScene.getUserData();
            registerHandlers();
            update();
        });
    }

    private void registerHandlers() {
        fsm.addPropertyChangeListener(DiliContext.PROP_BOOK, evt -> update());

        btnInfo.setOnAction(ev -> {
            MainInterfaceUserController.setIdLivroInfo(livro.getId());
            fsm.changeState(DiliState.BOOK_INFO.createState(fsm, fsm.getData()));
        });
    }

    private void update() {

        if (livro != null)
            livro = fsm.getData().getBookById(livro.getId());
    }
}

package pt.isec.gps.dili.ui.gui.controllers.mainInterface.admin;

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
import pt.isec.gps.dili.ui.gui.resources.ImageManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ScrollPaneBookItemAdminController extends Node implements Initializable {
    private DiliContext fsm;
    public BorderPane bookInterfaceAdmin;
    public ImageView ivBook;
    public Label lbTitle;
    public Label lbSynopsis;
    public Button btnInfo;
    public Button btnVisible;
    public Button btnEdit;
    public Button btnDelete;
    Book livro;

    public void initData(Book livro) {

        this.livro = livro;

        Image img = ImageManager.getImage(livro.getImagePath());

        if (img == null)
            ImageManager.getExternalImage(livro.getImagePath());

        if (img == null)
            img = ImageManager.getImage("book_generic.png");

        ivBook.setImage(img);

        lbTitle.setText(livro.getTitle());
        lbSynopsis.setText(livro.getSynopsis());

        btnEdit.setGraphic(ImageManager.getImageView("edit_icon.png", 20));
        btnDelete.setGraphic(ImageManager.getImageView("delete_icon.png", 20));
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

        btnVisible.setOnAction(ev -> {
            fsm.changeBookVisibility(livro.getId());
            livro.setAvailability(!livro.isAvailability());
        });

        btnDelete.setOnAction(ev -> fsm.removeBook(livro.getId()));

        btnEdit.setOnAction(ev -> {
            FXMLLoader loaderBook = new FXMLLoader(getClass().getResource("../../../fxml/mainInterface/admin/addBook.fxml"));
            Stage dialog = new Stage();
            Scene scene = new Scene(new Pane());
            scene.setUserData(fsm);
            try {
                scene.setRoot(loaderBook.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            AddBookController abc = loaderBook.getController();

            dialog.setScene(scene);
            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.getIcons().add(ImageManager.getImage("logo.png"));
            dialog.initOwner(bookInterfaceAdmin.getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.getScene().setUserData(fsm);
            dialog.show();
            abc.initData(livro);
        });

        btnInfo.setOnAction(ev -> {
            MainInterfaceAdminController.setIdLivroInfo(livro.getId());
            fsm.changeState(DiliState.BOOK_INFO.createState(fsm, fsm.getData()));
        });
    }

    private void update() {

        if (livro != null)
            livro = fsm.getData().getBookById(livro.getId());

        if (livro == null)
            return;

        if (livro.isAvailability())
            btnVisible.setGraphic(ImageManager.getImageView("visible_icon.png", 20));
        else
            btnVisible.setGraphic(ImageManager.getImageView("not_visible_icon.png", 20));
    }
}

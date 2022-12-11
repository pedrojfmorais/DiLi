package pt.isec.gps.dili.ui.gui.controllers.mainInterface.admin;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import pt.isec.gps.dili.model.data.book.Book;
import pt.isec.gps.dili.model.fsm.DiliContext;
import pt.isec.gps.dili.ui.gui.resources.ImageManager;

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

    public void initData(Book livro){

        this.livro = livro;

        Image img = ImageManager.getImage(livro.getImagePath());
        if(img == null)
            img = ImageManager.getImage("book_generic.png");

        ivBook.setImage(img);

        lbTitle.setText(livro.getTitle());
        lbSynopsis.setText(livro.getSynopsis());

        btnEdit.setGraphic(ImageManager.getImageView("edit_icon.png", 20));
        btnDelete.setGraphic(ImageManager.getImageView("delete_icon.png", 20));

        //TODO: acabar inicialização das cenas
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bookInterfaceAdmin.sceneProperty().addListener((observableValue, oldScene, newScene) -> {
            fsm = (DiliContext) newScene.getUserData();
            registerHandlers();
        });
    }

    private void registerHandlers() {
        fsm.addPropertyChangeListener(DiliContext.PROP_FASE, evt -> update());
    }

    private void update() {
        if(livro.isAvailability())
            btnVisible.setGraphic(ImageManager.getImageView("visible_icon.png", 20));
        else
            btnVisible.setGraphic(ImageManager.getImageView("not_visible_icon.png", 20));
    }

}

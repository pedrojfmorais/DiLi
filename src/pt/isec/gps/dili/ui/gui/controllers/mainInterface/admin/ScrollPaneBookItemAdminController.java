package pt.isec.gps.dili.ui.gui.controllers.mainInterface.admin;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import pt.isec.gps.dili.model.data.book.Book;
import pt.isec.gps.dili.model.fsm.DiliContext;

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

    public void initData(Book livro){
        lbTitle.setText(livro.getTitle());
        lbSynopsis.setText(livro.getSynopsis());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bookInterfaceAdmin.sceneProperty().addListener((observableValue, oldScene, newScene) -> {
            fsm = (DiliContext) newScene.getUserData();
            if(observableValue instanceof Book)
                System.out.println("è livro");
        });
    }
}

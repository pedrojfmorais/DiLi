package pt.isec.gps.dili.ui.gui.controllers.mainInterface.admin;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pt.isec.gps.dili.model.fsm.DiliContext;
import pt.isec.gps.dili.ui.gui.resources.ImageManager;

import java.net.URL;
import java.util.ResourceBundle;

public class AddBookController implements Initializable {
    public BorderPane addBook;
    public TextField tfTitle;
    public TextField tfAuthor;
    public TextField tfGenre;
    public Button btnAddGenre;
    public TextField tfLanguage;
    public TextArea tfSynopsis;
    public TextField tfBookFormat;
    public TextField tfLink;
    public Button btnAddBookFormat;
    public TextField tfPrice;
    public Button btnConfirm;
    public Button btnCancel;
    private DiliContext fsm;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addBook.sceneProperty().addListener((observableValue, oldScene, newScene) -> {
            fsm = (DiliContext) newScene.getUserData();
            createViews();
            registerHandlers();
        });
    }

    private void createViews() {
        btnAddGenre.setGraphic(ImageManager.getImageView("add_icon.png", 20));
        btnAddBookFormat.setGraphic(ImageManager.getImageView("add_icon.png", 20));
    }

    private void registerHandlers() {
        btnCancel.setOnAction(ev -> {
            Stage stage = (Stage) addBook.getScene().getWindow();
            stage.close();
        });
    }
}

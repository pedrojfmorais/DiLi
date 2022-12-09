package pt.isec.gps.dili.ui.gui.controllers.mainInterface.admin;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import pt.isec.gps.dili.model.data.DiLi;
import pt.isec.gps.dili.model.fsm.DiliContext;
import pt.isec.gps.dili.model.fsm.DiliState;
import pt.isec.gps.dili.ui.gui.resources.ImageManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainInterfaceAdminController implements Initializable {
    @FXML
    private MenuItem miProfile;
    @FXML
    private MenuItem miAddBook;
    @FXML
    private MenuItem miAddLibrarian;
    @FXML
    private MenuItem miStatistics;
    @FXML
    private Pane paneItems;
    @FXML
    private VBox vboxFilters;
    private DiliContext fsm;
    @FXML
    private BorderPane borderPane;
    @FXML
    private ImageView ivLogo;
    @FXML
    private TextField tfSearch;
    @FXML
    private Label lbUsername;
    @FXML
    private Button btnLogout;
    @FXML
    private MenuButton mbManage;

    private String options[] = {"Profile", "Add Book", "Add Librarian", "Statistics"};
    private String comboBoxValue = "Manage";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        borderPane.sceneProperty().addListener((observableValue, oldScene, newScene) -> {
            fsm = (DiliContext) newScene.getUserData();
            createViews();
            registerHandlers();
            update();
        });
    }

    private void createViews() {
        ivLogo.setImage(ImageManager.getImage("logo.png"));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../fxml/mainInterface/scrollPaneBookItems.fxml"));
        try {
            paneItems.getChildren().add(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void registerHandlers() {
        fsm.addPropertyChangeListener(DiliContext.PROP_FASE, evt -> update());

        //TODO: seleção das cenas na combobox
        miProfile.setOnAction(ev -> System.out.println("miProfile"));
        miAddBook.setOnAction(ev -> System.out.println("miAddBook"));
        miAddLibrarian.setOnAction(ev -> System.out.println("miAddLibrarian"));
        miStatistics.setOnAction(ev -> System.out.println("miStatistics"));

        btnLogout.setOnAction(ev -> fsm.logout());
    }

    private void update() {
        borderPane.setVisible(fsm != null && fsm.getState() == DiliState.MainInterface);

        if (DiLi.getLoggedAccount() != null)
            lbUsername.setText(DiLi.getLoggedAccount().getName());
    }
}

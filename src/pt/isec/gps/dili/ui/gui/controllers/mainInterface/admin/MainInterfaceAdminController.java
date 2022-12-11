package pt.isec.gps.dili.ui.gui.controllers.mainInterface.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pt.isec.gps.dili.model.data.DiLi;
import pt.isec.gps.dili.model.fsm.DiliContext;
import pt.isec.gps.dili.model.fsm.DiliState;
import pt.isec.gps.dili.ui.gui.controllers.mainInterface.ProfileController;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        borderPane.sceneProperty().addListener((observableValue, oldScene, newScene) -> {
            fsm = (DiliContext) newScene.getUserData();
            try {
                createViews();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            registerHandlers();
            update();
        });
    }

    private void createViews() throws IOException {
        ivLogo.setImage(ImageManager.getImage("logo.png"));

        FXMLLoader loaderScrollPaneBookItems = new FXMLLoader(getClass().getResource("../../../fxml/mainInterface/scrollPaneBookItems.fxml"));
        FXMLLoader loaderProfile = new FXMLLoader(getClass().getResource("../../../fxml/mainInterface/profile.fxml"));
        try {
            paneItems.getChildren().addAll((Node) loaderScrollPaneBookItems.load(), (Node) loaderProfile.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void registerHandlers() {
        fsm.addPropertyChangeListener(DiliContext.PROP_FASE, evt -> update());

        //TODO: seleção das cenas na combobox
        miProfile.setOnAction(ev -> fsm.changeState(DiliState.PROFILE.createState(fsm, fsm.getData())));
        miAddBook.setOnAction(ev -> {
            FXMLLoader loaderProfile = new FXMLLoader(getClass().getResource("../../../fxml/mainInterface/admin/addBook.fxml"));
            Stage dialog = new Stage();
            try {
                dialog.setScene(new Scene(loaderProfile.load()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.getIcons().add(ImageManager.getImage("logo.png"));
            dialog.initOwner(borderPane.getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();

        });
        miAddLibrarian.setOnAction(ev -> {
            FXMLLoader loaderProfile = new FXMLLoader(getClass().getResource("../../../fxml/mainInterface/admin/addLibrarian.fxml"));
            Stage dialog = new Stage();
            try {
                dialog.setScene(new Scene(loaderProfile.load()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.getIcons().add(ImageManager.getImage("logo.png"));
            dialog.initOwner(borderPane.getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.showAndWait();

        });
        miStatistics.setOnAction(ev -> System.out.println("miStatistics"));

        btnLogout.setOnAction(ev -> fsm.logout());
    }

    private void update() {
        borderPane.setVisible(fsm != null && (
                        fsm.getState() == DiliState.MAIN_INTERFACE
                                || fsm.getState() == DiliState.PROFILE
                )
        );

        if (DiLi.getLoggedAccount() != null)
            lbUsername.setText(DiLi.getLoggedAccount().getName());
//
//        if (fsm.getState() == DiliState.MAIN_INTERFACE) {
//            paneItems.getChildren().clear();
//            paneItems.getChildren().add(nodeBookSearch);
//        }
//
//        if (fsm.getState() == DiliState.PROFILE) {
//            paneItems.getChildren().clear();
//            paneItems.getChildren().add(nodeProfile);
//        }
    }
}

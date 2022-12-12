package pt.isec.gps.dili.ui.gui.controllers.mainInterface.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pt.isec.gps.dili.model.data.DiLi;
import pt.isec.gps.dili.model.data.user.UserType;
import pt.isec.gps.dili.model.fsm.DiliContext;
import pt.isec.gps.dili.model.fsm.DiliState;
import pt.isec.gps.dili.ui.gui.controllers.mainInterface.ProfileController;
import pt.isec.gps.dili.ui.gui.controllers.mainInterface.ScrollPaneBookItemsController;
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
    }

    private void registerHandlers() {
        fsm.addPropertyChangeListener(DiliContext.PROP_FASE, evt -> update());
        fsm.addPropertyChangeListener(DiliContext.PROP_BOOK, evt -> update());
        fsm.addPropertyChangeListener(DiliContext.PROP_USER, evt -> update());

        tfSearch.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                update();
            }
        });

        miProfile.setOnAction(ev -> fsm.changeState(DiliState.PROFILE.createState(fsm, fsm.getData())));
        miAddBook.setOnAction(ev -> {
            FXMLLoader loaderProfile = new FXMLLoader(getClass().getResource("../../../fxml/mainInterface/admin/addBook.fxml"));
            Stage dialog = new Stage();
            Scene scene = new Scene(new Pane());
            scene.setUserData(fsm);
            try {
                scene.setRoot(loaderProfile.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            dialog.setScene(scene);

            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.getIcons().add(ImageManager.getImage("logo.png"));
            dialog.initOwner(borderPane.getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.getScene().setUserData(fsm);
            dialog.showAndWait();

        });
        miAddLibrarian.setOnAction(ev -> {
            FXMLLoader loaderProfile = new FXMLLoader(getClass().getResource("../../../fxml/mainInterface/admin/addLibrarian.fxml"));
            Stage dialog = new Stage();
            Scene scene = new Scene(new Pane());
            scene.setUserData(fsm);
            try {
                scene.setRoot(loaderProfile.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            dialog.setScene(scene);
            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.getIcons().add(ImageManager.getImage("logo.png"));
            dialog.initOwner(borderPane.getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.getScene().setUserData(fsm);
            dialog.showAndWait();
        });

        //TODO: seleção das cenas na combobox
        miStatistics.setOnAction(ev -> System.out.println("miStatistics"));

        btnLogout.setOnAction(ev -> fsm.logout());
    }

    private void update() {
        borderPane.setVisible(fsm != null && (
                        fsm.getState() == DiliState.MAIN_INTERFACE
                                || fsm.getState() == DiliState.PROFILE
                )
                && DiLi.getLoggedAccount() != null && DiLi.getLoggedAccount().getTypeUser() == UserType.LIBRARIAN
        );

        if (DiLi.getLoggedAccount() != null)
            lbUsername.setText(DiLi.getLoggedAccount().getName());

        if (fsm.getState() == DiliState.MAIN_INTERFACE) {
            if (paneItems.getChildren().size() > 0)
                paneItems.getChildren().remove(0);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../fxml/mainInterface/scrollPaneBookItems.fxml"));

            Node node;
            try {
                node = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            ScrollPaneBookItemsController spbic = loader.getController();
            String filter = tfSearch.getText();

            paneItems.getChildren().add(node);
            spbic.initData(filter.isEmpty() ? "" : filter);
        }

        if (fsm.getState() == DiliState.PROFILE) {
            if (paneItems.getChildren().size() > 0)
                paneItems.getChildren().remove(0);

            FXMLLoader loaderProfile = new FXMLLoader(getClass().getResource("../../../fxml/mainInterface/profile.fxml"));

            try {
                paneItems.getChildren().add(loaderProfile.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

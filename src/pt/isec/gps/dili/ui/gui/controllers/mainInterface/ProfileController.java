package pt.isec.gps.dili.ui.gui.controllers.mainInterface;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pt.isec.gps.dili.model.data.DiLi;
import pt.isec.gps.dili.model.data.book.Book;
import pt.isec.gps.dili.model.data.user.User;
import pt.isec.gps.dili.model.fsm.DiliContext;
import pt.isec.gps.dili.model.fsm.DiliState;
import pt.isec.gps.dili.ui.gui.controllers.mainInterface.admin.AddLibrarianController;
import pt.isec.gps.dili.ui.gui.controllers.mainInterface.admin.ScrollPaneBookItemAdminController;
import pt.isec.gps.dili.ui.gui.resources.ImageManager;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    @FXML
    private BorderPane profile;
    @FXML
    private Button btnVoltar;
    @FXML
    private Button btnEditar;
    @FXML
    private ImageView ivProfilePic;
    @FXML
    private Label lbUsername;
    @FXML
    private Label lbEmail;
    @FXML
    private DiliContext fsm;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        profile.sceneProperty().addListener((observableValue, oldScene, newScene) -> {
            if (newScene != null)
                fsm = (DiliContext) newScene.getUserData();

            if (fsm != null) {
                createViews();
                registerHandlers();
                update();
            }
        });
    }

    private void createViews() {
        ivProfilePic.setImage(ImageManager.getImage("user.png"));

        btnVoltar.setGraphic(ImageManager.getImageView("left_arrow.png", 20));
        btnEditar.setGraphic(ImageManager.getImageView("edit_icon.png", 20));

        btnVoltar.setText("");
        btnEditar.setText("");
    }

    private void registerHandlers() {
        fsm.addPropertyChangeListener(DiliContext.PROP_FASE, evt -> update());
        fsm.addPropertyChangeListener(DiliContext.PROP_USER, evt -> update());

        btnVoltar.setOnAction(ev -> fsm.voltar());

        btnEditar.setOnAction(ev -> {
            FXMLLoader loaderProfile = new FXMLLoader(getClass().getResource("../../fxml/mainInterface/admin/addLibrarian.fxml"));
            Stage dialog = new Stage();
            Scene scene = new Scene(new Pane());
            scene.setUserData(fsm);
            try {
                scene.setRoot(loaderProfile.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            AddLibrarianController alc = loaderProfile.getController();
            alc.initData();

            dialog.setScene(scene);
            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.getIcons().add(ImageManager.getImage("logo.png"));
            dialog.initOwner(profile.getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.getScene().setUserData(fsm);
            dialog.showAndWait();
        });
    }

    private void update() {

        profile.setVisible(fsm != null && fsm.getState() == DiliState.PROFILE);

        if (DiLi.getLoggedAccount() != null) {
            User user = DiLi.getLoggedAccount();
            lbUsername.setText(user.getName());
            lbEmail.setText(user.getEmail());
        }
    }
}

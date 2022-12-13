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
import pt.isec.gps.dili.ui.gui.controllers.mainInterface.ScrollPaneBookItemsController;
import pt.isec.gps.dili.ui.gui.resources.ImageManager;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainInterfaceAdminController implements Initializable {
    @FXML
    private VBox vBoxFiltersLanguage;
    @FXML
    private VBox vBoxFiltersGenre;
    @FXML
    private VBox vBoxFiltersBookFormats;
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

    private static int idLivroInfo = 0;

    public static void setIdLivroInfo(int idLivroInfo) {
        MainInterfaceAdminController.idLivroInfo = idLivroInfo;
    }

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

        for(var genre : fsm.getData().getAllFiltersGenres()) {
            vBoxFiltersGenre.getChildren().add(new CheckBox(genre));
        }

        for(var language : fsm.getData().getAllFiltersLanguages()) {
            vBoxFiltersLanguage.getChildren().add(new CheckBox(language));
        }

        for(var format : fsm.getData().getAllFiltersFormats()) {
            vBoxFiltersBookFormats.getChildren().add(new CheckBox(format));
        }
    }

    private void registerHandlers() {
        fsm.addPropertyChangeListener(DiliContext.PROP_FASE, evt -> update());
        fsm.addPropertyChangeListener(DiliContext.PROP_BOOK, evt -> update());
        fsm.addPropertyChangeListener(DiliContext.PROP_USER, evt -> update());

        tfSearch.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
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

                paneItems.getChildren().add(node);
                spbic.initData(fsm.getData().search(tfSearch.getText()));

                clearFilters();
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

        //TODO: statistics
        miStatistics.setOnAction(ev -> System.out.println("miStatistics"));

        btnLogout.setOnAction(ev -> fsm.logout());

        for(Node checkbox : vBoxFiltersLanguage.getChildren()){
            CheckBox cb = (CheckBox) checkbox;
            cb.selectedProperty().addListener(change -> updateByFilters());
        }

        for(Node checkbox : vBoxFiltersGenre.getChildren()){
            CheckBox cb = (CheckBox) checkbox;
            cb.selectedProperty().addListener(change -> updateByFilters());
        }

        for(Node checkbox : vBoxFiltersBookFormats.getChildren()){
            CheckBox cb = (CheckBox) checkbox;
            cb.selectedProperty().addListener(change -> updateByFilters());
        }
    }

    private void updateByFilters() {
        List<String> languageFilters = new ArrayList<>();
        List<String> genreFilters = new ArrayList<>();
        List<String> bookFormatsFilters = new ArrayList<>();

        for(Node checkbox : vBoxFiltersLanguage.getChildren()){
            CheckBox cb = (CheckBox) checkbox;
            if(cb.isSelected())
                languageFilters.add(cb.getText());
        }

        for(Node checkbox : vBoxFiltersGenre.getChildren()){
            CheckBox cb = (CheckBox) checkbox;
            if(cb.isSelected())
                genreFilters.add(cb.getText());
        }

        for(Node checkbox : vBoxFiltersBookFormats.getChildren()){
            CheckBox cb = (CheckBox) checkbox;
            if(cb.isSelected())
                bookFormatsFilters.add(cb.getText());
        }

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

        paneItems.getChildren().add(node);

        if(!genreFilters.isEmpty() || !languageFilters.isEmpty() || !bookFormatsFilters.isEmpty())
            spbic.initData(fsm.getData().listByFilters(genreFilters, languageFilters, bookFormatsFilters));
        else
            spbic.initData(fsm.getData().search(""));

    }

    private void clearFilters(){
        for(Node checkbox : vBoxFiltersLanguage.getChildren()){
            CheckBox cb = (CheckBox) checkbox;
            cb.setSelected(false);
        }

        for(Node checkbox : vBoxFiltersGenre.getChildren()){
            CheckBox cb = (CheckBox) checkbox;
            cb.setSelected(false);
        }

        for(Node checkbox : vBoxFiltersBookFormats.getChildren()){
            CheckBox cb = (CheckBox) checkbox;
            cb.setSelected(false);
        }
    }

    private void update() {
        borderPane.setVisible(fsm != null && (
                        fsm.getState() == DiliState.MAIN_INTERFACE
                                || fsm.getState() == DiliState.PROFILE
                                || fsm.getState() == DiliState.BOOK_INFO
                )
                && DiLi.getLoggedAccount() != null && DiLi.getLoggedAccount().getTypeUser() == UserType.LIBRARIAN
        );

        if (DiLi.getLoggedAccount() != null)
            lbUsername.setText(DiLi.getLoggedAccount().getName());

        if (fsm.getState() == DiliState.MAIN_INTERFACE) {

            idLivroInfo = 0;

            clearFilters();

            updateByFilters();
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

        if (fsm.getState() == DiliState.BOOK_INFO) {
            if (paneItems.getChildren().size() > 0)
                paneItems.getChildren().remove(0);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../fxml/mainInterface/admin/bookInfoAdmin.fxml"));

            Node node;
            try {
                node = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            BookInfoAdminController spbic = loader.getController();

            paneItems.getChildren().add(node);
            spbic.initData(fsm.getData().getBookById(idLivroInfo));
        }
    }
}

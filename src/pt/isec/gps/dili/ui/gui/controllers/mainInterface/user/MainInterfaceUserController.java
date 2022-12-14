package pt.isec.gps.dili.ui.gui.controllers.mainInterface.user;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import pt.isec.gps.dili.model.data.DiLi;
import pt.isec.gps.dili.model.data.book.Book;
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

public class MainInterfaceUserController implements Initializable {

    public VBox vBoxLeft;
    public Button btnSearch;
    @FXML
    private VBox vBoxFiltersLanguage;
    @FXML
    private VBox vBoxFiltersGenre;
    @FXML
    private VBox vBoxFiltersBookFormats;
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

    private static int idLivroInfo = 0;

    public static void setIdLivroInfo(int idLivroInfo) {
        MainInterfaceUserController.idLivroInfo = idLivroInfo;
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
        btnSearch.setGraphic(ImageManager.getImageView("search.png", 20));
    }

    private void registerHandlers() {
        fsm.addPropertyChangeListener(DiliContext.PROP_FASE, evt -> update());
        fsm.addPropertyChangeListener(DiliContext.PROP_BOOK, evt -> update());
        fsm.addPropertyChangeListener(DiliContext.PROP_USER, evt -> update());

        btnSearch.setOnAction(ev -> {
            if (fsm.getState() != DiliState.MAIN_INTERFACE)
                fsm.changeState(DiliState.MAIN_INTERFACE.createState(fsm, fsm.getData()));

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
        });

        tfSearch.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                btnSearch.fire();
            }
        });

        btnLogout.setOnAction(ev -> fsm.logout());
    }

    private void updateByFilters() {

        if (fsm.getState() != DiliState.MAIN_INTERFACE)
            fsm.changeState(DiliState.MAIN_INTERFACE.createState(fsm, fsm.getData()));

        List<String> languageFilters = new ArrayList<>();
        List<String> genreFilters = new ArrayList<>();
        List<String> bookFormatsFilters = new ArrayList<>();

        for (Node checkbox : vBoxFiltersLanguage.getChildren()) {
            CheckBox cb = (CheckBox) checkbox;
            if (cb.isSelected())
                languageFilters.add(cb.getText());
        }

        for (Node checkbox : vBoxFiltersGenre.getChildren()) {
            CheckBox cb = (CheckBox) checkbox;
            if (cb.isSelected())
                genreFilters.add(cb.getText());
        }

        for (Node checkbox : vBoxFiltersBookFormats.getChildren()) {
            CheckBox cb = (CheckBox) checkbox;
            if (cb.isSelected())
                bookFormatsFilters.add(cb.getText());
        }

        ArrayList<Book> livros;

        if (!genreFilters.isEmpty() || !languageFilters.isEmpty() || !bookFormatsFilters.isEmpty()) {
            tfSearch.setText("");
            livros = fsm.getData().listByFilters(genreFilters, languageFilters, bookFormatsFilters);
        } else if (tfSearch.getText().isEmpty())
            livros = fsm.getData().search("");
        else
            return;


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

        spbic.initData(livros);

    }

    private void clearFilters() {
        for (Node checkbox : vBoxFiltersLanguage.getChildren()) {
            CheckBox cb = (CheckBox) checkbox;
            cb.setSelected(false);
        }

        for (Node checkbox : vBoxFiltersGenre.getChildren()) {
            CheckBox cb = (CheckBox) checkbox;
            cb.setSelected(false);
        }

        for (Node checkbox : vBoxFiltersBookFormats.getChildren()) {
            CheckBox cb = (CheckBox) checkbox;
            cb.setSelected(false);
        }
    }

    private void update() {
        borderPane.setVisible(fsm != null &&
                (fsm.getState() == DiliState.MAIN_INTERFACE || fsm.getState() == DiliState.BOOK_INFO)

                && DiLi.getLoggedAccount() != null && DiLi.getLoggedAccount().getTypeUser() == UserType.STUDENT_TEACHER);

        if (DiLi.getLoggedAccount() != null)
            lbUsername.setText(DiLi.getLoggedAccount().getName());

        if (fsm.getState() == DiliState.MAIN_INTERFACE) {
            idLivroInfo = 0;

            vBoxLeft.getChildren().clear();
            vBoxFiltersGenre = new VBox();
            vBoxFiltersLanguage = new VBox();
            vBoxFiltersBookFormats = new VBox();

            Accordion accordion = new Accordion();
            TitledPane tpLanguage = new TitledPane();
            TitledPane tpGenre = new TitledPane();
            TitledPane tpBookFormats = new TitledPane();

            tpLanguage.setText("Language");
            tpGenre.setText("Genre");
            tpBookFormats.setText("Book Format");

            vBoxFiltersGenre.setSpacing(10);
            vBoxFiltersGenre.setPrefWidth(100);

            for (var genre : fsm.getData().getAllFiltersGenres()) {
                vBoxFiltersGenre.getChildren().add(new CheckBox(genre));
            }

            vBoxFiltersLanguage.setSpacing(10);
            vBoxFiltersLanguage.setPrefWidth(100);
            for (var language : fsm.getData().getAllFiltersLanguages()) {
                vBoxFiltersLanguage.getChildren().add(new CheckBox(language));
            }

            vBoxFiltersBookFormats.setSpacing(10);
            vBoxFiltersBookFormats.setPrefWidth(100);
            for (var format : fsm.getData().getAllFiltersFormats()) {
                vBoxFiltersBookFormats.getChildren().add(new CheckBox(format));
            }

            tpLanguage.setContent(vBoxFiltersLanguage);
            tpGenre.setContent(vBoxFiltersGenre);
            tpBookFormats.setContent(vBoxFiltersBookFormats);

            accordion.getPanes().addAll(tpLanguage, tpGenre, tpBookFormats);

            Label lbFilters = new Label("Filters:");
            lbFilters.setFont(new Font(14));

            vBoxLeft.getChildren().addAll(lbFilters, accordion);

            for (Node checkbox : vBoxFiltersLanguage.getChildren()) {
                CheckBox cb = (CheckBox) checkbox;
                cb.selectedProperty().addListener(change -> updateByFilters());
            }

            for (Node checkbox : vBoxFiltersGenre.getChildren()) {
                CheckBox cb = (CheckBox) checkbox;
                cb.selectedProperty().addListener(change -> updateByFilters());
            }

            for (Node checkbox : vBoxFiltersBookFormats.getChildren()) {
                CheckBox cb = (CheckBox) checkbox;
                cb.selectedProperty().addListener(change -> updateByFilters());
            }
            clearFilters();
            updateByFilters();
        }

        if (fsm.getState() == DiliState.BOOK_INFO) {
            if (paneItems.getChildren().size() > 0)
                paneItems.getChildren().remove(0);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../fxml/mainInterface/user/bookInfoUser.fxml"));

            Node node;
            try {
                node = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            BookInfoUserController spbuc = loader.getController();

            paneItems.getChildren().add(node);
            spbuc.initData(fsm.getData().getBookById(idLivroInfo));
        }
    }
}

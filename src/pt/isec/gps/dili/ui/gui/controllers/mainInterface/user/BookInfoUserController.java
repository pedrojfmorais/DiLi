package pt.isec.gps.dili.ui.gui.controllers.mainInterface.user;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pt.isec.gps.dili.model.data.book.Book;
import pt.isec.gps.dili.model.fsm.DiliContext;
import pt.isec.gps.dili.model.fsm.DiliState;
import pt.isec.gps.dili.ui.gui.controllers.mainInterface.admin.AddBookController;
import pt.isec.gps.dili.ui.gui.resources.ImageManager;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class BookInfoUserController implements Initializable {
    public Button btnVoltar;
    public ImageView ivBookImage;
    public Label lbTitle;
    public Label lbAuthor;
    public Label lbGenre;
    public Label lbLanguage;
    public Label lbSynopsis;
    public Button btnDownload;
    public BorderPane bookInfoUser;
    public Label lbNumberDownloads;
    public Label lbRating;
    public Label lbReviews;
    public ImageView ivStars;
    public Button btnRating;
    private DiliContext fsm;
    private Book livro;

    public void initData(Book livro) {
        this.livro = livro;
        update();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bookInfoUser.sceneProperty().addListener((observableValue, oldScene, newScene) -> {
            if (newScene != null)
                fsm = (DiliContext) newScene.getUserData();

            if (fsm != null) {
                createViews();
                registerHandlers();
            }
        });
    }

    private void createViews() {
        btnVoltar.setGraphic(ImageManager.getImageView("left_arrow.png", 20));
    }

    private void registerHandlers() {
        fsm.addPropertyChangeListener(DiliContext.PROP_FASE, evt -> update());
        fsm.addPropertyChangeListener(DiliContext.PROP_BOOK, evt -> update());

        btnVoltar.setOnAction(ev -> fsm.voltar());
        btnRating.setOnAction(ev -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../fxml/mainInterface/user/addRating.fxml"));
            Stage dialog = new Stage();
            Scene scene = new Scene(new Pane());
            scene.setUserData(fsm);
            try {
                scene.setRoot(loader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            AddRatingController arc = loader.getController();
            dialog.setScene(scene);

            dialog.initStyle(StageStyle.UNDECORATED);
            dialog.getIcons().add(ImageManager.getImage("logo.png"));
            dialog.initOwner(bookInfoUser.getScene().getWindow());
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.getScene().setUserData(fsm);
            dialog.show();
            arc.initData(livro);
        });
    }

    private void update() {

        bookInfoUser.setVisible(fsm != null && fsm.getState() == DiliState.BOOK_INFO);


        if (fsm != null && fsm.getState() == DiliState.BOOK_INFO && livro != null) {
            lbTitle.setText(livro.getTitle());
            lbAuthor.setText(livro.getAuthor());
            lbLanguage.setText(livro.getLanguage());

            lbSynopsis.setText(livro.getSynopsis());

            ivBookImage.setImage(ImageManager.getImage(livro.getImagePath()));

            StringBuilder sb = new StringBuilder();
            for (var genre : livro.getGenres())
                sb.append(genre).append(", ");
            if (sb.toString().length() > 2)
                lbGenre.setText(sb.substring(0, sb.length() - 2));
            else
                lbGenre.setText(sb.toString());

            lbNumberDownloads.setText(String.valueOf(fsm.getData().getBookDownloadCounter(livro.getId())));

            lbReviews.setText(fsm.getData().getReviewCounter(livro.getId()) + " Reviews");

            double rating = fsm.getData().getRating(livro.getId());
            lbRating.setText(new DecimalFormat("0.0").format(rating) + "/5.0");

            ivStars.setImage(ImageManager.getImage("rating" + (int) rating + ".png"));
        }
    }
}

package pt.isec.gps.dili.ui.gui.controllers.mainInterface.admin;

import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pt.isec.gps.dili.model.data.Message;
import pt.isec.gps.dili.model.data.MessageType;
import pt.isec.gps.dili.model.data.book.Book;
import pt.isec.gps.dili.model.fsm.DiliContext;
import pt.isec.gps.dili.ui.gui.resources.ImageManager;

import java.io.File;
import java.net.URL;
import java.util.*;

public class AddBookController implements Initializable {
    public BorderPane addBook;
    public TextField tfTitle;
    public TextField tfAuthor;
    public VBox genreVBox;
    public TextField tfGenre;
    public Button btnAddGenre;
    public TextField tfLanguage;
    public TextArea taSynopsis;

    public VBox downloadVBox;
    public HBox downloadHBox;
    public ComboBox<String> cbBookFormat;

    public TextField tfLink;
    public Button btnAddBookFormat;
    public TextField tfPrice;
    public Button btnConfirm;
    public Button btnCancel;
    public CheckBox cbAvailability;
    public ImageView ivImage;
    public Button btnSelectImage;
    private DiliContext fsm;

    private String imagePath = "book_generic.png";
    private String type = "new";
    private int idLivro = 0;

    public void initData(Book livro) {

        idLivro = livro.getId();

        tfTitle.setText(livro.getTitle());
        tfAuthor.setText(livro.getAuthor());
        tfLanguage.setText(livro.getLanguage());
        tfPrice.setText(String.valueOf(livro.getCostPerDownload()));

        taSynopsis.setText(livro.getSynopsis());

        cbAvailability.setSelected(livro.isAvailability());

        imagePath = livro.getImagePath();

        Image img = ImageManager.getImage(livro.getImagePath());

        if (img == null)
            ImageManager.getExternalImage(livro.getImagePath());

        if (img == null)
            img = ImageManager.getImage("book_generic.png");

        ivImage.setImage(img);

        if(livro.getGenres().size() > 0) {
            tfGenre.setText(livro.getGenres().get(0));

            for (int i = 1; i < livro.getGenres().size(); i++) {
                addGenreTextField();
                HBox hBox = (HBox) genreVBox.getChildren().get(genreVBox.getChildren().size() - 1);
                ((TextField) hBox.getChildren().get(0)).setText(livro.getGenres().get(i));
            }
        }

        boolean first = true;
        for(var chave : livro.getDownloadFiles().keySet()){
            if(first){
                cbBookFormat.setValue(chave.toUpperCase());
                tfLink.setText(livro.getDownloadFiles().get(chave));
                first = false;
            }else{
                addDownloadBookFormat();
                HBox hBox = (HBox) downloadVBox.getChildren().get(downloadVBox.getChildren().size() - 1);
                ((ComboBox) hBox.getChildren().get(0)).setValue(chave.toUpperCase());
                ((TextField) hBox.getChildren().get(1)).setText(livro.getDownloadFiles().get(chave));
            }
        }

        type = "update";
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addBook.sceneProperty().addListener((observableValue, oldScene, newScene) -> {
            fsm = (DiliContext) newScene.getUserData();
            createViews();
            registerHandlers();
        });
        cbBookFormat.getSelectionModel().selectFirst();
        // ltfGenre = new ArrayList<>();
    }

    private void createViews() {
        btnAddGenre.setGraphic(ImageManager.getImageView("add_icon.png", 20));
        btnAddBookFormat.setGraphic(ImageManager.getImageView("add_icon.png", 20));
        ivImage.setImage(ImageManager.getImage(imagePath));
    }

    private void registerHandlers() {

        btnCancel.setOnAction(ev ->{
            Stage stage = (Stage) addBook.getScene().getWindow();
            stage.close();
        });

        btnAddGenre.setOnAction(ev -> addGenreTextField());
        btnAddBookFormat.setOnAction(ev -> addDownloadBookFormat());

        btnSelectImage.setOnAction(ev -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load Image ...");
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All", "*")
            );

            File hFile = fileChooser.showOpenDialog(addBook.getScene().getWindow());

            if (hFile != null) {
                imagePath = hFile.getAbsolutePath();
                ivImage.setImage(ImageManager.getExternalImage(imagePath));
            }
        });

        btnConfirm.setOnAction(ev -> {

            List<String> genres = new ArrayList<>();
            Map<String, String> downloadLink = new HashMap<>();

            // Vai buscar todos os genres
            for (Node node : genreVBox.getChildren()) {
                if (node instanceof HBox hBox) {
                    if (hBox.getChildren().get(0) instanceof TextField tf) {
                        if (!tf.getText().isBlank())
                            genres.add(tf.getText());
                    }
                }
            }

            // Vai buscar todos os formats e links
            // Vem sempre agrupados, primeiro o formato, depois o link
            for (Node hbox : downloadVBox.getChildren()) {
                if (hbox instanceof HBox hBox) {
                    if (hBox.getChildren().get(0) instanceof ComboBox comboB
                            && hBox.getChildren().get(1) instanceof TextField textf) {

                        if (!textf.getText().isBlank())
                            downloadLink.put((String) comboB.getSelectionModel().getSelectedItem(), textf.getText());
                    }
                }
            }

            Message message = null;
            if (type.equals("new"))
                message = fsm.addBook(tfTitle.getText(), tfAuthor.getText(), taSynopsis.getText(),
                        tfLanguage.getText(), genres, cbAvailability.isSelected(),
                        Double.parseDouble(tfPrice.getText().isBlank() ? "0.0" : tfPrice.getText()),
                        downloadLink, imagePath);

            else if (type.equals("update"))
                message = fsm.updateBookInfo(idLivro, tfTitle.getText(), tfAuthor.getText(), taSynopsis.getText(),
                        tfLanguage.getText(), genres, cbAvailability.isSelected(),
                        Double.parseDouble(tfPrice.getText().isBlank() ? "0.0" : tfPrice.getText()),
                        downloadLink, imagePath);

            if (message == null)
                return;

            if (message.getType() == MessageType.SUCCESS) {
                Alert alert = new Alert(
                        Alert.AlertType.INFORMATION,
                        "",
                        ButtonType.OK
                );
                alert.setTitle(message.getField());
                alert.setHeaderText(message.getMessage());

                alert.showAndWait().ifPresent(response -> {
                    Stage stage = (Stage) addBook.getScene().getWindow();
                    stage.close();
                });
            } else {

                Alert alert = new Alert(
                        Alert.AlertType.ERROR,
                        "",
                        ButtonType.OK
                );
                alert.setTitle("Error");
                alert.setHeaderText("Field: " + message.getField());
                alert.setContentText(message.getMessage());

                alert.showAndWait();
            }
        });
    }

    private void addGenreTextField(){
        if (genreVBox.getChildren().size() < 5) {
            TextField tf = new TextField();
            tf.prefWidth(tfGenre.getPrefWidth()); // Não consigo meter a ficar com o tamanho que quero
            tf.setPromptText("Genre");

            HBox newHBox = new HBox();
            newHBox.setSpacing(10);
            newHBox.setAlignment(Pos.CENTER);
            newHBox.getChildren().add(tf);

            genreVBox.getChildren().add(newHBox);
            Stage stage = (Stage) addBook.getScene().getWindow();
            stage.setHeight(stage.getHeight() + genreVBox.getHeight() / (genreVBox.getChildren().size() - 1) + 2);
        }
    }

    private void addDownloadBookFormat(){
        if (downloadVBox.getChildren().size() < 4) {
            ComboBox<String> cbFormat = new ComboBox<>();
            cbFormat.getItems().add("PDF");
            cbFormat.getItems().add("EPUB");
            cbFormat.getItems().add("MOBI");
            cbFormat.getItems().add("AZW");
            cbFormat.getSelectionModel().selectFirst();

            TextField tf = new TextField();
            tf.setPromptText("Link");

            HBox hboxCopy = new HBox();

            hboxCopy.setSpacing(10);
            hboxCopy.setAlignment(Pos.CENTER);
            hboxCopy.getChildren().addAll(cbFormat, tf);

            downloadVBox.getChildren().add(hboxCopy); // Não consigo meter a ficar com o tamanho que quero

            Stage stage = (Stage) addBook.getScene().getWindow();
            stage.setHeight(stage.getHeight() + downloadVBox.getHeight() / (downloadVBox.getChildren().size() - 1) + 2);
        }
    }
}

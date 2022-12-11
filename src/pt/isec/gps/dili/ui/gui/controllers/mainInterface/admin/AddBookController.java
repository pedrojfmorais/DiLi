package pt.isec.gps.dili.ui.gui.controllers.mainInterface.admin;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.w3c.dom.Text;
import pt.isec.gps.dili.model.fsm.DiliContext;
import pt.isec.gps.dili.ui.gui.resources.ImageManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddBookController implements Initializable {
    public BorderPane addBook;
    public TextField tfTitle;
    public TextField tfAuthor;
    public VBox genreVBox;
    public TextField tfGenre;
    //public List<TextField> ltfGenre;
    public Button btnAddGenre;
    public TextField tfLanguage;
    public TextArea tfSynopsis;

    public VBox downloadVBox;
    public HBox downloadHBox;
    public ComboBox<String> cbBookFormat;

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
        cbBookFormat.getSelectionModel().selectFirst();
        // ltfGenre = new ArrayList<>();
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
        btnAddGenre.setOnAction(ev -> {
            //System.out.println(tfGenre.getText());
            TextField tf = new TextField();
            tf.prefWidth(tfGenre.getPrefWidth()); // Não consigo meter a ficar com o tamanho que quero
            tf.setPromptText("Genre");
            // ltfGenre.add(tf);
            genreVBox.getChildren().add(tf);

            // Quando adicionas vários, não tem scroll e fica tudo fodido

        });
        btnAddBookFormat.setOnAction(ev -> {
            // Mudar o textField do format para um combobox
            ComboBox<String> cbFormat = new ComboBox<>();
            cbFormat.getItems().add("PDF");
            cbFormat.getItems().add("EPUB");
            cbFormat.getItems().add("MOBI");
            cbFormat.getItems().add("AZW");
            cbFormat.getSelectionModel().selectFirst();

            TextField tf = new TextField();
            tf.setPromptText("Link");
            HBox hboxCopy = new HBox();
            hboxCopy.getChildren().addAll(cbFormat, tf);
            downloadVBox.getChildren().add(hboxCopy); // Não consigo meter a ficar com o tamanho que quero


        });
        btnConfirm.setOnAction(ev -> {
            // Vai buscar todos os genres
            for(Node node : genreVBox.getChildren()) {
                if(node instanceof TextField) {
                    System.out.println(((TextField)node).getText());
                }
            }

            // Vai buscar todos os formats e links
            // Vem sempre agrupados, primeiro o formato, depois o link
            for(Node hbox : downloadVBox.getChildren()) {
                if(hbox instanceof HBox) {
                    for(Node node : ((HBox)hbox).getChildren()) {
                        if (node instanceof ComboBox comboB) {
                            System.out.println(comboB.getSelectionModel().getSelectedItem()); // Format
                        }
                        if (node instanceof TextField textf) {
                            System.out.println(textf.getText()); // Link
                        }
                    }

                }
            }
            // System.out.println(fsm.getState()); // Dá erro aqui porque o fsm é null
        });
    }
}

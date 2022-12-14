package pt.isec.gps.dili.ui.gui.controllers.mainInterface.user;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pt.isec.gps.dili.model.data.Message;
import pt.isec.gps.dili.model.data.MessageType;
import pt.isec.gps.dili.model.data.book.Book;
import pt.isec.gps.dili.model.fsm.DiliContext;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class ChooseDownloadController implements Initializable {

    public BorderPane chooseDownload;
    public ComboBox cbFormat;
    public Label lbLink;
    public Button btnConfirm;
    public Button btnCancel;
    private DiliContext fsm;
    private Book livro;
    Map<String, String> formatoLinks;

    public void initData(Book livro) {
        this.livro = livro;

        formatoLinks = fsm.getData().getBookDownloadFile(livro.getId());
        cbFormat.setValue("Choose extension ...");
        cbFormat.setItems(FXCollections.observableArrayList(formatoLinks.keySet()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        chooseDownload.sceneProperty().addListener((observableValue, oldScene, newScene) -> {
            fsm = (DiliContext) newScene.getUserData();
            registerHandlers();
        });
    }

    private void registerHandlers() {

        btnCancel.setOnAction(ev -> {
            Stage stage = (Stage) chooseDownload.getScene().getWindow();
            stage.close();
        });


        btnConfirm.setOnAction(ev -> {

            if (!cbFormat.getItems().contains(cbFormat.getSelectionModel().getSelectedItem()))
                return;

            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {
                    Desktop.getDesktop().browse(new URI(livro.getDownloadFiles().get(cbFormat.getSelectionModel().getSelectedItem())));
                } catch (IOException | URISyntaxException e) {
                    Alert alert = new Alert(
                            Alert.AlertType.ERROR,
                            "",
                            ButtonType.OK
                    );

                    alert.setTitle("Error download book");
                    alert.setHeaderText("Book: " + livro.getTitle() + ", Format: " + cbFormat.getSelectionModel().getSelectedItem());

                    alert.setContentText("Incorrect link\n" + livro.getDownloadFiles().get(cbFormat.getSelectionModel().getSelectedItem()));

                    alert.showAndWait();

                    return;
                }
            }

            Message message = fsm.downloadBook(livro, (String) cbFormat.getSelectionModel().getSelectedItem());

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
                    Stage stage = (Stage) chooseDownload.getScene().getWindow();
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

        cbFormat.getSelectionModel().selectedItemProperty().addListener(
                (ChangeListener<String>) (ov, oldValue, newValue) -> lbLink.setText(formatoLinks.get(newValue)));
    }
}

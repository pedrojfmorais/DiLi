package pt.isec.gps.dili.ui.gui.controllers.mainInterface.user;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pt.isec.gps.dili.model.data.Message;
import pt.isec.gps.dili.model.data.MessageType;
import pt.isec.gps.dili.model.data.book.Book;
import pt.isec.gps.dili.model.fsm.DiliContext;

import java.net.URL;
import java.util.ResourceBundle;

public class AddRatingController implements Initializable {
    public BorderPane addReview;
    public Slider sliderRating;
    public Label lbRating;
    public TextArea taReview;
    public Button btnConfirm;
    public Button btnCancel;
    private DiliContext fsm;
    private Book livro;

    public void initData(Book livro){
        this.livro = livro;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addReview.sceneProperty().addListener((observableValue, oldScene, newScene) -> {
            fsm = (DiliContext) newScene.getUserData();
            createViews();
            registerHandlers();
        });
    }

    private void createViews() {
        lbRating.setText(Math.round(sliderRating.getValue()) + " stars");
    }

    private void registerHandlers() {

        btnCancel.setOnAction(ev ->{
            Stage stage = (Stage) addReview.getScene().getWindow();
            stage.close();
        });

        sliderRating.valueProperty().addListener((observableValue, oldValue, newValue) ->
                lbRating.setText(Math.round((double) newValue) + " stars"));

        btnConfirm.setOnAction(ev -> {

            Message message;

            message = fsm.addReview(livro, (int) sliderRating.getValue(), taReview.getText());

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
                    Stage stage = (Stage) addReview.getScene().getWindow();
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
}

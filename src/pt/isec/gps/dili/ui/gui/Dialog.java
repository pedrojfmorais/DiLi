package pt.isec.gps.dili.ui.gui;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pt.isec.gps.dili.model.data.Message;

public class Dialog {

    static Stage dialog;

    public static void showConfirmAlert(String title, Message message){

        Alert.AlertType alertType = Alert.AlertType.WARNING;

        switch (message.getType()){

            case SUCCESS -> alertType = Alert.AlertType.CONFIRMATION;
            case ERROR -> alertType = Alert.AlertType.ERROR;
        }

        Alert alert = new Alert(
                alertType,
                "",
                ButtonType.OK
        );
        alert.setTitle(title);
        alert.setHeaderText(message.getMessage());

        alert.showAndWait();
    }
}

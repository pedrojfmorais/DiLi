import javafx.application.Application;
import pt.isec.gps.dili.ui.gui.MainJFX;

public class Main {

    public static void main(String[] args) {

        //WARNING:
        // VM OPTIONS
        //  --module-path "C:\Program Files\Java\javafx-sdk-17.0.2\lib" --add-modules javafx.controls,javafx.fxml
        Application.launch(MainJFX.class, args);
    }
}
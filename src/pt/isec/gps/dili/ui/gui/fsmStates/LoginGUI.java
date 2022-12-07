package pt.isec.gps.dili.ui.gui.fsmStates;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pt.isec.gps.dili.model.data.Message;
import pt.isec.gps.dili.model.data.MessageType;
import pt.isec.gps.dili.model.fsm.DiliContext;
import pt.isec.gps.dili.model.fsm.DiliState;
import pt.isec.gps.dili.ui.gui.Dialog;

public class LoginGUI  extends BorderPane {

    DiliContext fsm;

    Button btnLogin, btnExit;
    TextField tfEmail, tfPassword;

    public LoginGUI(DiliContext fsm) {
        this.fsm = fsm;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        btnLogin = new Button("LOG IN");
        btnExit = new Button("EXIT");

        tfEmail = new TextField();
        tfPassword = new TextField();

        tfEmail.setPromptText("Email");
        tfPassword.setPromptText("Password");

        tfEmail.setMaxSize(200, 75);
        tfPassword.setMaxSize(200, 75);

        btnLogin.setPrefWidth(75);
        btnExit.setPrefWidth(75);

        VBox vBoxExterior = new VBox();
        vBoxExterior.setAlignment(Pos.CENTER);
        vBoxExterior.setSpacing(10);
        vBoxExterior.setPadding(new Insets(10));

        VBox vBoxTextFields =  new VBox(tfEmail, tfPassword);
        vBoxTextFields.setAlignment(Pos.CENTER);
        vBoxTextFields.setSpacing(10);
        vBoxTextFields.setPadding(new Insets(10));

        HBox hBoxButtons =  new HBox(btnLogin, btnExit);
        hBoxButtons.setAlignment(Pos.CENTER);
        hBoxButtons.setSpacing(10);
        hBoxButtons.setPadding(new Insets(10));

        vBoxExterior.getChildren().addAll(
                vBoxTextFields,
                hBoxButtons
        );

        this.setCenter(vBoxExterior);
    }

    private void registerHandlers() {
        fsm.addPropertyChangeListener(DiliContext.PROP_FASE, evt -> update());

        //TODO: dialog message
        btnLogin.setOnAction(ev -> {
            Message result = fsm.login(tfEmail.getText(), tfPassword.getText());
            Dialog.showConfirmAlert("Login", result);

            if(result.getType() == MessageType.SUCCESS)
                //TODO: passa para o próximo
                System.out.println("ACERTOU");
        });

        btnExit.setOnAction(ev -> Platform.exit());
    }

    private void update() {
        this.setVisible(fsm != null && fsm.getState() == DiliState.LOGIN);
    }
}

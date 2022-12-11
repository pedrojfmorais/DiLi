package pt.isec.gps.dili.model.fsm.concreteStates;

import pt.isec.gps.dili.model.data.DiLi;
import pt.isec.gps.dili.model.data.Message;
import pt.isec.gps.dili.model.data.MessageType;
import pt.isec.gps.dili.model.fsm.DiliContext;
import pt.isec.gps.dili.model.fsm.DiliState;

public class LoginState extends DiliAdapter{
    public LoginState(DiliContext context, DiLi data) {
        super(context, data);
    }

    @Override
    public Message login(String email, String password) {
        Message message = data.authenticate(email, password);

        if(message.getType() == MessageType.SUCCESS)
            changeState(DiliState.MAIN_INTERFACE);

        return message;
    }

    @Override
    public DiliState getState() {
        return DiliState.LOGIN;
    }
}

package pt.isec.gps.dili.model.fsm.concreteStates;

import pt.isec.gps.dili.model.data.DiLi;
import pt.isec.gps.dili.model.fsm.DiliContext;
import pt.isec.gps.dili.model.fsm.DiliState;

public class LoginState extends DiliAdapter{
    public LoginState(DiliContext context, DiLi data) {
        super(context, data);
    }

    @Override
    public DiliState getState() {
        return DiliState.LOGIN;
    }
}

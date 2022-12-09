package pt.isec.gps.dili.model.fsm.concreteStates;

import pt.isec.gps.dili.model.data.DiLi;
import pt.isec.gps.dili.model.fsm.DiliContext;
import pt.isec.gps.dili.model.fsm.DiliState;

public class MainInterfaceAdmin extends DiliAdapter{
    public MainInterfaceAdmin(DiliContext context, DiLi data) {
        super(context, data);
    }

    @Override
    public void logout() {
        data.logout();
        changeState(DiliState.LOGIN);
    }

    @Override
    public DiliState getState() {
        return DiliState.MainInterface;
    }
}

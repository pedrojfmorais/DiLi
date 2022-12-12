package pt.isec.gps.dili.model.fsm.concreteStates;

import pt.isec.gps.dili.model.data.DiLi;
import pt.isec.gps.dili.model.data.Message;
import pt.isec.gps.dili.model.fsm.DiliContext;
import pt.isec.gps.dili.model.fsm.DiliState;

public class ProfileState extends DiliAdapter {
    public ProfileState(DiliContext context, DiLi data) {
        super(context, data);
    }

    @Override
    public void voltar() {
        changeState(DiliState.MAIN_INTERFACE);
    }

    @Override
    public Message updateLibrarianInfo(String name, String email, String password) {
        return data.updateLibrarianInfo(name, email, password);
    }

    @Override
    public void logout() {
        new MainInterfaceAdminState(context, data).logout();
    }

    @Override
    public DiliState getState() {
        return DiliState.PROFILE;
    }
}

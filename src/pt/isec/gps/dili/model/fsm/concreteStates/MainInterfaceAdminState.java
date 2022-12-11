package pt.isec.gps.dili.model.fsm.concreteStates;

import pt.isec.gps.dili.model.data.DiLi;
import pt.isec.gps.dili.model.data.Message;
import pt.isec.gps.dili.model.fsm.DiliContext;
import pt.isec.gps.dili.model.fsm.DiliState;

import java.sql.SQLException;

public class MainInterfaceAdminState extends DiliAdapter{
    public MainInterfaceAdminState(DiliContext context, DiLi data) {
        super(context, data);
    }

    @Override
    public void logout() {
        data.logout();
        changeState(DiliState.LOGIN);
    }

    @Override
    public void changeBookVisibility(int idLivro) {
        data.changeBookVisibility(idLivro);
    }

    @Override
    public Message createLibrarian(String name, String email, String password) {
        try {
            return data.createLibrarian(name, email, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public DiliState getState() {
        return DiliState.MAIN_INTERFACE;
    }
}

package pt.isec.gps.dili.model.fsm;

import pt.isec.gps.dili.model.data.DiLi;
import pt.isec.gps.dili.model.fsm.concreteStates.LoginState;
import pt.isec.gps.dili.model.fsm.concreteStates.MainInterfaceAdmin;

public enum DiliState {

    LOGIN,
    MainInterface;

    public IDiliState createState(DiliContext context, DiLi data){
        return switch (this){
            case LOGIN -> new LoginState(context, data);
            case MainInterface -> new MainInterfaceAdmin(context, data);
        };
    }

}


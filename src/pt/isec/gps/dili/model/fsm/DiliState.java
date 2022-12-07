package pt.isec.gps.dili.model.fsm;

import pt.isec.gps.dili.model.data.DiLi;
import pt.isec.gps.dili.model.fsm.concreteStates.LoginState;

public enum DiliState {

    LOGIN;

    public IDiliState createState(DiliContext context, DiLi data){
        return switch (this){
            case LOGIN -> new LoginState(context, data);
        };
    }

}


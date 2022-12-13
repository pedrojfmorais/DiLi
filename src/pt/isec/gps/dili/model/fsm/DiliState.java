package pt.isec.gps.dili.model.fsm;

import pt.isec.gps.dili.model.data.DiLi;
import pt.isec.gps.dili.model.fsm.concreteStates.BookInfoState;
import pt.isec.gps.dili.model.fsm.concreteStates.LoginState;
import pt.isec.gps.dili.model.fsm.concreteStates.MainInterfaceAdminState;
import pt.isec.gps.dili.model.fsm.concreteStates.ProfileState;

public enum DiliState {

    LOGIN,
    MAIN_INTERFACE,
    PROFILE,
    BOOK_INFO;

    public IDiliState createState(DiliContext context, DiLi data){
        return switch (this){
            case LOGIN -> new LoginState(context, data);
            case MAIN_INTERFACE -> new MainInterfaceAdminState(context, data);
            case PROFILE -> new ProfileState(context, data);
            case BOOK_INFO -> new BookInfoState(context, data);
        };
    }

}


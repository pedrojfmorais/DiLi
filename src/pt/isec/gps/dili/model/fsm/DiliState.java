package pt.isec.gps.dili.model.fsm;

import pt.isec.gps.dili.model.data.DiLi;
import pt.isec.gps.dili.model.fsm.concreteStates.*;

public enum DiliState {

    LOGIN,
    MAIN_INTERFACE,
    PROFILE,
    BOOK_INFO,
    STATISTICS;

    public IDiliState createState(DiliContext context, DiLi data){
        return switch (this){
            case LOGIN -> new LoginState(context, data);
            case MAIN_INTERFACE -> new MainInterfaceAdminState(context, data);
            case PROFILE -> new ProfileState(context, data);
            case BOOK_INFO -> new BookInfoState(context, data);
            case STATISTICS -> new StatisticsState(context, data);
        };
    }

}


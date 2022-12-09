package pt.isec.gps.dili.model.fsm;

import pt.isec.gps.dili.model.data.Message;

public interface IDiliState {

    Message login(String email, String password);
    void logout();

    DiliState getState();
}

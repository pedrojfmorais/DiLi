package pt.isec.gps.dili.model.fsm;

import pt.isec.gps.dili.model.data.Message;

public interface IDiliState {

    Message login(String email, String password);
    void logout();
    void voltar();
    void changeBookVisibility(int idLivro);
    Message createLibrarian(String name, String email, String password);
    DiliState getState();
}

package pt.isec.gps.dili.model.fsm.concreteStates;

import pt.isec.gps.dili.model.data.DiLi;
import pt.isec.gps.dili.model.data.Message;
import pt.isec.gps.dili.model.fsm.DiliContext;
import pt.isec.gps.dili.model.fsm.DiliState;
import pt.isec.gps.dili.model.fsm.IDiliState;

import java.util.List;
import java.util.Map;

public abstract class DiliAdapter implements IDiliState {
    DiliContext context;
    DiLi data;

    public DiliAdapter(DiliContext context, DiLi data){
        this.context = context;
        this.data = data;
    }
    void changeState(DiliState state){context.changeState(state.createState(context, data));}

    @Override
    public Message login(String email, String password) {
        return null;
    }

    @Override
    public void logout() {

    }
    public void voltar() {

    }
    public void changeBookVisibility(int idLivro){

    }

    @Override
    public void removeBook(int idLivro) {

    }

    @Override
    public Message createLibrarian(String name, String email, String password) {
        return null;
    }

    @Override
    public Message updateLibrarianInfo(String name, String email, String password) {
        return null;
    }

    @Override
    public Message addBook(String title, String author, String synopsis, String language, List<String> genres,
                           boolean availability, double costPerDownload, Map<String,
            String> downloadLink, String imagePath) {
        return null;
    }

    @Override
    public Message updateBookInfo(int id, String title, String author, String synopsis, String language,
                                  List<String> genres, boolean availability, double costPerDownload,
                                  Map<String, String> downloadLink, String imagePath) {
        return null;
    }
}

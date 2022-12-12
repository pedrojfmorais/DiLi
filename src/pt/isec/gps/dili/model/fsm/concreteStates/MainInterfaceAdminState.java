package pt.isec.gps.dili.model.fsm.concreteStates;

import pt.isec.gps.dili.model.data.DiLi;
import pt.isec.gps.dili.model.data.Message;
import pt.isec.gps.dili.model.fsm.DiliContext;
import pt.isec.gps.dili.model.fsm.DiliState;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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
    public void removeBook(int idLivro) {
        data.removeBook(idLivro);
    }

    @Override
    public Message createLibrarian(String name, String email, String password) {
        return data.createLibrarian(name, email, password);
    }

    @Override
    public Message addBook(String title, String author, String synopsis, String language,
                    List<String> genres, boolean availability, double costPerDownload,
                    Map<String, String> downloadLink, String imagePath){

        return data.addBook(title, author, synopsis, language, genres, availability,
                costPerDownload, downloadLink, imagePath);

    }

    @Override
    public Message updateBookInfo(int id, String title, String author, String synopsis, String language,
                                  List<String> genres, boolean availability, double costPerDownload,
                                  Map<String, String> downloadLink, String imagePath) {
        return data.updateBookInfo(id, title, author, synopsis, language, genres, availability,
                costPerDownload, downloadLink, imagePath);
    }

    @Override
    public DiliState getState() {
        return DiliState.MAIN_INTERFACE;
    }
}

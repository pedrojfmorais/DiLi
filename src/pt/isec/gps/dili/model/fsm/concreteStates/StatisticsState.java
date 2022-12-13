package pt.isec.gps.dili.model.fsm.concreteStates;

import pt.isec.gps.dili.model.data.DiLi;
import pt.isec.gps.dili.model.data.Message;
import pt.isec.gps.dili.model.fsm.DiliContext;
import pt.isec.gps.dili.model.fsm.DiliState;

import java.util.List;
import java.util.Map;

public class StatisticsState extends DiliAdapter {
    public StatisticsState(DiliContext context, DiLi data) {
        super(context, data);
    }

    @Override
    public void voltar() {
        changeState(DiliState.MAIN_INTERFACE);
    }

    @Override
    public void logout() {
        data.logout();
        changeState(DiliState.LOGIN);
    }
    @Override
    public Message createLibrarian(String name, String email, String password) {
        return new MainInterfaceAdminState(context, data).createLibrarian(name, email, password);
    }

    @Override
    public Message addBook(String title, String author, String synopsis, String language,
                           List<String> genres, boolean availability, double costPerDownload,
                           Map<String, String> downloadLink, String imagePath) {

        return new MainInterfaceAdminState(context, data).addBook(title, author, synopsis, language, genres,
                availability, costPerDownload, downloadLink, imagePath);

    }
    @Override
    public DiliState getState() {
        return DiliState.STATISTICS;
    }
}

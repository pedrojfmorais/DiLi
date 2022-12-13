package pt.isec.gps.dili.model.fsm.concreteStates;

import pt.isec.gps.dili.model.data.DiLi;
import pt.isec.gps.dili.model.data.Message;
import pt.isec.gps.dili.model.fsm.DiliContext;
import pt.isec.gps.dili.model.fsm.DiliState;

import java.util.List;
import java.util.Map;

public class BookInfoState extends DiliAdapter {
    public BookInfoState(DiliContext context, DiLi data) {
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
    public Message updateBookInfo(int id, String title, String author, String synopsis, String language,
                                  List<String> genres, boolean availability, double costPerDownload,
                                  Map<String, String> downloadLink, String imagePath) {
        if (!data.isAdmin())
            return null;
        return data.updateBookInfo(id, title, author, synopsis, language, genres, availability,
                costPerDownload, downloadLink, imagePath);
    }

    @Override
    public DiliState getState() {
        return DiliState.BOOK_INFO;
    }
}

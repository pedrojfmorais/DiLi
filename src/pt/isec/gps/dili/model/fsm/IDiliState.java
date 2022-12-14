package pt.isec.gps.dili.model.fsm;

import pt.isec.gps.dili.model.data.Message;
import pt.isec.gps.dili.model.data.book.Book;

import java.util.List;
import java.util.Map;

public interface IDiliState {

    Message login(String email, String password);
    void logout();
    void voltar();
    void changeBookVisibility(int idLivro);
    void removeBook(int idLivro);
    Message createLibrarian(String name, String email, String password);
    Message updateLibrarianInfo(String name, String email, String password);

    Message addBook(String title, String author, String synopsis, String language,
                    List<String> genres, boolean availability, double costPerDownload,
                    Map<String, String> downloadLink, String imagePath);

    Message updateBookInfo(int id, String title, String author, String synopsis, String language,
                           List<String> genres, boolean availability, double costPerDownload,
                           Map<String, String> downloadLink, String imagePath);

    Message addReview(Book book, int rating, String review);
    Message downloadBook(Book book, String format);
    DiliState getState();
}

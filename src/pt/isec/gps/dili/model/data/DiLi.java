package pt.isec.gps.dili.model.data;

import pt.isec.gps.dili.model.data.book.Book;
import pt.isec.gps.dili.model.data.book.BookPrice;
import pt.isec.gps.dili.model.data.book.Review;
import pt.isec.gps.dili.model.data.user.User;
import pt.isec.gps.dili.model.data.user.UserType;
import pt.isec.gps.dili.model.jdbc.ConnDB;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class DiLi {

    private final ConnDB connDB;
    private static User loggedAccount;

    public DiLi() throws SQLException {
        this.connDB = new ConnDB();
        loggedAccount = null;
    }

    public static User getLoggedAccount() {
        return loggedAccount;
    }

    public Message authenticate(String email, String password) {
        try {
            if (!connDB.verifyLogin(email, password))
                return new Message("password", MessageType.ERROR, "Invalid email or password.");


            loggedAccount = connDB.getUserInformation(email);
        } catch (SQLException e) {
            //TODO: tratar exceção
            throw new RuntimeException(e);
        }
        return new Message(null, MessageType.SUCCESS, "User logged in.");
    }

    public void logout() {
        loggedAccount = null;
    }

    public Message createLibrarian(String name, String email, String password) {
        try {
            Message message = checkUserFields(name, email, password, false);


            if (message.getType().equals(MessageType.ERROR))
                return message;

            connDB.insertLibrarian(email, name, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new Message(null, MessageType.SUCCESS, "Librarian created.");
    }

    public Message checkUserFieldsTest(String name, String email, String password, boolean canEmailExist) throws SQLException {
        return checkUserFields(name, email, password, canEmailExist);
    }

    private Message checkUserFields(String name, String email, String password, boolean canEmailExist) throws SQLException {
        // Check if fields are empty
        if (name.isBlank())
            return new Message("name", MessageType.ERROR, "Empty field.");
        if (email.isBlank())
            return new Message("email", MessageType.ERROR, "Empty field.");
        if (password.isBlank())
            return new Message("password", MessageType.ERROR, "Empty field.");

        // Check if fields have more than 50 chars
        if (name.length() > 50)
            return new Message("name", MessageType.ERROR, "Name is too long.");
        if (email.length() > 50)
            return new Message("email", MessageType.ERROR, "Email is too long.");
        if (password.length() > 50)
            return new Message("password", MessageType.ERROR, "Password is too long.");

        // Check if email is already used
        // if(connDB.getUserInformation(email) != null)
        // if not null > user exists >>>> false
        // canEmailExist = false
        // If user exists, errors
        if (connDB.getUserInformation(email) != null && !canEmailExist)
            return new Message("email", MessageType.ERROR, "Email is already used.");

        // Check if email is of type @domain
        if (!verifyEmailValidity(email))
            return new Message("email", MessageType.ERROR, "Email is invalid. Email should be of type: personal_info@domain.extension");

        // Check password security
        if (!verifyPasswordSecurity(password))
            return new Message("password", MessageType.ERROR, "Password must have: " + System.lineSeparator() +
                    "At least 8 characters." + System.lineSeparator() +
                    "A mixture of both uppercase and lowercase letters." + System.lineSeparator() +
                    "A mixture of letters and numbers." + System.lineSeparator() +
                    "At least one special character, e.g., ! @ # ? ].");
        return new Message(null, MessageType.SUCCESS, "");
    }

    public boolean verifyEmailValidityTest(String email) {
        return verifyEmailValidity(email);
    }

    private boolean verifyEmailValidity(String email) {
        // return Pattern.compile("^(.+)@(.+)$").matcher(email).find();
        return Pattern.compile("[a-z0-9]+@[a-z]+\\.[a-z]{2,3}").matcher(email).find();
    }

    public boolean verifyPasswordSecurityTest(String password) {
        return verifyPasswordSecurity(password);
    }

    private boolean verifyPasswordSecurity(String password) {
        // At least 8 characters.
        // A mixture of both uppercase and lowercase letters.
        // A mixture of letters and numbers.
        // At least one special character, e.g., ! @ # ? ].

        return Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")
                .matcher(password)
                .find();
    }

    public Message updateLibrarianInfoTest(int id, String name, String email, String password) throws SQLException {
        Message message = checkUserFields(name, email, password, true);

        if (message.getType().equals(MessageType.ERROR))
            return message;

        connDB.updateLibrarian(id, name, email, password);
        return new Message(null, MessageType.SUCCESS, "Info updated.");
    }

    public Message updateLibrarianInfo(String name, String email, String password) {
        try {
            Message message = checkUserFields(name, email, password, true);


            if (message.getType().equals(MessageType.ERROR))
                return message;

            connDB.updateLibrarian(loggedAccount.getId(), email, name, password);

            loggedAccount = connDB.getUserInformation(email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return new Message(null, MessageType.SUCCESS, "Info updated.");
    }

    public Message addBook(String title, String author, String synopsis, String language,
                           List<String> genres, boolean availability, double costPerDownload,
                           Map<String, String> downloadLink, String imagePath) {
        Message message = checkBookFields(title, author, synopsis, language, genres, costPerDownload);

        if (message.getType().equals(MessageType.ERROR))
            return message;

        try {
            connDB.insertBook(title, author, synopsis, language, genres, availability, costPerDownload, downloadLink, imagePath);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new Message(null, MessageType.SUCCESS, "Book entry created.");
    }

    public Message checkBookFieldsTest(String title, String author, String synopsis,
                                       String language, List<String> genres,
                                       double costPerDownload) {
        return checkBookFields(title, author, synopsis, language, genres, costPerDownload);
    }

    private Message checkBookFields(String title, String author, String synopsis,
                                    String language, List<String> genres,
                                    double costPerDownload) {

        // The title, author, and language are text fields with a maximum of 100 characters each.
        // The synopsis is also a text field, but it accepts a maximum of 700 characters.
        // The genre is a text field that accepts multiple inputs, with a maximum of 30 characters each.
        // The download link for the digital version of the book is the only field that can be empty.
        // An  error  message  must appear below the field (cost per download) if a non-numerical or negative value is given.

        // Check if fields are empty
        if (title == null || title.isBlank())
            return new Message("title", MessageType.ERROR, "Empty field.");
        if (author == null || author.isBlank())
            return new Message("author", MessageType.ERROR, "Empty field.");
        if (synopsis == null || synopsis.isBlank())
            return new Message("synopsis", MessageType.ERROR, "Empty field.");
        if (language == null || language.isBlank())
            return new Message("language", MessageType.ERROR, "Empty field.");
        if (genres == null || genres.isEmpty())
            return new Message("genres", MessageType.ERROR, "Empty field.");
        for (int i = 0; i < genres.size(); i++)
            if (genres.get(i).isBlank())
                return new Message("genre" + i, MessageType.ERROR, "Empty field.");

        // Check if fields meet char limit
        if (title.length() > 100)
            return new Message("title", MessageType.ERROR, "Title is too long.");
        if (author.length() > 100)
            return new Message("author", MessageType.ERROR, "Author is too long.");
        if (language.length() > 100)
            return new Message("language", MessageType.ERROR, "Language is too long.");
        if (synopsis.length() > 700)
            return new Message("synopsis", MessageType.ERROR, "Synopsis is too long.");
        for (int i = 0; i < genres.size(); i++)
            if (genres.get(i).length() > 30)
                return new Message("genre" + i, MessageType.ERROR, "Genre is too long.");

        // Check if cost per download is negative
        if (costPerDownload < 0)
            return new Message("costPerDownload", MessageType.ERROR, "Cost per download can't be negative.");
        return new Message(null, MessageType.SUCCESS, "");
    }

    public Message updateBookInfo(int id, String title, String author, String synopsis, String language,
                                  List<String> genres, boolean availability, double costPerDownload,
                                  Map<String, String> downloadLink, String imagePath) {
        Message message = checkBookFields(title, author, synopsis, language, genres, costPerDownload);

        if (message.getType().equals(MessageType.ERROR))
            return message;

        try {
            connDB.updateBook(id, title, author, synopsis, language, genres, availability, costPerDownload, downloadLink, imagePath);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new Message(null, MessageType.SUCCESS, "Book entry created.");
    }

    public boolean isAdmin() {
        if (loggedAccount != null)
            return loggedAccount.getTypeUser() == UserType.LIBRARIAN;
        return false;
    }

    public ArrayList<Book> search(String search) {
        try {
            return connDB.search(search, isAdmin());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Message downloadBook(Book book, String format) throws SQLException {
        if (connDB.canDownloadBook(book.getId(), loggedAccount.getEmail())) {
            //TODO Download
            connDB.downloadBook(book.getId(), loggedAccount.getEmail(), format);
            return new Message(null, MessageType.SUCCESS, "Book downloaded successfully");
        }
        return new Message(null, MessageType.ERROR, "Book already downloaded");
    }

    public Message downloadBookTest(Book book, String email, String format) throws SQLException {
        if (connDB.canDownloadBook(book.getId(), email)) {
            //TODO Download
            connDB.downloadBook(book.getId(), email, format);
            return new Message(null, MessageType.SUCCESS, "Book downloaded successfully");
        }
        return new Message(null, MessageType.ERROR, "Book already downloaded");
    }

    /*
     * List by Filters
     */
    public ArrayList<Book> listByFiltersTest(List<String> filtersGenre, List<String> filtersLanguage, List<String> filtersFormat, boolean isAdmin) throws SQLException {
        if (filtersGenre != null && filtersLanguage != null && filtersFormat != null)
            return connDB.listByFilters(filtersGenre, filtersLanguage, filtersFormat, isAdmin);
        return new ArrayList<>();
    }

    public ArrayList<Book> listByFilters(List<String> filtersGenre, List<String> filtersLanguage, List<String> filtersFormat) {
        if (filtersGenre != null && filtersLanguage != null && filtersFormat != null) {
            try {
                return connDB.listByFilters(filtersGenre, filtersLanguage, filtersFormat, isAdmin());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return new ArrayList<>();
    }
    /*
     * End List by Filters
     */
    /*
     * Copyright costs and statistics
     */

    public double copyrightAllBooks() {

        ArrayList<Book> books;
        double total = 0;
        try {
            books = connDB.listAllBooks(isAdmin());


            for (Book book : books) {
                total += connDB.getBookDownloadCounter(book.getId()) * book.getCostPerDownload();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return total;

    }

    public double copyrightBook(Book book) {

        if (book == null)
            return 0;

        int copies;
        try {
            copies = connDB.getBookDownloadCounter(book.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return copies * book.getCostPerDownload();

    }

    public HashMap<String, Integer> statisticsBookDownloads() throws SQLException {

        HashMap<String, Integer> result = new HashMap<>();

        List<Book> books = connDB.listAllBooks(isAdmin());

        for (Book book : books) {

            int total = connDB.getBookDownloadCounter(book.getId());
            result.put(book.getTitle(), total);

        }
        return result;
    }

    public List<BookPrice> statisticCostPerEachBook(int limit, String order) {


        List<BookPrice> bp = new ArrayList<>();

        ResultSet resultSet;
        try {
            resultSet = connDB.getStatisticCostPerEachBook(limit, order);


            while (resultSet.next()) {
                bp.add(new BookPrice(resultSet.getString(1), resultSet.getDouble(2)));
            }

            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bp;
    }


    public HashMap<String, Integer> statisticsFormatDownloads() {


        HashMap<String, Integer> result = new HashMap<>();

        ResultSet resultSet;
        try {
            resultSet = connDB.getStatisticFormatDownloads();

            while (resultSet.next()) {
                result.put(resultSet.getString(1), resultSet.getInt(2));
            }

            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;

    }

    public HashMap<String, Integer> statisticAllDownloadedBooks() {


        HashMap<String, Integer> result = new HashMap<>();

        ResultSet resultSet;
        try {
            resultSet = connDB.getStatisticAllDownloadedBooks();


            while (resultSet.next()) {

                result.put(resultSet.getString(1), resultSet.getInt(2));

            }

            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;

    }

    public HashMap<String, Integer> statisticMostDownloadedBooks(int limit) {


        HashMap<String, Integer> result = new HashMap<>();

        ResultSet resultSet;

        try {
            resultSet = connDB.getStatisticMostDownloadedBooks(limit);


            while (resultSet.next()) {

                result.put(resultSet.getString(1), resultSet.getInt(2));

            }

            resultSet.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;

    }

    /*
     * End Copyright costs and statistics
     */


    public Message addReviewTest(Book book, int rating, String review, String email) throws SQLException {
        User loggedAccount = connDB.getUserInformation(email);
        if (book == null) {
            return new Message(null, MessageType.ERROR, "Book is null.");
        }
        if (loggedAccount == null) {
            return new Message(null, MessageType.ERROR, "User is null");
        }
        if (!connDB.canDownloadBook(book.getId(), loggedAccount.getEmail())) {
            if (rating < 1 || rating > 5)
                return new Message("rating", MessageType.ERROR, "Invalid rating");
            if (connDB.addReview(loggedAccount, book, rating, review) == 0)
                return new Message(null, MessageType.ERROR, "Failed to add Review");
            book.addReview(new Review(connDB.getReviewId(book.getId(), loggedAccount.getEmail()), loggedAccount.getEmail(), rating, review));
            return new Message(null, MessageType.SUCCESS, "Review added successfully");
        }
        return new Message(null, MessageType.ERROR, "User must download the book before adding a rating/review.");
    }

    public Message addReview(Book book, int rating, String review) {
        if (book == null)
            return new Message(null, MessageType.ERROR, "Book is null.");
        if (loggedAccount == null)
            return new Message(null, MessageType.ERROR, "User is null");
        try {
            if (!connDB.canDownloadBook(book.getId(), loggedAccount.getEmail())) {
                if (rating < 1 || rating > 5)
                    return new Message("rating", MessageType.ERROR, "Invalid rating");
                if (connDB.addReview(loggedAccount, book, rating, review) == 0)
                    return new Message(null, MessageType.ERROR, "Failed to add Review");
                book.addReview(new Review(connDB.getReviewId(book.getId(), loggedAccount.getEmail()), loggedAccount.getEmail(), rating, review));
                return new Message(null, MessageType.SUCCESS, "Review added successfully");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new Message(null, MessageType.ERROR, "User must download the book before adding a rating/review.");
    }

    public Message deleteReview(Book book, Review review) throws SQLException {
        if (book == null)
            return new Message(null, MessageType.ERROR, "Book is null.");
        if (review == null)
            return new Message(null, MessageType.ERROR, "Review is null.");
        if (connDB.deleteReview(review.getId()) == 0) {
            return new Message(null, MessageType.ERROR, "Review doesn't exist");
        }
        book.removeReview(review.getId());
        return new Message(null, MessageType.SUCCESS, "Review deleted successfully.");
    }

    public void changeBookVisibility(int idLivro) {
        try {
            connDB.updateBookAvailability(idLivro);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeBook(int idLivro) {
        try {
            connDB.deleteBook(idLivro);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Book getBookById(int idLivro) {
        try {
            return connDB.getBookById(idLivro, isAdmin());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getAllFiltersGenres() {
        try {
            return connDB.getAllFiltersGenres();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getAllFiltersLanguages() {
        try {
            return connDB.getAllFiltersLanguages();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getAllFiltersFormats() {
        try {
            return connDB.getAllFiltersFormats();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getBookDownloadCounter(int bookId) {
        try {
            return connDB.getBookDownloadCounter(bookId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getReviewCounter(int bookId) {
        try {
            return connDB.getReviewCounter(bookId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public double getRating(int bookId) {
        try {
            return connDB.getRating(bookId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

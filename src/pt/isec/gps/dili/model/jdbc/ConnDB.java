package pt.isec.gps.dili.model.jdbc;

import pt.isec.gps.dili.model.data.book.Book;
import pt.isec.gps.dili.model.data.book.Review;
import pt.isec.gps.dili.model.data.user.User;
import pt.isec.gps.dili.model.data.user.UserType;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnDB {
    private final static String DATABASE_URL = "jdbc:sqlite:DiLi.db";
    private final Connection dbConn;

    public ConnDB() throws SQLException {
        dbConn = DriverManager.getConnection(DATABASE_URL);
    }

    public void close() throws SQLException {
        if (dbConn != null)
            dbConn.close();
    }


    public boolean verifyLogin(String email, String password) throws SQLException {
        boolean result = false;
        Statement statement = dbConn.createStatement();

        String sqlQuery = "SELECT * FROM librarian " +
                "WHERE email='" + email + "' and password='" + password + "'";

        ResultSet resultSet = statement.executeQuery(sqlQuery);

        while(resultSet.next())
        {
            result = true;
        }

        if(!result){
            sqlQuery = "SELECT * FROM student_teacher " +
                    "WHERE email='" + email + "' and password='" + password + "'";

            resultSet = statement.executeQuery(sqlQuery);

            while(resultSet.next())
            {
                result = true;
            }
        }

        resultSet.close();
        statement.close();

        return result;
    }

    public User getUserInformation(String email) throws SQLException {

        User user = null;
        Statement statement = dbConn.createStatement();

        String sqlQuery = "SELECT * FROM librarian WHERE email='" + email + "'";

        ResultSet resultSet = statement.executeQuery(sqlQuery);

        while(resultSet.next())
        {
            user = new User(
                    resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    UserType.LIBRARIAN
            );
        }

        if(user == null){
            sqlQuery = "SELECT * FROM student_teacher WHERE email='" + email + "'";

            resultSet = statement.executeQuery(sqlQuery);

            while(resultSet.next())
            {
                user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        UserType.STUDENT_TEACHER
                );
            }
        }

        resultSet.close();
        statement.close();

        return user;
    }

    /*public User getLibrarian(int id) throws SQLException {

        Statement statement = dbConn.createStatement();

        String sqlQuery = "SELECT * FROM librarian WHERE id='" + id + "'";

        ResultSet resultSet = statement.executeQuery(sqlQuery);

        resultSet.close();
        statement.close();

        return new User(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("email"),
                UserType.Librarian
        );
    }*/

    public void insertLibrarianTest(String email, String name, String password, int id) throws SQLException {
        Statement statement = dbConn.createStatement();

        String sqlQuery = "INSERT INTO librarian (id, email, name, password) " +
                "VALUES ('" + id + "','" + email + "','" + name + "','" + password + "')";

        statement.executeUpdate(sqlQuery);

        statement.close();

        // return getLibrarian(((ResultSet)statement.getGeneratedKeys()).getInt("id"));
    }
    public void insertLibrarian(String email, String name, String password) throws SQLException {
        Statement statement = dbConn.createStatement();

        String sqlQuery = "INSERT INTO librarian (email, name, password) " +
                "VALUES ('" + email + "','" + name + "','" + password + "')";

        statement.executeUpdate(sqlQuery);

        statement.close();

        // return getLibrarian(((ResultSet)statement.getGeneratedKeys()).getInt("id"));
    }

    public int updateLibrarian(int id, String email, String nome, String password) throws SQLException
    {
        Statement statement = dbConn.createStatement();

        String sqlQuery = "UPDATE librarian SET email='" + email + "', name='" + nome + "', " +
                "password='" + password + "' WHERE id=" + id;
        int rowsAffected = statement.executeUpdate(sqlQuery);
        statement.close();
        return rowsAffected;
    }
    public void insertBookTest(int id, String title, String author, String synopsis,
                               boolean availability, double costPerDownload, String imagePath) throws SQLException {

        Statement statement = dbConn.createStatement();

        String sqlQuery = "INSERT INTO book (id, title, synopsis, author, availability, costPerDownload, image_path ) VALUES (" +
                "'" + id + "','" + title + "','" + synopsis + "','" + author + "','"
                + availability + "','" + costPerDownload + "','" + imagePath + "')";

        statement.executeUpdate(sqlQuery);
        statement.close();
    }

    public void insertBook(String title, String author, String synopsis, String language,
                           List<String> genres, boolean availability, double costPerDownload,
                           Map<String, String> downloadLink, String imagePath) throws SQLException{

        Statement statement = dbConn.createStatement();

        String sqlQuery = "INSERT INTO book ( title, synopsis, author, availability, costPerDownload, image_path ) VALUES (" +
                "'" + title + "','" + synopsis + "','" + author + "','"
                + availability + "','" + costPerDownload + "','" + imagePath + "')";

        statement.executeUpdate(sqlQuery);
        int idBook = statement.getGeneratedKeys().getInt(1);

        for(String format : downloadLink.keySet()) {
            sqlQuery = "SELECT * FROM format WHERE name LIKE '" + format + "'";
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            int idFormat = resultSet.getInt("id");
            resultSet.close();
            //TODO IF FORMAT DOESN'T EXIST

            sqlQuery = "INSERT INTO book_file VALUES " +
                    "('" + idBook + "', '" + idFormat + "', '" + downloadLink.get(format) + "')";
            statement.executeUpdate(sqlQuery);
        }

        sqlQuery = "SELECT * FROM language WHERE name LIKE '" + language + "'";
        int idLanguage = (statement.executeQuery(sqlQuery)).getInt("id");
        //TODO IF LANGUAGE DOESN'T EXIST

        sqlQuery = "INSERT INTO book_language VALUES " +
                "('" + idBook + "', '" + idLanguage + "')";
        statement.executeUpdate(sqlQuery);

        if(genres != null)
            for(String genre : genres) {

                sqlQuery = "SELECT id FROM genre WHERE name LIKE '" + genre + "'";
                ResultSet resultSet = statement.executeQuery(sqlQuery);
                int idGenre = resultSet.getInt(1);
                resultSet.close();
                if(idGenre == 0) { // No genre with the name
                    //TODO Create genre
                    sqlQuery = "INSERT INTO genre (name) VALUES ('" + genre + "')";
                    statement.executeUpdate(sqlQuery);
                    idGenre = statement.getGeneratedKeys().getInt(1);
                }

                //TODO Associate
                sqlQuery = "INSERT INTO book_genre ( book_id, genre_id ) VALUES ('" + idBook + "', '" + idGenre + "')";
                statement.executeUpdate(sqlQuery);

            }

        statement.close();

    }

    public void updateBookAvailability(int id) throws SQLException {
        Statement statement = dbConn.createStatement();
        String sqlQuery = "UPDATE book SET availability= NOT availability WHERE id='" + id + "'";
        statement.executeUpdate(sqlQuery);
        statement.close();
    }

    public void updateBook(int id, String title, String author, String synopsis, String language,
                           List<String> genres, boolean availability, double costPerDownload,
                           Map<String, String> downloadLink, String imagePath) throws SQLException {
        // TODO

        Statement statement = dbConn.createStatement();

        String sqlQuery;

        sqlQuery = String.format("UPDATE book SET " +
                "title='%s', " +
                "synopsis='%s', " +
                "author='%s', " +
                "availability='%b', " +
                "costPerDownload='%f', " +
                "image_path='%s' WHERE id='%s'",
                title, synopsis, author, availability, costPerDownload, id, imagePath);
        statement.executeUpdate(sqlQuery);

        sqlQuery = "DELETE FROM book_genre WHERE book_id='" + id + "'";
        statement.executeUpdate(sqlQuery);

        if(genres != null)
            for(String genre : genres) {

                sqlQuery = "SELECT id FROM genre WHERE name LIKE '" + genre + "'";
                ResultSet resultSet = statement.executeQuery(sqlQuery);
                int idGenre = resultSet.getInt(1);
                resultSet.close();
                if(idGenre == 0) { // No genre with the name
                    //TODO Create genre
                    sqlQuery = "INSERT INTO genre (name) VALUES ('" + genre + "')";
                    statement.executeUpdate(sqlQuery);
                    idGenre = statement.getGeneratedKeys().getInt(1);
                }

                //TODO Associate
                sqlQuery = "INSERT INTO book_genre ( book_id, genre_id ) VALUES ('" + id + "', '" + idGenre + "')";
                statement.executeUpdate(sqlQuery);

            }

        sqlQuery = "DELETE FROM book_language WHERE book_id='" + id + "'";
        statement.executeUpdate(sqlQuery);

        sqlQuery = "SELECT * FROM language WHERE name LIKE '" + language + "'";
        int idLanguage = (statement.executeQuery(sqlQuery)).getInt("id");
        //TODO IF LANGUAGE DOESN'T EXIST

        sqlQuery = "INSERT INTO book_language VALUES " +
                "('" + id + "', '" + idLanguage + "')";
        statement.executeUpdate(sqlQuery);



        sqlQuery = "DELETE FROM book_file WHERE book_id='" + id + "'";
        statement.executeUpdate(sqlQuery);

        for(String format : downloadLink.keySet()) {
            sqlQuery = "SELECT * FROM format WHERE name LIKE '" + format + "'";
            ResultSet resultSet = statement.executeQuery(sqlQuery);
            int idFormat = resultSet.getInt("id");
            resultSet.close();
            //TODO IF FORMAT DOESN'T EXIST

            sqlQuery = "INSERT INTO book_file VALUES " +
                    "('" + id + "', '" + idFormat + "', '" + downloadLink.get(format) + "')";
            statement.executeUpdate(sqlQuery);
        }
        statement.close();
    }
    public void deleteBook(int id) throws SQLException {
        Statement statement = dbConn.createStatement();
        String sqlQuery = "DELETE FROM book WHERE id='" + id + "'";
        statement.executeUpdate(sqlQuery);
        statement.close();
    }

    public void clearDB(String passcode, boolean bdbf, boolean bg, boolean bl, boolean b, boolean g, boolean bdown) throws SQLException {
        if(passcode.equals("test")) {

            Statement statement = dbConn.createStatement();
            String sqlQuery;
            if(bdbf) {
                sqlQuery = "DELETE FROM book_file";
                statement.executeUpdate(sqlQuery);
            }
            if(bg) {
                sqlQuery = "DELETE FROM book_genre";
                statement.executeUpdate(sqlQuery);
            }
            if(bl) {
                sqlQuery = "DELETE FROM book_language";
                statement.executeUpdate(sqlQuery);
            }
            if(bdown) {
                sqlQuery = "DELETE FROM book_download";
                statement.executeUpdate(sqlQuery);
            }
            if(b) {
                sqlQuery = "DELETE FROM rating_review";
                statement.executeUpdate(sqlQuery);
                sqlQuery = "DELETE FROM book";
                statement.executeUpdate(sqlQuery);
            }
            if(g) {
                sqlQuery = "DELETE FROM genre";
                statement.executeUpdate(sqlQuery);
            }
            statement.close();

        }
    }
    public void clearLibrarians(String passcode) throws SQLException {
        if(passcode.equals("test")) {
            Statement statement = dbConn.createStatement();
            String sqlQuery = "DELETE FROM librarian";
            statement.executeUpdate(sqlQuery);
            statement.close();
        }
    }
    public void clearUsers(String passcode) throws SQLException {
        if(passcode.equals("test")) {
            Statement statement = dbConn.createStatement();
            String sqlQuery = "DELETE FROM student_teacher";
            statement.executeUpdate(sqlQuery);
            statement.close();
        }
    }

    public List<String> getBookGenres(int bookId) throws SQLException {
        ArrayList<String> list = new ArrayList<>();
        Statement statement = dbConn.createStatement();
        String sqlQuery = "SELECT name FROM book_genre, genre " +
                "WHERE book_genre.genre_id = genre.id AND " +
                "book_id=" + bookId;
        ResultSet resultSet = statement.executeQuery(sqlQuery);
        while(resultSet.next()) {
            list.add(resultSet.getString(1));
        }
        statement.close();
        resultSet.close();
        return list;
    }
    public String getBookLanguage(int bookId) throws SQLException {
        Statement statement = dbConn.createStatement();
        String sqlQuery = "SELECT language.name FROM book_language, language " +
                "WHERE book_language.language_id = language.id AND " +
                "book_id=" + bookId;

        ResultSet resultSet = statement.executeQuery(sqlQuery);
        String language = resultSet.getString(1);
        resultSet.close();
        statement.close();
        return language;
    }
    public Map<String, String> getBookDownloadFile(int bookId) throws SQLException {
        HashMap<String, String> map = new HashMap<>();
        Statement statement = dbConn.createStatement();
        String sqlQuery = "SELECT name, url FROM book_file, format " +
                "WHERE book_file.format_id = format.id AND " +
                "book_id=" + bookId;
        ResultSet resultSet = statement.executeQuery(sqlQuery);
        while(resultSet.next()) {
            map.put(resultSet.getString("name"), resultSet.getString("url"));
        }
        statement.close();
        resultSet.close();
        return map;
    }

    public int getBookDownloadCounter(int bookId) throws SQLException {
        Statement statement = dbConn.createStatement();
        String sqlQuery = "SELECT COUNT(*) FROM book_download WHERE book_id=" + bookId;

        ResultSet resultSet = statement.executeQuery(sqlQuery);
        int ret = resultSet.getInt(1);
        statement.close();
        resultSet.close();
        return ret;
    }

    public boolean canDownloadBook(int bookId, String email) throws SQLException {
        Statement statement = dbConn.createStatement();
        String sqlQuery = "SELECT COUNT(*) FROM book_download WHERE " +
                "book_id=" + bookId + " AND user_email='" + email + "'";
        ResultSet resultSet = statement.executeQuery(sqlQuery);
        int ret = resultSet.getInt(1);
        statement.close();
        resultSet.close();
        return ret == 0;

    }
    public int downloadBook(int bookId, String email, String format) throws SQLException {
        Statement statement = dbConn.createStatement();
        String sqlQuery = "INSERT INTO book_download VALUES ('" + bookId + "', '" + email + "', '" + format + "')";
        int rowsAffected = statement.executeUpdate(sqlQuery);
        statement.close();
        return rowsAffected;
    }
    public ResultSet getStatisticFormatDownloads() throws SQLException {

        Statement statement = dbConn.createStatement();
        String sqlQuery = "SELECT format, COUNT(*) FROM test GROUP BY format";
        ResultSet resultSet = statement.executeQuery(sqlQuery);
        statement.close();
        resultSet.close();
        return resultSet;
    }

    private Book prepareBook(ResultSet resultSet) throws SQLException {

        int bookId = resultSet.getInt("id");
        return new Book(bookId,
                resultSet.getString("title"),
                resultSet.getString("author"),
                resultSet.getString("synopsis"),
                getBookLanguage(bookId),
                getBookGenres(bookId),
                resultSet.getBoolean("availability"),
                resultSet.getDouble("costPerDownload"),
                getBookDownloadFile(bookId),
                resultSet.getString("image_path"));

    }

    private ArrayList<Book> listBooks(ResultSet resultSet) throws SQLException {
        ArrayList<Book> bookArrayList = new ArrayList<>();
        while(resultSet.next()) {

            bookArrayList.add(prepareBook(resultSet));
        }

        return bookArrayList;
    }

    private List<String> getAllFilterSpecific(String table) throws SQLException {

        ArrayList<String> filterList = new ArrayList<>();
        Statement statement = dbConn.createStatement();

        String sqlQuery = "SELECT name FROM " + table;
        ResultSet resultSet = statement.executeQuery(sqlQuery);


        while(resultSet.next()) {

            filterList.add(resultSet.getString(1));
        }


        resultSet.close();
        statement.close();

        return filterList;
    }

    private String prepareQueryList(List<String> filters, String nameField) throws SQLException {
        StringBuilder stringBuilder = new StringBuilder();
        if(filters.size() == 1 && filters.get(0).equals("")) {
            filters = getAllFilterSpecific(nameField.split("\\.")[0]);
        }
        if(!filters.isEmpty()) {
            stringBuilder.append(" AND (");
            int i = 0;
            for (String filter : filters) {
                stringBuilder.append(nameField + " LIKE '").append(filter).append("'");
                if (i++ != filters.size() - 1)
                    stringBuilder.append(" OR ");
            }
            stringBuilder.append(")");
        }
        return stringBuilder.toString();
    }

    public ArrayList<Book> listByFilters(List<String> filtersGenre, List<String> filtersLanguage, List<String> filtersFormat, boolean isAdmin) throws SQLException {

        /*
        SELECT book.id, title, synopsis, author, availability, costPerDownload, image_path FROM book, genre, book_genre WHERE
            book.id = book_genre.book_id AND
            genre.id = book_genre.genre_id AND
            (genre.name LIKE 'Eletrónica' OR
            genre.name LIKE 'Programming');
         */

        /*
        SELECT DISTINCT book.id, title, synopsis, author, availability, costPerDownload, image_path FROM book, language, book_language INNER JOIN genre, book_genre ON book.id = book_genre.book_id AND genre.id = book_genre.genre_id
         AND (genre.name LIKE 'Eletrónica')
          INNER JOIN format, book_file ON book.id = book_file.book_id AND format.id = book_file.format_id
           AND (format.name LIKE 'pdf')
            WHERE book.id = book_language.book_id AND language.id = book_language.language_id AND (language.name LIKE 'French');
         */
        Statement statement = dbConn.createStatement();
        String genres = prepareQueryList(filtersGenre, "genre.name");
        String languages = prepareQueryList(filtersLanguage, "language.name");
        String formats = prepareQueryList(filtersFormat, "format.name");
        String sqlQuery = "SELECT DISTINCT book.id, title, synopsis, author, availability, costPerDownload, image_path FROM book, language, book_language " +
                " INNER JOIN genre, book_genre ON book.id = book_genre.book_id AND genre.id = book_genre.genre_id " +
                genres +
                " INNER JOIN format, book_file ON book.id = book_file.book_id AND format.id = book_file.format_id " +
                formats +
                " WHERE book.id = book_language.book_id AND language.id = book_language.language_id " +
                languages;
        if(!isAdmin)
            sqlQuery += " AND book.availability='true'";

        /*StringBuilder sqlQuery = new StringBuilder("SELECT book.id, title, synopsis, author, availability, costPerDownload, image_path FROM book, genre, book_genre WHERE " +
                "book.id = book_genre.book_id AND " +
                "genre.id = book_genre.genre_id AND " +
                "(");

        int i = 0;
        for(String filter : filters) {
            sqlQuery.append("genre.name LIKE '").append(filter).append("'");
            if(i++ != filters.size()-1)
                sqlQuery.append(" OR ");
        }
        sqlQuery.append(")");*/
        ResultSet resultSet = statement.executeQuery(sqlQuery);

        ArrayList<Book> bookArrayList = listBooks(resultSet);

        resultSet.close();
        statement.close();

        return bookArrayList;
    }
    public ArrayList<Book> listAllBooks(boolean isAdmin) throws SQLException {


        Statement statement = dbConn.createStatement();

        String sqlQuery = "SELECT * FROM book";
        if(!isAdmin)
            sqlQuery += " WHERE availability='true'";
        ResultSet resultSet = statement.executeQuery(sqlQuery);

        ArrayList<Book> bookArrayList = listBooks(resultSet);

        resultSet.close();
        statement.close();

        return bookArrayList;
    }
    public ArrayList<Book> search(String search, boolean isAdmin) throws SQLException {

        if(search.isBlank())
            return listAllBooks(isAdmin);

        Statement statement = dbConn.createStatement();

        String sqlQuery = "SELECT * FROM book " +
                "WHERE (title LIKE '%" + search + "%' " +
                "OR author LIKE  '%" + search + "%')";
        if(!isAdmin)
            sqlQuery += " AND availability='true'";

        ResultSet resultSet = statement.executeQuery(sqlQuery);
        ArrayList<Book> bookArrayList = listBooks(resultSet);

        resultSet.close();
        statement.close();

        return bookArrayList;
    }

    public Book getBookById(int id, boolean isAdmin) throws SQLException {
        Book book = null;
        Statement statement = dbConn.createStatement();

        String sqlQuery = "SELECT * FROM book " + "WHERE id='" + id + "'";
        if(!isAdmin)
            sqlQuery += " AND availability='true'";

        ResultSet resultSet = statement.executeQuery(sqlQuery);

        if(resultSet.next()) {
            book = prepareBook(resultSet);
        }

        resultSet.close();
        statement.close();

        return book;
    }

    public int addReview(User user, Book book, int rating, String review) throws SQLException {
        try(Statement statement = dbConn.createStatement()) {
            String sqlQuery = "INSERT INTO rating_review (user_email, book_id, rating, review) VALUES ('" + user.getEmail() + "', '" + book.getId() + "', '" + rating + "', '" + review + "')";
            statement.close();
            return statement.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            return 0;
        }
    }

    public int getReviewId(int bookId, String userEmail) throws SQLException {
        int reviewId = 0;
        Statement statement = dbConn.createStatement();

        String sqlQuery = "SELECT * FROM rating_review " + "WHERE book_id='" + bookId + "' AND user_email='" + userEmail + "'";

        ResultSet resultSet = statement.executeQuery(sqlQuery);

        if(resultSet.next()) {
            reviewId = resultSet.getInt("id");
        }

        resultSet.close();
        statement.close();

        return reviewId;
    }

    public int deleteReview(int id) throws SQLException {
        try(Statement statement = dbConn.createStatement()) {
            String sqlQuery = "UPDATE rating_review SET review='' WHERE id='" + id + "'";
            statement.close();
            return statement.executeUpdate(sqlQuery);
        } catch (SQLException e) {
            return 0;
        }
    }

    public Review getReview(int id) throws SQLException {
        Review review = null;
        Statement statement = dbConn.createStatement();

        String sqlQuery = "SELECT * FROM rating_review " + "WHERE id='" + id + "'";

        ResultSet resultSet = statement.executeQuery(sqlQuery);

        if(resultSet.next()) {
            review = new Review(resultSet.getInt("id"),
                    resultSet.getString("user_email"),
                    resultSet.getInt("rating"),
                    resultSet.getString("review"));
        }

        resultSet.close();
        statement.close();

        return review;
    }

}
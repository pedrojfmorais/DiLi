package model.data;

import model.data.user.User;
import model.jdcb.ConnDB;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class DiLi {

    private final ConnDB connDB;
    private static User loggedAccount;

    public DiLi() throws SQLException {
        this.connDB = new ConnDB();
        loggedAccount = null;
    }

    public Message authenticate(String email, String password) throws SQLException {
        if(!connDB.verifyLogin(email, password))
            return new Message("password", MessageType.ERROR, "Invalid email or password.");

        loggedAccount = connDB.getUserInformation(email);
        return new Message(null, MessageType.ERROR, "User logged in.");
    }

    public Message createLibrarian(String name, String email, String password) throws SQLException {
        Message message = checkUserFields(name, email, password);

        if(message.type.equals(MessageType.ERROR))
            return message;

        connDB.insertLibrarian(name, email, password);
        return new Message(null, MessageType.SUCCESS, "Librarian created.");
    }

    private Message checkUserFields(String name, String email, String password) throws SQLException {
        // Check if fields are empty
        if(name.isBlank())
            return new Message("name", MessageType.ERROR, "Empty field.");
        if(email.isBlank())
            return new Message("email", MessageType.ERROR, "Empty field.");
        if(password.isBlank())
            return new Message("password", MessageType.ERROR, "Empty field.");

        // Check if fields have more than 50 chars
        if(name.length() > 50 )
            return new Message("name", MessageType.ERROR, "Name is too long.");
        if(email.length() > 50 )
            return new Message("email", MessageType.ERROR, "Email is too long.");
        if(password.length() > 50 )
            return new Message("password", MessageType.ERROR, "Password is too long.");

        // Check if email is already used
        if(connDB.getUserInformation(email) != null)
            return new Message("email", MessageType.ERROR, "Email is already used.");

        // Check if email is of type @domain
        if(!verifyEmailValidity(email))
            return new Message("email", MessageType.ERROR, "Email is invalid. Email should be of type: personal_info@domain");

        // Check password security
        if(!verifyPasswordSecurity(password))
            return new Message("password", MessageType.ERROR, "Password must have: \n" +
                    "At least 8 characters.\n" +
                    "A mixture of both uppercase and lowercase letters.\n" +
                    "A mixture of letters and numbers.\n" +
                    "At least one special character, e.g., ! @ # ? ].");
        return new Message(null, MessageType.SUCCESS, "");
    }

    private boolean verifyEmailValidity(String email) {
        return Pattern.compile("^(.+)@(.+)$").matcher(email).find();
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

    public Message updateLibrarianInfo(String name, String email, String password) throws SQLException {
        Message message = checkUserFields(name, email, password);

        if(message.type.equals(MessageType.ERROR))
            return message;

        connDB.updateLibrarian(loggedAccount.getId(), name, email, password);
        return new Message(null, MessageType.SUCCESS, "Info uptated.");
    }

    public Message addBook(String title, String author, String synopsis,
                           String language, ArrayList<String> genres, boolean availability,
                           double costPerDownload, String downloadLink) {
        Message message = checkBookFields(title, author, synopsis, language, genres, costPerDownload);

        if(message.type.equals(MessageType.ERROR))
            return message;

        connDB.insertBook(title, author, synopsis, language, genres, availability, costPerDownload, downloadLink);
        return new Message(null, MessageType.SUCCESS, "Book entry created.");
    }

    private Message checkBookFields(String title, String author, String synopsis,
                                    String language, ArrayList<String> genres,
                                    double costPerDownload) {

        // The title, author, and language are text fields with a maximum of 100 characters each.
        // The synopsis is also a text field, but it accepts a maximum of 700 characters.
        // The genre is a text field that accepts multiple inputs, with a maximum of 30 characters each.
        // The download link for the digital version of the book is the only field that can be empty.
        // An  error  message  must appear below the field (cost per download) if a non-numerical or negative value is given.

        // Check if fields are empty
        if(title.isBlank())
            return new Message("title", MessageType.ERROR, "Empty field.");
        if(author.isBlank())
            return new Message("author", MessageType.ERROR, "Empty field.");
        if(synopsis.isBlank())
            return new Message("synopsis", MessageType.ERROR, "Empty field.");
        if(language.isBlank())
            return new Message("language", MessageType.ERROR, "Empty field.");
        if(genres.isEmpty())
            return new Message("genres", MessageType.ERROR, "Empty field.");
        for(int i = 0; i < genres.size(); i++)
            if(genres.get(i).isBlank())
                return new Message("genre" + i, MessageType.ERROR, "Empty field.");

        // Check if fields meet char limit
        if(title.length() > 100 )
            return new Message("title", MessageType.ERROR, "Title is too long.");
        if(author.length() > 100 )
            return new Message("email", MessageType.ERROR, "Author is too long.");
        if(language.length() > 100 )
            return new Message("password", MessageType.ERROR, "Language is too long.");
        if(synopsis.length() > 700 )
            return new Message("password", MessageType.ERROR, "Synopsis is too long.");
        for(int i = 0; i < genres.size(); i++)
            if(genres.get(i).length() > 30)
                return new Message("genre" + i, MessageType.ERROR, "Genre is too long.");

        // Check if cost per download is negative
        if(costPerDownload < 0)
            return new Message("costPerDownload", MessageType.ERROR, "Cost per download can't be negative.");
        return new Message(null, MessageType.SUCCESS, "");
    }

    public Message updateBookInfo(String title, String author, String synopsis,
                           String language, ArrayList<String> genres, boolean availability,
                           double costPerDownload, String downloadLink) {
        Message message = checkBookFields(title, author, synopsis, language, genres, costPerDownload);

        if(message.type.equals(MessageType.ERROR))
            return message;

        connDB.updateBook(title, author, synopsis, language, genres, availability, costPerDownload, downloadLink);
        return new Message(null, MessageType.SUCCESS, "Book entry created.");
    }
}

package model.jdcb;

import model.data.user.User;
import model.data.user.UserType;

import java.sql.*;

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
                    UserType.Librarian
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
}
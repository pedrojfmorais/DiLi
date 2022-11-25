package model.data;

import model.data.user.User;
import model.jdcb.ConnDB;

import java.sql.SQLException;

public class DiLi {

    private final ConnDB connDB;

    public DiLi() throws SQLException {
        this.connDB = new ConnDB();
    }

    public User authenticate(String email, String password) throws SQLException {
        if(!connDB.verifyLogin(email, password))
            return null;

        return connDB.getUserInformation(email);
    }

}

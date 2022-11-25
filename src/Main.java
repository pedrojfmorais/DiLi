import model.data.DiLi;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        System.out.println(new DiLi().authenticate("a1234567@isec.pt", "!Qq123456789"));
    }
}
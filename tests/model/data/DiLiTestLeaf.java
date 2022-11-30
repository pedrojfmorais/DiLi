package model.data;

import model.data.book.Book;
import model.jdcb.ConnDB;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class DiLiTestLeaf {

    public boolean wasSuccessful(Message msg) {
        return msg.getType() == MessageType.SUCCESS;
    }

    @BeforeAll
    static void beforeAll() throws SQLException {
        new ConnDB().clearDB("test", true, true, true, true, true, true);
        new ConnDB().clearLibrarians("test");
        new ConnDB().clearUsers("test");
    }
    @BeforeEach
    public void beforeEach() throws SQLException {

        ConnDB conn = new ConnDB();

        conn.insertLibrarian("a1234567@isec.pt", "Marco", "!Qq123456789");
        conn.insertLibrarian("a21280055321@isec.pt", "António", "!Qq123456789");



        Map<String, String> downloadLink = new HashMap<String, String>();
        downloadLink.put("epub", "https://qgwlmcg2lz.pdcdn1.top/dl2.php?id=6754130&h=f64cc79da1c4d117f2175f678f985241&u=cache&ext=pdf&n=College%20physics%20-%20physics%20and%20astronomy");
        conn.insertBook("Book 1", "John Doe", "Sinopse", "Portuguese",
                Arrays.asList("Fisica", "Eletrónica"), true, 0.005, downloadLink, "");

        downloadLink = new HashMap<String, String>();
        downloadLink.put("pdf", "https://m5mdanpkq6.pdcdn1.top/dl2.php?id=200665543&h=545e66e7ff5a77ae8a9fd70f9fac0724&u=cache&ext=pdf&n=Portuguese%20learn%20portuguese%20in%2021%20days%20-%20a%20practical%20guide%20to%20make%20portuguese%20look%20easy%20even%20for%20beginners");
        conn.insertBook("Book 2", "Jane Doe", "Sinopse do livro 2", "English",
                List.of("Eletrónica"), true, 0.002, downloadLink, "");

        downloadLink = new HashMap<String, String>();
        downloadLink.put("pdf", "https://q1oxh77y1t.pdcdn1.top/dl2.php?id=33414495&h=5d85dc55db6512fc8c4ace4fa5d11b19&u=cache&ext=pdf&n=Capital%20markets%20financial%20management%20and%20investment%20management");
        conn.insertBook("Book 3", "John Doe", "Sinopse", "English",
                List.of("Management"), true, 0.015, downloadLink, "");

        downloadLink = new HashMap<String, String>();
        downloadLink.put("pdf", "https://2zappmzhw3.pdcdn1.top/dl2.php?id=178205861&h=351217e7aed2625b1e321796d7cd8d0f&u=cache&ext=pdf&n=Kotlin%20for%20android%20developers%20learn%20kotlin%20the%20easy%20way%20while%20developing%20an%20android%20app");
        downloadLink.put("epub", "https://2zappmzhw3.pdcdn1.top/dl2.php?id=178205861&h=351217e7aed2625b1e321796d7cd8d0f&u=cache&ext=pdf&n=Kotlin%20for%20android%20developers%20learn%20kotlin%20the%20easy%20way%20while%20developing%20an%20android%20app");
        conn.insertBook("Book 2", "Jane Doe", "Learn to program Kotlin ", "English",
                List.of("Programming", "Java", "Kotlin"), true, 0.105, downloadLink, "");

    }
    @AfterEach
    public void afterEach() throws SQLException {
        new ConnDB().clearDB("test", true, true, true, true, true, true);
        new ConnDB().clearLibrarians("test");
        new ConnDB().clearUsers("test");
    }

    /*
     * TODO
     *  Authenticate:
     *      connDB.verifyLogin(email, password)
     *      connDB.getUserInformation(email)
     *
     *
     */

    @ParameterizedTest
    @MethodSource
    void verifyLogin(String email, String password) throws SQLException {
        // assertNotNull(new DiLi().authenticate(email, password));
        // assertTrue(wasSuccessful(new DiLi().authenticate(email, password)));
        assertNull(new ConnDB().verifyLogin(email, password));
    }
    public static Stream<Arguments> verifyLogin() {
        return Stream.of(
                arguments("a1234567@isec.pt", "!Qq123456789")
                //,arguments("a21280686@isec.pt", "pedro")
        );
    }

    /*@ParameterizedTest
    @MethodSource
    void authenticateTrue(String email, String password) throws SQLException {
        // assertNotNull(new DiLi().authenticate(email, password));
        assertTrue(wasSuccessful(new DiLi().authenticate(email, password)));
    }

    public static Stream<Arguments> authenticateTrue() {
        return Stream.of(
                arguments("a1234567@isec.pt", "!Qq123456789")
                //,arguments("a21280686@isec.pt", "pedro")
        );
    }*/
}
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
import java.sql.Statement;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class DiLiTest {

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

    @ParameterizedTest
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
    }

    @ParameterizedTest
    @MethodSource
    void authenticateFalse(String email, String password) throws SQLException {
        //assertNull(new DiLi().authenticate(email, password));
        assertFalse(wasSuccessful(new DiLi().authenticate(email, password)));
    }

    public static Stream<Arguments> authenticateFalse() {
        return Stream.of(
                arguments("a123@isec.pt", "!!!")
        );
    }
    @ParameterizedTest
    @MethodSource
    void createLibrarianREmail(String email, String name, String password) throws SQLException {
        //assertNull(new DiLi().authenticate(email, password));
        assertFalse(wasSuccessful(new DiLi().createLibrarian(name, email, password)));
    }
    public static Stream<Arguments> createLibrarianREmail() {
        return Stream.of(
                arguments("a1234567@isec.pt", "Marco", "!Qq123456789")
        );
    }
    @ParameterizedTest
    @MethodSource
    void createLibrarianWeakPassword(String email, String name, String password) throws SQLException {
        //assertNull(new DiLi().authenticate(email, password));
        assertFalse(wasSuccessful(new DiLi().createLibrarian(name, email, password)));
    }
    public static Stream<Arguments> createLibrarianWeakPassword() {
        return Stream.of(
                arguments("a1arsfasf567@isec.pt", "Marco", "Qq123456789")
        );
    }
    @ParameterizedTest
    @MethodSource
    void createLibrarianSuccess(String email, String name, String password) throws SQLException {
        //assertNull(new DiLi().authenticate(email, password));
        assertTrue(wasSuccessful(new DiLi().createLibrarian(name, email, password)));
    }
    public static Stream<Arguments> createLibrarianSuccess() {
        return Stream.of(
                arguments("success@isec.pt", "Marco", "!Qq123456789")
        );
    }
    @ParameterizedTest
    @MethodSource
    void createBookMaxField(String title, String author, String synopsis, String language,
                            List<String> genres, boolean availability, double costPerDownload,
                            Map<String, String> downloadLink, String imagePath) throws SQLException {
        //assertNull(new DiLi().authenticate(email, password));
        assertFalse(wasSuccessful(new DiLi().addBook(title, author, synopsis, language, genres, availability, costPerDownload, downloadLink, imagePath)));
    }
    public static Stream<Arguments> createBookMaxField() {

        Map<String, String> downloadLink = new HashMap<String, String>();
        downloadLink.put("epub", "https://qgwlmcg2lz.pdcdn1.top/dl2.php?id=6754130&h=f64cc79da1c4d117f2175f678f985241&u=cache&ext=pdf&n=College%20physics%20-%20physics%20and%20astronomy");
        return Stream.of(
                arguments("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin a purus sed magna sodales sodales sed.",
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin a purus sed magna sodales sodales sed.",
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla id quam viverra orci commodo varius. Sed pharetra enim libero, sed sodales urna scelerisque sed. Quisque ultricies molestie ligula, non varius massa volutpat in. Quisque condimentum, erat eget aliquam viverra, erat metus rhoncus arcu, eget lobortis neque erat eget dolor. Donec eget metus eget ipsum pulvinar lobortis. Nam faucibus eu arcu a vehicula. Praesent quis quam urna. Donec magna lectus, fermentum vitae erat at, tempor porttitor velit. Fusce tincidunt odio nibh, vitae volutpat nisi maximus et. Vivamus vitae tempus dolor, vitae convallis nisi. Cras sed luctus nibh, id gravida sapien. Sed vel scelerisque nulla, at sodales sed.",
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin a purus sed magna sodales sodales sed.",
                        Arrays.asList("Fisica", "Eletrónica"),
                        true,
                        0.005,
                        downloadLink,
                        "")
        );
    }
    @ParameterizedTest
    @MethodSource
    void createBookSuccess(String title, String author, String synopsis, String language,
                            List<String> genres, boolean availability, double costPerDownload,
                            Map<String, String> downloadLink, String imagePath) throws SQLException {
        //assertNull(new DiLi().authenticate(email, password));
        assertTrue(wasSuccessful(new DiLi().addBook(title, author, synopsis, language, genres, availability, costPerDownload, downloadLink, imagePath)));
    }
    public static Stream<Arguments> createBookSuccess() {

        Map<String, String> downloadLink = new HashMap<>();
        downloadLink.put("pdf", "https://2zappmzhw3.pdcdn1.top/dl2.php?id=178205861&h=351217e7aed2625b1e321796d7cd8d0f&u=cache&ext=pdf&n=Kotlin%20for%20android%20developers%20learn%20kotlin%20the%20easy%20way%20while%20developing%20an%20android%20app");
        return Stream.of(
                arguments("Book Success",
                        "John Doe",
                        "Kotlin for beginners",
                        "English",
                        List.of("Kotlin"),
                        true,
                        0.005,
                        downloadLink,
                        "")
        );
    }
    @ParameterizedTest
    @MethodSource
    void listBooksByFiltersMultiple(List<String> filtersGenre, List<String> filtersLanguage, List<String> filtersFormat, int howMany) throws SQLException {
        ArrayList<Book> books = new DiLi().listByFilters(filtersGenre, filtersLanguage, filtersFormat);
        assertNotNull(books);
        assertEquals(howMany, books.size());
    }

    public static Stream<Arguments> listBooksByFiltersMultiple() {
        return Stream.of(
                arguments(List.of("Eletrónica"), List.of(""), List.of(""), 2),
                arguments(List.of("Eletrónica"), List.of("Portuguese"), List.of(""), 1),
                arguments(List.of("Eletrónica"), List.of("Portuguese"), List.of("epub"), 1),
                arguments(List.of("Eletrónica"), List.of(""), List.of("epub"), 1),
                arguments(List.of(""), List.of(""), List.of("epub"), 2),
                // arguments(List.of(""), List.of(""), List.of("epub", "pdf"), 1),
                arguments(List.of(""), List.of("French"), List.of("epub", "pdf"), 0),
                arguments(List.of(""), List.of(""), List.of(""), 4)
        );
    }

    // @ParameterizedTest
    //@MethodSource
    @Test
    void downloadBookSecondDownload() throws SQLException {
        //assertNull(new DiLi().authenticate(email, password));
        //System.out.println(new ConnDB().search("Book 1"));
        /*ConnDB conn = new ConnDB();
        Book book = conn.search("Book 1").get(0);*/
        DiLi dili = new DiLi();
        Book book = dili.search("Book 1").get(0);

        assertFalse(
        wasSuccessful(dili.downloadBookTest(book, "a1234567@isec.pt"))
                &&
                wasSuccessful(dili.downloadBookTest(book, "a1234567@isec.pt"))

        );
    }
    /*public static Stream<Arguments> downloadBookSecondDownload() throws SQLException {
        //System.out.println(new ConnDB().search("Book 1"));
        return Stream.of(
                arguments()
        );
    }*/
}
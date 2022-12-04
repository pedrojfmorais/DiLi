package model.data;

import model.data.book.Book;
import model.jdcb.ConnDB;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class DiLiTestLeaf {

    public boolean wasSuccessful(Message msg) {
        return msg.getType() == MessageType.SUCCESS;
    }

    private static void clearDB() throws SQLException {
        new ConnDB().clearDB("test", true, true, true, true, true, true);
        new ConnDB().clearLibrarians("test");
        new ConnDB().clearUsers("test");
    }
    private static void dbSeeder() throws SQLException {

        ConnDB conn = new ConnDB();

        conn.insertLibrarianTest("a123456@isec.pt", "Marco", "!Qq123456789", 1);
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
    /*@BeforeAll
    static void beforeAll() throws SQLException {
        clearDB();
    }
    @BeforeEach
    public void beforeEach() throws SQLException {

        dbSeeder();
        System.out.println("hm");

    }
    @AfterEach
    public void afterEach() throws SQLException {
        clearDB();
    }*/


    @Nested
    class authenticate {
        /*
         * TODO
         *  Authenticate:
         *      connDB.verifyLogin(email, password)
         *      connDB.getUserInformation(email)
         *
         *
         */
        @BeforeAll
        static void beforeAll() throws SQLException {
            clearDB();
            dbSeeder();

        }
        @ParameterizedTest
        @MethodSource
        void verifyLoginSuccess(String email, String password) throws SQLException {
            // assertNotNull(new DiLi().authenticate(email, password));
            // assertTrue(wasSuccessful(new DiLi().authenticate(email, password)));
            assertTrue(new ConnDB().verifyLogin(email, password));
        }

        public static Stream<Arguments> verifyLoginSuccess() {
            return Stream.of(
                    arguments("a1234567@isec.pt", "!Qq123456789")
                    //,arguments("a21280686@isec.pt", "pedro")
            );
        }

        @ParameterizedTest
        @MethodSource
        void verifyLoginFail(String email, String password) throws SQLException {
            // assertNotNull(new DiLi().authenticate(email, password));
            // assertTrue(wasSuccessful(new DiLi().authenticate(email, password)));
            assertFalse(new ConnDB().verifyLogin(email, password));
        }

        public static Stream<Arguments> verifyLoginFail() {
            return Stream.of(
                    arguments("a123456722@isec.pt", "!Qq123456789"),
                    arguments("", "!Qq123456789"),
                    arguments("a123456722@isec.pt", ""),
                    arguments("", "")
                    //,arguments("a21280686@isec.pt", "pedro")
            );
        }

        @ParameterizedTest
        @MethodSource
        void getUserInformationSuccess(String email) throws SQLException {
            assertNotNull(new ConnDB().getUserInformation(email));
        }

        public static Stream<Arguments> getUserInformationSuccess() {
            return Stream.of(
                    arguments("a1234567@isec.pt")
            );
        }

        @ParameterizedTest
        @MethodSource
        void getUserInformationFail(String email) throws SQLException {
            assertNull(new ConnDB().getUserInformation(email));
        }

        public static Stream<Arguments> getUserInformationFail() {
            return Stream.of(
                    arguments("a123456722@isec.pt"),
                    arguments("")
            );
        }

        @ParameterizedTest
        @MethodSource
        void authenticateSuccess(String email, String password) throws SQLException {
            //assertNotNull(new DiLi().authenticate(email, password));
            assertNotNull(new DiLi().authenticate(email, password));
            assertEquals(MessageType.SUCCESS, (new DiLi().authenticate(email, password)).type);
        }

        public static Stream<Arguments> authenticateSuccess() {
            return Stream.of(
                    arguments("a1234567@isec.pt", "!Qq123456789")
            );
        }

        @ParameterizedTest
        @MethodSource
        void authenticateFail(String email, String password) throws SQLException {
            assertNotNull(new DiLi().authenticate(email, password));
            assertEquals(MessageType.ERROR, (new DiLi().authenticate(email, password)).type);
        }

        public static Stream<Arguments> authenticateFail() {
            return Stream.of(
                    arguments("a123456722@isec.pt", "!Qq123456789"),
                    arguments("", "!Qq123456789"),
                    arguments("a123456722@isec.pt", ""),
                    arguments("", "")
            );
        }
    }

    @Nested
    class createLibrarianTest {
        /*
         * TODO
         *  createLibrarian:
         *      checkUserFields(name, email, password)
         *          connDB.getUserInformation(email) # Tested
         *          verifyEmailValidity(email)
         *          verifyPasswordSecurity(password)
         *      connDB.insertLibrarian(name, email, password)
         *
         *
         */
        @BeforeAll
        static void beforeAll() throws SQLException {
            clearDB();
            dbSeeder();

        }
        @ParameterizedTest
        @MethodSource
        void verifyEmailValiditySuccess(String email) throws SQLException {
            //assertNotNull(new DiLi().authenticate(email, password));
            assertTrue(new DiLi().verifyEmailValidityTest(email));
        }

        public static Stream<Arguments> verifyEmailValiditySuccess() {
            return Stream.of(
                    arguments("a1234567@isec.pt"),
                    arguments("a1234567@abc.pt")
            );
        }

        @ParameterizedTest
        @MethodSource
        void verifyEmailValidityFail(String email) throws SQLException {
            assertFalse(new DiLi().verifyEmailValidityTest(email));
        }

        public static Stream<Arguments> verifyEmailValidityFail() {
            return Stream.of(
                    arguments(""),
                    arguments("@isec.pt"),
                    arguments("a123456722@.pt"),
                    arguments("a123456722@isec."),
                    arguments("@.pt"),
                    arguments("a123456722@."),
                    arguments("@isec."),
                    arguments("@."),
                    arguments("a123456722"),
                    arguments("a123456722@"),
                    arguments("a123456722@isec"),
                    arguments("a123456722@isec.")
            );
        }
        @ParameterizedTest
        @MethodSource
        void verifyPasswordSecuritySuccess(String password) throws SQLException {
            //assertNotNull(new DiLi().authenticate(email, password));
            assertTrue(new DiLi().verifyPasswordSecurityTest(password));
        }

        public static Stream<Arguments> verifyPasswordSecuritySuccess() {
            return Stream.of(
                    arguments("!Qq123456789"),
                    arguments("!!31fefQf4#12")
            );
        }

        @ParameterizedTest
        @MethodSource
        void verifyPasswordSecurityFail(String password) throws SQLException {
            assertFalse(new DiLi().verifyPasswordSecurityTest(password));
        }

        public static Stream<Arguments> verifyPasswordSecurityFail() {
            return Stream.of(
                    arguments("abc"),
                    arguments("abcdefgh"),
                    arguments("AAAA"),
                    arguments("AAAAAAAA"),
                    arguments("aAaA"),
                    arguments("aAaAaAaA"),
                    arguments("1234"),
                    arguments("12345678"),
                    arguments("!!!!"),
                    arguments("!!!!!!!!"),
                    arguments("a1a2aAaA"),
                    arguments("aA1aA2AaAa"),
                    arguments("aA1!")
            );
        }

        @ParameterizedTest
        @MethodSource
        void checkUserFieldsSuccess(String name, String email, String password) throws SQLException {
            //assertNotNull(new DiLi().authenticate(email, password));
            Message message = new DiLi().checkUserFieldsTest(name, email, password, false);
            assertNotNull(message);
            assertEquals(MessageType.SUCCESS, message.type);
        }

        public static Stream<Arguments> checkUserFieldsSuccess() {
            return Stream.of(
                    arguments("Marco", "a123456722@isec.pt", "!Qq123456789")
            );
        }

        @ParameterizedTest
        @MethodSource
        void checkUserFieldsFail(String name, String email, String password) throws SQLException {
            Message message = new DiLi().checkUserFieldsTest(name, email, password, false);
            assertNotNull(message);
            assertEquals(MessageType.ERROR, message.type);
        }

        public static Stream<Arguments> checkUserFieldsFail() {
            return Stream.of(
                    arguments("", "a123456722@isec.pt", "!Qq123456789"),
                    arguments("Marco", "", "!Qq123456789"),
                    arguments("Marco", "a123456722@isec.pt", ""),

                    arguments("Lorem ipsum dolor sit amet, consectetur vestibulum.", "a123456722@isec.pt", "!Qq123456789"),
                    arguments("Marco", "Loremipsumdolorsitametconsecteturvestibulum@isec.pt", "!Qq123456789"),
                    arguments("Marco", "a123456722@isec.pt", "!Loremipsumdolorsitametconsecteturvestibulum1234568"),

                    arguments("Marco", "a1234567@isec.pt", "!Qq12345678"),

                    arguments("Marco", "a123456722@", "!Qq12345678"),

                    arguments("Marco", "a123456722@isec.pt", "!!!!!!!!")
            );
        }

        @Nested
        class createLibrarian {
            @BeforeAll
            static void beforeAll() throws SQLException {
                clearDB();
            }
            @BeforeEach
            public void beforeEach() throws SQLException {

                dbSeeder();

            }
            @AfterEach
            public void afterEach() throws SQLException {
                clearDB();
            }
            @ParameterizedTest
            @MethodSource
            void insertLibrarianSuccess(String name, String email, String password) throws SQLException {
                new ConnDB().insertLibrarian(email, name, password);
                assertNotNull(new ConnDB().getUserInformation(email));
            }

            public static Stream<Arguments> insertLibrarianSuccess() {
                return Stream.of(
                        arguments("Marco", "newTestEmail@isec.pt", "!Qq123456789")
                );
            }

            @ParameterizedTest
            @MethodSource
            void createLibrarianSuccess(String name, String email, String password) throws SQLException {
                Message message = new DiLi().createLibrarian(name, email, password);
                assertNotNull(message);
                assertEquals(MessageType.SUCCESS, message.type);
            }

            public static Stream<Arguments> createLibrarianSuccess() {
                return Stream.of(
                        arguments("Marco", "newTestEmail@isec.pt", "!Qq123456789")
                );
            }

            @ParameterizedTest
            @MethodSource
            void createLibrarianFail(String name, String email, String password) throws SQLException {
                Message message = new DiLi().createLibrarian(name, email, password);
                assertNotNull(message);
                assertEquals(MessageType.ERROR, message.type);
            }

            public static Stream<Arguments> createLibrarianFail() {
                return Stream.of(
                        arguments("Marco", "a1234567@isec.pt", "!Qq123456789")
                );
            }
        }

    }



    @Nested
    class updateLibrarianTest {
        /*
         * TODO
         *  updateLibrarianInfo:
         *      checkUserFields(name, email, password) # Tested
         *      connDB.updateLibrarian(loggedAccount.getId(), name, email, password)
         *
         *
         */
        @BeforeAll
        static void beforeAll() throws SQLException {
            clearDB();
            dbSeeder();

        }
        @ParameterizedTest
        @MethodSource
        void updateLibrarianSuccess(int id, String name, String email, String password) throws SQLException {
            int rowsAffected = new ConnDB().updateLibrarian(id, name, email, password);
            assertEquals(1, rowsAffected);
        }

        public static Stream<Arguments> updateLibrarianSuccess() {
            return Stream.of(
                    arguments(1, "Marco1", "a1234561@isec.pt", "!Qq1234567891")
            );
        }

        @ParameterizedTest
        @MethodSource
        void updateLibrarianFail(int id, String name, String email, String password) throws SQLException {
            int rowsAffected = new ConnDB().updateLibrarian(id, name, email, password);
            assertEquals(0, rowsAffected);
        }

        public static Stream<Arguments> updateLibrarianFail() {
            return Stream.of(
                    arguments(-1, "Marco1", "a12345617@isec.pt", "!Qq1234567891")
            );
        }

        @ParameterizedTest
        @MethodSource
        void updateLibrarianInfoSuccess(int id, String name, String email, String password) throws SQLException {
            Message message = new DiLi().updateLibrarianInfoTest(id, name, email, password);
            assertNotNull(message);
            assertEquals(MessageType.SUCCESS, message.type);
        }

        public static Stream<Arguments> updateLibrarianInfoSuccess() {
            return Stream.of(
                    arguments(1, "Marco1", "a1234567@isec.pt", "!Qq1234567891")
            );
        }

        @ParameterizedTest
        @MethodSource
        void updateLibrarianInfoFail(int id, String name, String email, String password) throws SQLException {
            Message message = new DiLi().updateLibrarianInfoTest(id, name, email, password);
            assertNotNull(message);
            assertEquals(MessageType.ERROR, message.type);
        }

        public static Stream<Arguments> updateLibrarianInfoFail() {
            return Stream.of(
                    arguments(1, "Marco", "newTestEmail@isec.pt", "!Qq123456789")
            );
        }
    }

    @Nested
    class addBookTest {
        /*
         * TODO
         *  addBook:
         *      checkBookFields(title, author, synopsis, language, genres, costPerDownload)
         *      connDB.insertBook(title, author, synopsis, language, genres, availability, costPerDownload, downloadLink, imagePath)
         *
         *
         */
        @BeforeAll
        static void beforeAll() throws SQLException {
            clearDB();
            dbSeeder();

        }


        @ParameterizedTest
        @MethodSource
        void checkBookFieldsSuccess(String title, String author, String synopsis,
                                    String language, List<String> genres,
                                    double costPerDownload) throws SQLException {
            //assertNotNull(new DiLi().authenticate(email, password));
            Message message = new DiLi().checkBookFieldsTest(title, author, synopsis, language, genres, costPerDownload);
            assertNotNull(message);
            assertEquals(MessageType.SUCCESS, message.type);
        }

        public static Stream<Arguments> checkBookFieldsSuccess() {
            return Stream.of(
                    arguments("Book 10", "Marco", "Book about a subject", "Portuguese", List.of("Genre1"), 0.003)
            );
        }

        @ParameterizedTest
        @MethodSource
        void checkBookFieldsFail(String title, String author, String synopsis,
                                    String language, List<String> genres,
                                    double costPerDownload) throws SQLException {
            Message message = new DiLi().checkBookFieldsTest(title, author, synopsis, language, genres, costPerDownload);
            assertNotNull(message);
            assertEquals(MessageType.ERROR, message.type);
        }

        public static Stream<Arguments> checkBookFieldsFail() {
            return Stream.of(
                    // arguments("abc", "abc", "abc", "Portuguese", List.of("abc"), 0.001),
                    arguments("", "", "", "", List.of(), 0.001),
                    arguments("", "", "", "", null, 0.001),
                    arguments("Title", "", "", "", null, 0.001),
                    arguments("", "Author", "", "", null, 0.001),
                    arguments("", "", "Synopsis", "", null, 0.001),
                    arguments("", "", "", "Portuguese", null, 0.001),
                    arguments("", "", "", "", List.of("Genre1"), 0.001),

                    arguments("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec porta hendrerit pharetra. Maecenas dui.", "", "", "", null, 0.001),
                    arguments("", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec porta hendrerit pharetra. Maecenas dui.", "", "", null, 0.001),
                    arguments("", "", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec porta hendrerit pharetra. Maecenas dui.".repeat(7), "", null, 0.001),
                    arguments("", "", "", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec porta hendrerit pharetra. Maecenas dui.", null, 0.001),
                    arguments("", "", "", "", List.of("abcdefghijkl".repeat(10)), 0.001),
                    arguments("", "", "", "", null, -0.001),


                    arguments("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec porta hendrerit pharetra. Maecenas dui.", "author", "sin", "Portuguese", List.of("teste"), 0.001),
                    arguments("title", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec porta hendrerit pharetra. Maecenas dui.", "sin", "Portuguese", List.of("teste"), 0.001),
                    arguments("title", "author", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec porta hendrerit pharetra. Maecenas dui.".repeat(7), "Portuguese", List.of("teste"), 0.001),
                    arguments("title", "author", "sin", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec porta hendrerit pharetra. Maecenas dui.", List.of("teste"), 0.001),
                    arguments("title", "author", "sin", "Portuguese", List.of("abcdefghijkl".repeat(10)), 0.001),
                    arguments("title", "author", "sin", "Portuguese", List.of("teste"), -0.001)

                    /*arguments("", "", "", "", "", 0.001),
                    arguments("", "", "", "", "", 0.001),
                    arguments("", "", "", "", "", 0.001),
                    arguments("", "", "", "", "", 0.001),
                    arguments("", "", "", "", "", 0.001),
                    arguments("", "", "", "", "", 0.001),
                    arguments("", "", "", "", "", 0.001),
                    arguments("", "", "", "", "", 0.001),
                    arguments("", "", "", "", "", 0.001),
                    arguments("Marco", "", "!Qq123456789"),
                    arguments("Marco", "a123456722@isec.pt", ""),

                    arguments("Lorem ipsum dolor sit amet, consectetur vestibulum.", "a123456722@isec.pt", "!Qq123456789"),
                    arguments("Marco", "Loremipsumdolorsitametconsecteturvestibulum@isec.pt", "!Qq123456789"),
                    arguments("Marco", "a123456722@isec.pt", "!Loremipsumdolorsitametconsecteturvestibulum1234568"),

                    arguments("Marco", "a1234567@isec.pt", "!Qq12345678"),

                    arguments("Marco", "a123456722@", "!Qq12345678"),

                    arguments("Marco", "a123456722@isec.pt", "!!!!!!!!")*/
            );
        }
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
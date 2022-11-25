package model.data;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.sql.SQLException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class DiLiTest {

    @ParameterizedTest
    @MethodSource
    void authenticateTrue(String email, String password) throws SQLException {
        assertNotNull(new DiLi().authenticate(email, password));
    }

    public static Stream<Arguments> authenticateTrue() {
        return Stream.of(
                arguments("a1234567@isec.pt", "!Qq123456789"),
                arguments("a21280686@isec.pt", "pedro")
        );
    }

    @ParameterizedTest
    @MethodSource
    void authenticateFalse(String email, String password) throws SQLException {
        assertNull(new DiLi().authenticate(email, password));
    }

    public static Stream<Arguments> authenticateFalse() {
        return Stream.of(
                arguments("a123@isec.pt", "!!!")
        );
    }
}
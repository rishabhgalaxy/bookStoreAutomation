package api_modules;

import data.BookstoreContext;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Random;

import static io.restassured.RestAssured.given;
import constant.EndPoint;

/**
 * API layer for user operations like registration and login.
 */
public class Api_User_module {

    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * Generates a random alphanumeric string for email/password.
     */
    public static String generateRandomString(int length) {
        StringBuilder result = new StringBuilder();
        Random rnd = new Random();
        while (length-- > 0) {
            result.append(CHAR_POOL.charAt(rnd.nextInt(CHAR_POOL.length())));
        }
        return result.toString();
    }

    /**
     * Registers a user using the given credentials.
     */
    public static Response registerUser(String email, String password, BookstoreContext context) {
        String payload = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", email, password);

        return given()
                .contentType(ContentType.JSON)
                .body(payload)
                .log().ifValidationFails()
                .when().post(EndPoint.SIGNUP)
                .then().log().ifValidationFails()
                .extract().response();
    }

    /**
     * Authenticates a user and returns the login response.
     */
    public static Response authenticateUser(String email, String password) {
        String payload = String.format("{\"email\":\"%s\",\"password\":\"%s\"}", email, password);

        return given()
                .contentType(ContentType.JSON)
                .body(payload)
                .log().ifValidationFails()
                .when().post(EndPoint.LOGIN)
                .then().log().ifValidationFails()
                .extract().response();
    }
}

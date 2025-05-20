package api_modules;

import constant.EndPoint;
import data.BookstoreContext;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.*;

import static io.restassured.RestAssured.given;

/**
 * This class encapsulates API interactions for book-related operations.
 */
public class Api_Book_module {

    private static final String JSON_TEMPLATE = """
            {
              "name": "%s",
              "author": "%s",
              "published_year": "%s",
              "book_summary": "%s"
            }
            """;

    private static RequestSpecification getBaseRequest(String accessToken) {
        RequestSpecification request = given().contentType(ContentType.JSON);
        if (accessToken != null && !accessToken.isBlank()) {
            request.header("Authorization", accessToken);
        }
        return request;
    }

    private static String createBookPayload(Map<String, Object> details) {
        return String.format(
                JSON_TEMPLATE,
                details.getOrDefault("bookName", ""),
                details.getOrDefault("author", ""),
                details.getOrDefault("published_year", ""),
                details.getOrDefault("book_summary", "")
        );
    }

    public static Response addBook(Map<String, Object> details, String token, BookstoreContext context) {
        return getBaseRequest(token)
                .body(createBookPayload(details))
                .post(EndPoint.ADDNEWBOOK)
                .then().log().ifValidationFails()
                .extract().response();
    }

    public static Response updateBook(Map<String, Object> details, String token) {
        return getBaseRequest(token)
                .body(createBookPayload(details))
                .pathParam("book_id", details.get("createdBookId"))
                .put(EndPoint.BYBOOKID)
                .then().log().ifValidationFails()
                .extract().response();
    }

    public static Response fetchBookById(Map<String, Object> details, String token) {
        return getBaseRequest(token)
                .pathParam("book_id", details.get("createdBookId"))
                .get(EndPoint.BYBOOKID)
                .then().log().ifValidationFails()
                .extract().response();
    }

    public static List<Response> fetchAllBooks(String token) {
        Response response = getBaseRequest(token)
                .get(EndPoint.ADDNEWBOOK)
                .then().log().ifValidationFails()
                .extract().response();

        return Collections.singletonList(response);
    }

    public static Response removeBookById(String id, String token) {
        return getBaseRequest(token)
                .pathParam("book_id", id)
                .delete(EndPoint.BYBOOKID)
                .then().log().ifValidationFails()
                .extract().response();
    }
}

package Steps;

import data.BookstoreContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.testng.Assert;

import api_modules.Api_Book_module;
import api_modules.Api_User_module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserStepDefs {
    HashMap<String, Object> bookDetails = new HashMap<>();
    List<HashMap<String, Object>> allBooksList = new ArrayList<>();
    private final BookstoreContext bookStoreContext;

    public UserStepDefs() {
        this.bookStoreContext = new BookstoreContext();
    }

    @Given("^the user signs up with a unique email and password$")
    public void userSignsUpWithUniqueEmailAndPassword() {
        // Can prepare or reset signup context here if needed
    }

    @When("^the sign up request is submitted with (valid|newPasswordOnly|sameEmail) credentials$")
    public void signUpRequestIsSubmittedWithCredentials(String condition) {
        if (condition.equalsIgnoreCase("valid")) {
            bookStoreContext.setValidEmailUsed(Api_User_module.generateEmailAndPassword(10) + "@gmail.com");
            bookStoreContext.setValidPasswordUsed(Api_User_module.generateEmailAndPassword(8));
        } else if (condition.equalsIgnoreCase("newPasswordOnly")) {
            if (bookStoreContext.getValidEmailUsed() == null) {
                bookStoreContext.setValidEmailUsed(Api_User_module.generateEmailAndPassword(10) + "@gmail.com");
            }
            bookStoreContext.setValidPasswordUsed(Api_User_module.generateEmailAndPassword(8));
        } else if (condition.equalsIgnoreCase("sameEmail")) {
            if (bookStoreContext.getValidEmailUsed() == null) {
                bookStoreContext.setValidEmailUsed(Api_User_module.generateEmailAndPassword(10) + "@gmail.com");
                bookStoreContext.setValidPasswordUsed(Api_User_module.generateEmailAndPassword(8));
            }
        }

        Response response = Api_User_module.signUp(bookStoreContext.getValidEmailUsed(), bookStoreContext.getValidPasswordUsed(), bookStoreContext);
        bookStoreContext.setSignUpResponse(response);
    }

    @Then("^the response code should be (\\d+) and the response message should (?:confirm|indicate|be) \"([^\"]*)\"$")
    public void validateResponseCodeAndMessage(int statusCode, String expectedMsg) {
        Assert.assertEquals(bookStoreContext.getSignUpResponse().getStatusCode(), statusCode, "Sign up status code mismatch");
        if (statusCode == 200) {
            String actualMsg = bookStoreContext.getSignUpResponse().getBody().jsonPath().getString("message");
            Assert.assertEquals(actualMsg, expectedMsg, "Success message mismatch");
        } else if (statusCode == 400 || statusCode == 422) {
            String actualMsg = bookStoreContext.getSignUpResponse().getBody().jsonPath().getString("detail");
            Assert.assertEquals(actualMsg, expectedMsg, "Error message mismatch");
        }
    }

    @When("^the user logs in with (valid|noSignUpUser|missingParam) credentials$")
    public void userLogsInWithCredentials(String condition) {
        if (condition.equalsIgnoreCase("valid")) {
            if (bookStoreContext.getValidEmailUsed() == null || bookStoreContext.getValidPasswordUsed() == null) {
                bookStoreContext.setValidEmailUsed(Api_User_module.generateEmailAndPassword(10) + "@gmail.com");
                bookStoreContext.setValidPasswordUsed(Api_User_module.generateEmailAndPassword(8));
            }
        } else if (condition.equalsIgnoreCase("noSignUpUser")) {
            bookStoreContext.setValidEmailUsed(Api_User_module.generateEmailAndPassword(10) + "@gmail.com");
            bookStoreContext.setValidPasswordUsed(Api_User_module.generateEmailAndPassword(8));
        } else if (condition.equalsIgnoreCase("missingParam")) {
            bookStoreContext.setValidEmailUsed(null);
            bookStoreContext.setValidPasswordUsed(null);
        }

        Response response = Api_User_module.login(bookStoreContext.getValidEmailUsed(), bookStoreContext.getValidPasswordUsed());
        bookStoreContext.setLogInResponse(response);
    }

    @Then("^the login response code should be (\\d+) and the response should indicate (successLogin|incorrectCredentials|missingParam)$")
    public void verifyLoginResponse(int statusCode, String condition) {
        Assert.assertEquals(bookStoreContext.getLogInResponse().getStatusCode(), statusCode, "Login status code mismatch");

        switch (condition) {
            case "successLogin":
                String token = bookStoreContext.getLogInResponse().jsonPath().getString("access_token");
                Assert.assertNotNull(token, "Access token missing");
                bookStoreContext.setAccessToken("Bearer " + token);
                Assert.assertEquals(bookStoreContext.getLogInResponse().jsonPath().getString("token_type"), "bearer", "Token type mismatch");
                break;

            case "incorrectCredentials":
                Assert.assertEquals(bookStoreContext.getLogInResponse().getStatusLine(), "HTTP/1.1 400 Bad Request", "Status line mismatch for incorrect credentials");
                Assert.assertEquals(bookStoreContext.getLogInResponse().jsonPath().getString("detail"), "Incorrect email or password", "Error message mismatch");
                break;

            case "missingParam":
                Assert.assertEquals(bookStoreContext.getLogInResponse().getStatusLine(), "HTTP/1.1 422 Unprocessable Entity", "Status line mismatch for missing param");
                Assert.assertEquals(bookStoreContext.getLogInResponse().jsonPath().getString("detail[0].type"), "missing", "Error type mismatch");
                Assert.assertEquals(bookStoreContext.getLogInResponse().jsonPath().getString("detail[0].msg"), "Field required", "Error message mismatch");
                break;

            default:
                Assert.fail("Unknown login condition: " + condition);
        }
    }

    @Given("^the user is logged in and adds a new book to the store$")
    public void userAddsNewBookAfterLogin() {
        Long uniqueId = System.nanoTime();
        bookDetails.put("bookName", "Book Title " + uniqueId);
        bookDetails.put("author", "Book Author " + uniqueId);
        bookDetails.put("published_year", uniqueId);
        bookDetails.put("book_summary", "Summary for the book " + uniqueId);
        allBooksList.add(new HashMap<>(bookDetails));
    }

    @When("^the user adds a new book to the store with valid login token$")
    public void userAddsNewBookWithValidToken() {
        Response response = Api_Book_module.addNewBook(bookDetails, bookStoreContext.getAccessToken(), bookStoreContext);
        bookStoreContext.setAddBookResponse(response);
    }

    @Then("^the response after adding the new book should be (success|failure)$")
    public void verifyResponseAfterAddingBook(String condition) {
        if (condition.equalsIgnoreCase("success")) {
            Response resp = bookStoreContext.getAddBookResponse();
            Assert.assertNotNull(resp.getBody().jsonPath().get("id"), "Book ID not generated");
            bookDetails.put("createdBookId", resp.getBody().jsonPath().get("id"));

            Assert.assertEquals(resp.getBody().jsonPath().get("name"), bookDetails.get("bookName"), "Book name mismatch");
            Assert.assertEquals(resp.getBody().jsonPath().get("author"), bookDetails.get("author"), "Author mismatch");
            Assert.assertEquals(resp.getBody().jsonPath().get("published_year"), bookDetails.get("published_year"), "Published year mismatch");
            Assert.assertEquals(resp.getBody().jsonPath().get("book_summary"), bookDetails.get("book_summary"), "Book summary mismatch");
        } else {
            Assert.fail("Add book failed, condition: " + condition);
        }
    }

    @When("^the user edits the (name|author|bookSummary|published_year|noAccessToken) of the added book and verifies the response$")
    public void editBookDetailsAndVerifyResponse(String editField) {
        if (editField.equalsIgnoreCase("name")) {
            bookDetails.put("bookName", "Edited Book Name");
        } else if (editField.equalsIgnoreCase("author")) {
            bookDetails.put("author", "Edited Book Author");
        } else if (editField.equalsIgnoreCase("bookSummary")) {
            bookDetails.put("book_summary", "Edited book summary via update");
        } else if (editField.equalsIgnoreCase("published_year")) {
            bookDetails.put("published_year", System.nanoTime());
        }

        if (editField.equalsIgnoreCase("noAccessToken")) {
            bookStoreContext.setEditBookResponse(Api_Book_module.editTheBook(bookDetails, null));
        } else {
            bookStoreContext.setEditBookResponse(Api_Book_module.editTheBook(bookDetails, bookStoreContext.getAccessToken()));
        }
    }

    @Then("^the response after updating the book should have status code (\\d+)$")
    public void verifyResponseAfterBookUpdate(int expectedStatusCode) {
        Assert.assertEquals(bookStoreContext.getEditBookResponse().getStatusCode(), expectedStatusCode, "Update book status code mismatch");

        switch (expectedStatusCode) {
            case 200:
                Assert.assertEquals(bookStoreContext.getEditBookResponse().getStatusLine(), "HTTP/1.1 200 OK", "Status line mismatch for 200 OK");
                break;
            case 400:
                Assert.assertEquals(bookStoreContext.getEditBookResponse().getStatusLine(), "HTTP/1.1 400 Bad Request", "Status line mismatch for 400 Bad Request");
                break;
            case 403:
                Assert.assertEquals(bookStoreContext.getEditBookResponse().getStatusLine(), "HTTP/1.1 403 Forbidden", "Status line mismatch for 403 Forbidden");
                break;
            default:
                Assert.fail("Unexpected status code: " + expectedStatusCode);
        }
    }

    @When("^the user deletes the last added book with (validToken|noAccessToken) and verifies the response$")
    public void userDeletesLastAddedBookAndVerifiesResponse(String tokenCondition) {
        String accessToken = tokenCondition.equalsIgnoreCase("validToken") ? bookStoreContext.getAccessToken() : null;
        Response deleteResponse = Api_Book_module.deleteBook(bookDetails, accessToken);
        bookStoreContext.setDeleteBookResponse(deleteResponse);
    }

    @Then("^the response after deleting the book should have status code (\\d+)$")
    public void verifyResponseAfterDeletingBook(int expectedStatusCode) {
        Assert.assertEquals(bookStoreContext.getDeleteBookResponse().getStatusCode(), expectedStatusCode, "Delete book status code mismatch");
        if (expectedStatusCode == 200) {
            Assert.assertEquals(bookStoreContext.getDeleteBookResponse().getStatusLine(), "HTTP/1.1 200 OK", "Status line mismatch for 200 OK");
        } else if (expectedStatusCode == 403) {
            Assert.assertEquals(bookStoreContext.getDeleteBookResponse().getStatusLine(), "HTTP/1.1 403 Forbidden", "Status line mismatch for 403 Forbidden");
        }
    }

    @When("^the user retrieves all books from the bookstore$")
    public void userRetrievesAllBooks() {
        Response response = Api_Book_module.getAllBooks();
        bookStoreContext.setGetAllBooksResponse(response);
    }

    @Then("^the response for retrieving all books should have status code (\\d+)$")
    public void verifyResponseForGetAllBooks(int expectedStatusCode) {
        Assert.assertEquals(bookStoreContext.getGetAllBooksResponse().getStatusCode(), expectedStatusCode, "Get all books status code mismatch");
        if (expectedStatusCode == 200) {
            Assert.assertTrue(bookStoreContext.getGetAllBooksResponse().getBody().asString().contains("id"), "Response does not contain book IDs");
        }
    }
}

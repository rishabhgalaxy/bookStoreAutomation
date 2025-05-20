package data;

import io.restassured.response.Response;
import java.util.List;

/**
 * Container class to hold test data and API responses
 * related to Bookstore test workflow.
 */
public class BookstoreContext {

    private String userEmail;
    private String userPassword;
    private String jwtToken;

    private Response registrationResponse;
    private Response authenticationResponse;
    private Response createBookResponse;
    private Response updateBookResponse;
    private Response fetchBookByIdResponse;
    private List<Response> fetchAllBooksResponses;
    private Response removeBookResponse;

    // Getters & Setters
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public Response getRegistrationResponse() {
        return registrationResponse;
    }

    public void setRegistrationResponse(Response registrationResponse) {
        this.registrationResponse = registrationResponse;
    }

    public Response getAuthenticationResponse() {
        return authenticationResponse;
    }

    public void setAuthenticationResponse(Response authenticationResponse) {
        this.authenticationResponse = authenticationResponse;
    }

    public Response getCreateBookResponse() {
        return createBookResponse;
    }

    public void setCreateBookResponse(Response createBookResponse) {
        this.createBookResponse = createBookResponse;
    }

    public Response getUpdateBookResponse() {
        return updateBookResponse;
    }

    public void setUpdateBookResponse(Response updateBookResponse) {
        this.updateBookResponse = updateBookResponse;
    }

    public Response getFetchBookByIdResponse() {
        return fetchBookByIdResponse;
    }

    public void setFetchBookByIdResponse(Response fetchBookByIdResponse) {
        this.fetchBookByIdResponse = fetchBookByIdResponse;
    }

    public List<Response> getFetchAllBooksResponses() {
        return fetchAllBooksResponses;
    }

    public void setFetchAllBooksResponses(List<Response> fetchAllBooksResponses) {
        this.fetchAllBooksResponses = fetchAllBooksResponses;
    }

    public Response getRemoveBookResponse() {
        return removeBookResponse;
    }

    public void setRemoveBookResponse(Response removeBookResponse) {
        this.removeBookResponse = removeBookResponse;
    }
}

Feature: Comprehensive Bookstore API automation
  Validate user registration, authentication, and full lifecycle of book management including creation, updates, retrieval, deletion, and error handling

  @SignUpDelete
  Scenario: User registration, login, book lifecycle management, and response validation
    Given the user signs up with valid credentials
    Then the response code should be 200
    And the response message should confirm successful user creation

    When the user logs in with valid credentials
    Then the login response code should be 200
    And the response should indicate successful login with a valid token

    When the user adds a new book with a valid token
    Then the response should confirm successful book addition

    When the user updates the book's name
    Then the response code should be 200
    And the updated book details should be reflected correctly in the response

    When the user retrieves the book details by book ID
    Then the response should contain the correct book details

    When the user deletes the book by book ID
    Then the response should confirm successful deletion

    When the user attempts to delete the same book again
    Then the response should indicate the book was not found

    When the user attempts to retrieve the deleted book by book ID
    Then the response should indicate the book is unavailable

  @EditBook @AuthenticationVerificationForEditApi
  Scenario: Validate editing book attributes and enforce authentication on edit operations
    Given the user signs up and logs in with valid credentials
    When the user adds a new book with a valid token
    Then the response should confirm successful book addition

    When the user attempts to update the book without a valid access token
    Then the response status should be 403 (Forbidden) 

    When the user updates the book's name
    Then the response code should be 200
    And the updated name should be reflected in the response

    When the user retrieves the book details by book ID
    Then the response should contain the updated book details

    When the user updates the book's author
    Then the response code should be 200
    And the updated author should be reflected in the response

    When the user updates the book's summary
    Then the response code should be 200
    And the updated summary should be reflected in the response

    When the user updates the book's published year
    Then the response code should be 200
    And the updated year should be reflected in the response

    When the user retrieves the book details by book ID after each update
    Then the response should reflect the latest updated details accordingly

  @FetchBooks
  Scenario: Add multiple books and verify their presence in the complete book list
    Given the user signs up and logs in with valid credentials

    When the user adds multiple new books with a valid token
    Then each response should confirm successful book addition

    When the user fetches all books
    Then the response should list all the newly added books with correct details

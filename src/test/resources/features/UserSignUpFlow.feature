@SignUpFlow
Feature: User registration and login functionality with input validation for Book Store APIs

  @SignUpBookStore
  Scenario: Register a new user, verify success, and validate duplicate registration rejection
    Given the user signs up with a unique email and password
    When the sign up request is submitted with valid credentials
    Then the response code should be 200
    And the response message should confirm "User created successfully"

    When the user attempts to sign up again with the same email
    Then the response code should be 400
    And the response message should indicate "Email already registered"

    When the user attempts to sign up using only a new password (no email change)
    Then the response code should be 400
    And the response message should indicate "Email already registered"

  @SignUpLogin
  Scenario: Register a new user and login successfully to receive a token
    Given the user signs up with a unique email and password
    When the sign up request is submitted with valid credentials
    Then the response code should be 200
    And the response message should confirm "User created successfully"

    When the user logs in with the same valid credentials
    Then the login response code should be 200
    And the response should indicate successful login with a token

  @BeforeLoginSignUp
  Scenario: Attempt login with unregistered credentials and verify error response
    When the user tries to log in with unregistered credentials
    Then the response code should be 400
    And the response message should indicate "Incorrect credentials"

  @LoginAPIValidationWithMissingParam
  Scenario: Attempt login with missing parameters and validate the error response
    When the user tries to log in with incomplete credentials
    Then the response code should be 422
    And the response message should indicate "Missing parameters"

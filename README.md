Bookstore  Automation with CI/CD Integration
Overview
This project is an end-to-end automation framework built for a Bookstore application using Cucumber BDD + TestNG in Java. It integrates with Jenkins for CI/CD and uses Allure for reporting.

Tech Stack Used
IDE: IntelliJ IDEA

Language: Java 11 or above

Framework: Rest Assured with Cucumber BDD for behavior-driven API testing

Build Tool: Maven, used for managing dependencies and integrating with CI/CD

Test Execution: TestNG, used for test execution with support for parallel runs, retries, and listeners

Reporting: Allure Report, used for generating visually rich test reports with categorization and history tracking

Why This Stack?
Why TestNG over JUnit?
TestNG is better suited for projects not using Spring Boot.

It provides better control over test execution such as retries, parallel runs, and custom listeners.

It integrates easily with Jenkins and other CI/CD tools.

Why Allure?
Allure offers a rich and user-friendly interface for viewing test results.

It categorizes failures clearly into test failures (e.g., assertion errors) and product/environment failures (e.g., 5xx errors).

It maintains test history and trends across builds.

It integrates easily with CI pipelines like Jenkins and GitHub Actions.

How to Set Up and Run the Project
Prerequisites
Java 11 or above

Maven installed

Steps to Set Up
Create a new Maven project or clone the existing one from GitHub.

Fork the development (Dev) repository and set it up on your local machine.

The setup steps for the Dev repo will be available in its README file.

The following API endpoints are automated in this project:

POST /signup: To register a user

POST /login: To log in and generate a token

POST /books: To create a new book

PUT /books/{id}: To update an existing book

GET /books/{id}: To retrieve a book by ID

GET /books: To fetch all books

DELETE /books/{id}: To delete a book

To execute the automation suite, run the Cucumber TestNG runner.

After test execution, Allure reports will be generated in the target/allure-results folder.

CI/CD Integration (Jenkins)
Prerequisites
Jenkins installed on your local system

Necessary Jenkins plugins installed:

Git

GitHub Integration

Pipeline

Maven Integration

Allure Jenkins Plugin

Ngrok installed (for exposing Jenkins on localhost to GitHub)

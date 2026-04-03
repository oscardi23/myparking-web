MyParking Web

Web-based parking management system built with Java and Spring Boot.

Description

MyParking Web is a backend-focused application designed to manage parking operations, including vehicle registration, authentication, and access control.

The system implements secure user authentication with email verification and follows a modular architecture for scalability and maintainability.

Features
User registration with email verification
Authentication with Spring Security
Email service integration (verification codes)
Modular architecture (Controller, Service, Repository)
Exception handling and DTO pattern
Tailwind CSS integration (for views)
Ready for frontend integration with Thymeleaf and JavaScript
Technologies
Java
Spring Boot
Spring Security
Thymeleaf
JavaScript
Tailwind CSS
Maven
lombok
Project Structure

backend/

controllers
services
repositories
models
dtos
security
exceptions
utils
 Branches
main → Stable version
backend → Backend development
frontend → Views and templates (Thymeleaf)
Status

Project in development.

Currently implemented:

Authentication system
Email verification
Base parking logic

Pending:

Controllers for parking module
Frontend views and UI
Setup
Clone the repository
Configure environment variables
Set up your database (if applicable)
Run the Spring Boot application
Security Note

Sensitive configuration files (like application.properties) are not included in this repository.

Author

Oscar Diaz

# Spring Boot Boilerplate Docker Portainer

This repository is a boilerplate for Spring Boot applications, including permission services, authentication, linting, test coverage, and CI/CD pipelines for automated deployment.

## Technologies Used

This project is built following the **Clean Architecture** principles with **Java 21**.

- **Spring Boot** - Backend development framework
- **Flyway** - Database migration
- **Spotless** - Linting tool to maintain code standards
- **JaCoCo** - Test coverage
- **Maven** - Dependency manager and build automation tool

## Docs

You can find the complete API documentation at the following Postman link:

[![Running on postman](https://run.pstmn.io/button.svg)](https://documenter.getpostman.com/view/37022898/2sAYXBFzV5)

## Available Commands

```json
{
    "build": "mvn clean package -DskipTests",
    "install:java:dependencies": "mvn install -DskipTests",
    "start:dev": "mvn spring-boot:run",
    "db:migrate": "mvn flyway:migrate",
    "lint:test": "mvn spotless:check",
    "lint:fix": "mvn spotless:apply",
    "test": "mvn test",
    "test:coverage": "mvn test jacoco:report"
}
```

### Command Explanation

- **`npm run build`**: Compiles and packages the project without running tests.
- **`npm run install:java:dependencies`**: Installs Java dependencies without running tests.
- **`npm run start:dev`**: Starts the server in development mode.
- **`npm run db:migrate`**: Applies database migrations.
- **`npm run lint:test`**: Checks code formatting standards.
- **`npm run lint:fix`**: Automatically fixes code formatting issues.
- **`npm run test`**: Runs automated tests.
- **`npm run test:coverage`**: Generates a test coverage report.

## CI/CD Pipeline

The project includes a configured pipeline for:

- **CI (Continuous Integration)**:
  - Checks code linting.
  - Runs tests and generates a test coverage report.
- **CD (Continuous Deployment)**:
  - Builds the application.
  - Deploys to the configured environment.

## By Gabriel Soares Maciel
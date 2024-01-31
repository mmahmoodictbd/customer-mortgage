# Mortgage System

## Introduction

The Mortgage System is an application that manages mortgages and customers. It provides functionality 
to create, read, update, and delete (CRUD) operations for mortgages and customers.

## Table of Contents

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
	- [Prerequisites](#prerequisites)
	- [Installation](#installation)
- [API Documentation](#api-documentation)
- [Testing](#testing)
- [License](#license)

## Features

- Create, retrieve, update, and delete mortgages.
- Create, retrieve, and delete customers.
- Fetch age information from Agify API based on customer name.

## Technologies Used

- Kotlin / Java 17
- Spring Boot
- Spring Data JPA
- Spring MVC
- Spring Resilience4j
- Hibernate
- H2 Database (for testing)
- Jackson (for JSON processing)
- Resilience4j (for circuit breaking)
- Mockito (for mocking in tests)
- JUnit (for unit testing)
- Spring Test (for integration testing)

## Getting Started

### Prerequisites

- JDK 17 (OpenJDK Runtime Environment Temurin-17.0.8+7 (build 17.0.8+7))
- Gradle

### Installation

1. Clone the repository:

    ```bash
    git clone https://github.com/mmahmoodictbd/customer-mortgage.git
    ```

2. Build the project:

    ```bash
    cd customer-mortgage
    ./gradlew clean build
    ```

3. Run the application:

    ```bash
    ./gradlew bootRun
    ```


## API Documentation
Swagger API Documentation: http://localhost:8080/swagger-ui/index.html

## License

This project is licensed under the [MIT License](LICENSE).


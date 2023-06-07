# Project Name

The project is a Java application built using IntelliJ IDEA, Maven, Spring Boot, MySQL, and Amazon Corretto JDK 17. It provides a RESTful API for managing Employee and Company data in a MySQL database. The application follows a layered architecture pattern, with separate components for entities, repositories, services, controllers, and global exception handling.

## Table of Contents

- [Project Introduction](#project-introduction)
- [Key Features](#key-features)
- [Technologies Used](#technologies-used)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
  - [Configuration](#configuration)
  - [Build and Run](#build-and-run)
- [API Endpoints](#api-endpoints)
- [Global Exception Handling](#global-exception-handling)
- [Database Views](#database-views)
- [Stored Procedures](#stored-procedures)
- [Contributing](#contributing)
- [License](#license)

## Project Introduction

The project is a Java application built using IntelliJ IDEA, Maven, Spring Boot, MySQL, and Amazon Corretto JDK 17. It provides a RESTful API for managing Employee and Company data in a MySQL database. The application follows a layered architecture pattern, with separate components for entities, repositories, services, controllers, and global exception handling.

## Key Features

- **Employee Management**: The application allows CRUD (Create, Read, Update, Delete) operations for managing Employee records.
- **Company Management**: The application provides functionality for CRUD operations on Company records.
- **API Endpoints**: The RESTful API exposes endpoints to interact with the Employee and Company resources.
- **Database Integration**: The application integrates with a MySQL database to store and retrieve data.
- **Global Exception Handling**: Global exception handlers are implemented to handle KeyNotFoundException, BadRequestException, and INTERNAL_SERVER_ERROR gracefully.

## Technologies Used

- Java
- IntelliJ IDEA
- Maven
- Spring Boot
- MySQL
- Amazon Corretto JDK 17

## Project Structure

The project follows a standard Maven project structure with the following key components:

- **Entities**: Java classes (`Employee` and `Company`) representing the Employee and Company entities. These classes define the structure and attributes of the corresponding database tables.

- **Repositories**: Interfaces (`EmployeeRepository` and `CompanyRepository`) extending Spring Data interfaces. These interfaces provide convenient methods for interacting with the database, such as querying and persisting entities.

- **Services**: Interfaces (`IEmployeeService` and `ICompanyService`) and their implementations (`EmployeeService` and `CompanyService`). These components encapsulate the business logic and interact with the repositories to perform CRUD operations.

- **Controllers**: Java classes (`EmployeeController` and `CompanyController`) responsible for handling incoming HTTP requests, invoking the appropriate service methods, and returning the corresponding HTTP responses.

- **Exceptions**: Custom exception classes (`KeyNotFoundException` and `BadRequestException`) that represent specific error scenarios in the application.

- **Global Exception Handling**: A global exception handler class (`GlobalExceptionHandler`) that handles exceptions thrown by the application and returns appropriate HTTP responses.

## Getting Started

Follow the steps below to set up and run the project locally.

### Prerequisites

- Amazon Corretto JDK 17
- MySQL database

### Installation

1. Clone the project repository:

   ```bash
   git clone https://github.com/your-username/project-name.git


2. Open the project in IntelliJ IDEA.
Configuration

1. Configure the MySQL database connection in the application.properties file located in the src/main/resources directory.
propertiesCopy code
 1. spring.datasource.url=jdbc:mysql://localhost:3306/your-database-name 
 2. spring.datasource.username=your-username 
 3. spring.datasource.password=your-password 

### Build and Run

1. Build the project using Maven. Open the Maven toolbar in IntelliJ IDEA, navigate to Lifecycle, and click on install.
2. Run the application using the Spring Boot Maven plugin. Open the Maven toolbar in IntelliJ IDEA, navigate to Plugins, spring-boot, and click on spring-boot:run.
3. Access the API endpoints using tools like cURL, Postman, or a web browser.

### API Endpoints

 1. The API exposes the following endpoints:

* GET api/employees: Get all employees.
* GET api/employees/{id}: Get an employee by ID.
* POST api/employees: Create a new employee.
* PUT api/employees/{id}: Update an existing employee.
* DELETE api/employees/{id}: Delete an employee by ID.
* GET api/companies: Get all companies.
* GET api/companies/{id}: Get a company by ID.
* POST api/companies: Create a new company.
* PUT api/companies/{id}: Update an existing company.
* DELETE api/companies/{id}: Delete a company by ID.

### Global Exception Handling

   The project includes a global exception handling mechanism to handle common error scenarios. Two custom exception classes, KeyNotFoundException and BadRequestException, are provided to represent specific error situations. The GlobalExceptionHandler class captures and handles these exceptions, returning appropriate HTTP responses with relevant error details.

## Database Views

The project includes two database views:

1.  Employee View

A view that retrieves the ID, email, first name, and last name of all employees.

```sql
CREATE ALGORITHM = UNDEFINED DEFINER = `root`@`localhost` SQL SECURITY DEFINER VIEW `testdb2`.`all_employees_view` AS
SELECT `testdb2`.`employees`.`id` AS `id`,
       `testdb2`.`employees`.`email` AS `email`,
       `testdb2`.`employees`.`first_name` AS `first_name`,
       `testdb2`.`employees`.`last_name` AS `last_name`
FROM `testdb2`.`employees`;
```
2. Company View

A view that retrieves the ID, address, and name of all companies.


```sql
CREATE ALGORITHM = UNDEFINED DEFINER = `root`@`localhost` SQL SECURITY DEFINER VIEW `testdb2`.`all_companies_view` AS
SELECT `testdb2`.`companies`.`id` AS `id`,
       `testdb2`.`companies`.`address` AS `address`,
       `testdb2`.`companies`.`name` AS `name`
FROM `testdb2`.`companies`;
```
### Stored Procedures

The project includes two stored procedures:

1. Add Employee

A stored procedure that adds a new employee to the employees table.

```sql
CREATE DEFINER=`root`@`localhost` PROCEDURE `add_employee`(IN first_name VARCHAR(50), IN last_name VARCHAR(50), IN email VARCHAR(100))
BEGIN
  INSERT INTO employees (first_name, last_name, email) VALUES (first_name, last_name, email);
END
```
2. Add Company

A stored procedure that adds a new company to the companies table.

```sql
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_company`(IN name VARCHAR(255), IN address VARCHAR(255))
BEGIN
  INSERT INTO companies (name, address) VALUES (name, address);
END
```
### Contributing

  Contributions to this project are welcome. If you encounter any issues, have suggestions for improvements, or would like to add new features, please feel free to submit a pull request or open an issue on the project's GitHub repository.


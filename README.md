# Database CRUD Application

A Java standalone application that performs CRUD operations on both MySQL and Oracle databases.

## Prerequisites

- Java 11 or higher
- Maven
- MySQL Server (for MySQL operations)
- Oracle Database (for Oracle operations)

## Setup Instructions

1. Clone the repository
2. Configure your database credentials in the `DatabaseCRUD.java` file:
   - For MySQL: Update the URL, username, and password in the MySQL case
   - For Oracle: Update the URL, username, and password in the Oracle case

3. Build the project:
   ```bash
   mvn clean install
   ```

4. Run the application:
   ```bash
   mvn exec:java -Dexec.mainClass="com.example.DatabaseCRUD"
   ```

## Features

- Connect to either MySQL or Oracle database
- Create (Insert) records
- Read (Select) records
- Update records
- Delete records

## Database Schema

The application creates a `users` table with the following structure:
- id (INT, PRIMARY KEY, AUTO_INCREMENT)
- name (VARCHAR)
- email (VARCHAR)
- age (INT)

## Error Handling

The application includes proper error handling and logging using SLF4J.

package com.example;

import java.sql.*;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseCRUD {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseCRUD.class);
    private static Connection connection = null;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            System.out.println("Choose database type:");
            System.out.println("1. MySQL");
            System.out.println("2. Oracle");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            String url = "";
            String username = "";
            String password = "";

            switch (choice) {
                case 1:
                    url = "jdbc:mysql://localhost:3306/testdb";
                    username = "root";
                    password = "password";
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    break;
                case 2:
                    url = "jdbc:oracle:thin:@localhost:1521:xe";
                    username = "system";
                    password = "oracle";
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    break;
                default:
                    System.out.println("Invalid choice");
                    return;
            }

            connection = DriverManager.getConnection(url, username, password);
            logger.info("Connected to database successfully");

            createTable();
            showMenu();

        } catch (Exception e) {
            logger.error("Error: " + e.getMessage(), e);
        } finally {
            closeConnection();
        }
    }

    private static void createTable() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT PRIMARY KEY AUTO_INCREMENT," +
                "name VARCHAR(100)," +
                "email VARCHAR(100)," +
                "age INT)";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
            logger.info("Table created successfully");
        }
    }

    private static void showMenu() {
        while (true) {
            System.out.println("\nDatabase Operations:");
            System.out.println("1. Create (Insert)");
            System.out.println("2. Read (Select)");
            System.out.println("3. Update");
            System.out.println("4. Delete");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    createRecord();
                    break;
                case 2:
                    readRecords();
                    break;
                case 3:
                    updateRecord();
                    break;
                case 4:
                    deleteRecord();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    private static void createRecord() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String sql = "INSERT INTO users (name, email, age) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setInt(3, age);
            pstmt.executeUpdate();
            logger.info("Record created successfully");
        } catch (SQLException e) {
            logger.error("Error creating record: " + e.getMessage());
        }
    }

    private static void readRecords() {
        String sql = "SELECT * FROM users";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("\nUsers:");
            while (rs.next()) {
                System.out.printf("ID: %d, Name: %s, Email: %s, Age: %d%n",
                        rs.getInt("id"), rs.getString("name"),
                        rs.getString("email"), rs.getInt("age"));
            }
        } catch (SQLException e) {
            logger.error("Error reading records: " + e.getMessage());
        }
    }

    private static void updateRecord() {
        System.out.print("Enter ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        System.out.print("Enter new name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new email: ");
        String email = scanner.nextLine();
        System.out.print("Enter new age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String sql = "UPDATE users SET name = ?, email = ?, age = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setInt(3, age);
            pstmt.setInt(4, id);
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                logger.info("Record updated successfully");
            } else {
                logger.info("No record found with ID: " + id);
            }
        } catch (SQLException e) {
            logger.error("Error updating record: " + e.getMessage());
        }
    }

    private static void deleteRecord() {
        System.out.print("Enter ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                logger.info("Record deleted successfully");
            } else {
                logger.info("No record found with ID: " + id);
            }
        } catch (SQLException e) {
            logger.error("Error deleting record: " + e.getMessage());
        }
    }

    private static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                logger.info("Database connection closed");
            } catch (SQLException e) {
                logger.error("Error closing connection: " + e.getMessage());
            }
        }
    }
}

import java.sql.*;
import java.util.Scanner;

public class LibraryManagementSystem {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/library";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS)) {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("\nLibrary Management System");
                System.out.println("1. Add Book");
                System.out.println("2. View Books");
                System.out.println("3. Update Book Availability");
                System.out.println("4. Delete Book");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        addBook(conn, scanner);
                        break;
                    case 2:
                        viewBooks(conn);
                        break;
                    case 3:
                        updateBookAvailability(conn, scanner);
                        break;
                    case 4:
                        deleteBook(conn, scanner);
                        break;
                    case 5:
                        System.out.println("Exiting system.");
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addBook(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter title: ");
        String title = scanner.next();
        System.out.print("Enter author: ");
        String author = scanner.next();

        String sql = "INSERT INTO books (title, author, isAvailable) VALUES (?, ?, TRUE)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.executeUpdate();
            System.out.println("Book added successfully.");
        }
    }

    private static void viewBooks(Connection conn) throws SQLException {
        String sql = "SELECT * FROM books";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Books in the library:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Title: " + rs.getString("title") +
                        ", Author: " + rs.getString("author") +
                        ", Available: " + rs.getBoolean("isAvailable"));
            }
        }
    }

    private static void updateBookAvailability(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter book ID to update availability: ");
        int id = scanner.nextInt();
        System.out.print("Enter availability (true/false): ");
        boolean isAvailable = scanner.nextBoolean();

        String sql = "UPDATE books SET isAvailable = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, isAvailable);
            stmt.setInt(2, id);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Book availability updated.");
            } else {
                System.out.println("Book not found.");
            }
        }
    }

    private static void deleteBook(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter book ID to delete: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Book deleted.");
            } else {
                System.out.println("Book not found.");
            }
        }
    }
}

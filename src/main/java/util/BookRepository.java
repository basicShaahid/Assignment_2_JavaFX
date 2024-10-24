package util;

import model.Book;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {

    private static BookRepository instance;

    private BookRepository() {
    }

    public static BookRepository getInstance() {
        if (instance == null) {
            instance = new BookRepository();
        }
        return instance;
    }

    // Method to insert initial books into the database
    public void insertInitialBooks() {
        String sql = "INSERT INTO Books(title, author, physicalCopies, price, soldCopies) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // List of books
            Object[][] books = {
                    {"Absolute Java", "Savitch", 10, 50.0, 142},
                    {"JAVA: How to Program", "Deitel and Deitel", 100, 70.0, 475},
                    {"Computing Concepts with JAVA 8 Essentials", "Horstman", 500, 89.0, 60},
                    {"Java Software Solutions", "Lewis and Loftus", 500, 99.0, 12},
                    {"Java Program Design", "Cohoon and Davidson", 2, 29.0, 86},
                    {"Clean Code", "Robert Martin", 100, 45.0, 300},
                    {"Gray Hat C#", "Brandon Perry", 300, 68.0, 178},
                    {"Python Basics", "David Amos", 1000, 49.0, 79},
                    {"Bayesian Statistics The Fun Way", "Will Kurt", 600, 42.0, 155}
            };

            // Insert each book into the database
            for (Object[] book : books) {
                pstmt.setString(1, (String) book[0]);
                pstmt.setString(2, (String) book[1]);
                pstmt.setInt(3, (Integer) book[2]);
                pstmt.setDouble(4, (Double) book[3]);
                pstmt.setInt(5, (Integer) book[4]);
                pstmt.executeUpdate();
            }

            System.out.println("Books inserted successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to fetch all books from the database
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM Books";

        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Book book = new Book(rs.getInt("id"),  // Fetch the unique id
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("physicalCopies"),
                        rs.getDouble("price"),
                        rs.getInt("soldCopies"));
                books.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return books;
    }

    // Method to add a new book to the database
    public void addBook(Book book) {
        String sql = "INSERT INTO Books(title, author, physicalCopies, price, soldCopies) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setInt(3, book.getPhysicalCopies());
            pstmt.setDouble(4, book.getPrice());
            pstmt.setInt(5, book.getSoldCopies());

            pstmt.executeUpdate();
            System.out.println("Book added successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to update an existing book in the database
    public void updateBook(Book book) {
        String sql = "UPDATE Books SET author = ?, physicalCopies = ?, price = ?, soldCopies = ? WHERE title = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getAuthor());
            pstmt.setInt(2, book.getPhysicalCopies());
            pstmt.setDouble(3, book.getPrice());
            pstmt.setInt(4, book.getSoldCopies());
            pstmt.setString(5, book.getTitle());

            pstmt.executeUpdate();
            System.out.println("Book updated successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to delete a book from the database using bookId
    public void deleteBook(int bookId) {
        String sql = "DELETE FROM Books WHERE id = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);  // Bind the book ID to the query
            pstmt.executeUpdate();
            System.out.println("Book deleted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

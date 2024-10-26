package model;
import util.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;



public class Book {
    private int id;
    private String title;
    private String author;
    private int physicalCopies;
    private double price;
    private int soldCopies;

    public Book(int id, String title, String author, int physicalCopies, double price, int soldCopies) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.physicalCopies = physicalCopies;
        this.price = price;
        this.soldCopies = soldCopies;
    }

    public void insertInitialBooks() {
        String sql = "INSERT INTO Books(title, author, physicalCopies, price, soldCopies) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // List of books
            Object[][] books = {
                    {"Absolute Java", "Savitch", 10, 50, 142},
                    {"JAVA: How to Program", "Deitel and Deitel", 100, 70, 475},
                    {"Computing Concepts with JAVA 8 Essentials", "Horstman", 500, 89, 60},
                    {"Java Software Solutions", "Lewis and Loftus", 500, 99, 12},
                    {"Clean Code", "Robert Martin", 100, 45, 300}
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


    // Getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getPhysicalCopies() {
        return physicalCopies;
    }

    public double getPrice() {
        return price;
    }

    public int getSoldCopies() {
        return soldCopies;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPhysicalCopies(int physicalCopies) {
        this.physicalCopies = physicalCopies;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setSoldCopies(int soldCopies) {
        this.soldCopies = soldCopies;
    }
}

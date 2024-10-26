package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHelper {

    private static final String URL = "jdbc:sqlite:C:/Users/shaah/Desktop/RMIT/Advanced_programming/Advance-programing/Assignment_2_JavaFX/ReadingRoomGUI/src/main/RRLibrary.db";


    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("Connection established.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to the database.");
        }
        return conn;
    }
}

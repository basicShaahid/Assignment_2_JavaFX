package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseHelper {

    private static final String DATABASE_URL = "jdbc:sqlite:C:/Users/shaah/Desktop/RMIT/Advanced_programming/Advance-programing/Assignment_2_JavaFX/ReadingRoomGUI/src/main/RRLibrary.db";


    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DATABASE_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}

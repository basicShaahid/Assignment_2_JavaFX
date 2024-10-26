package util;

import model.Ebook;
import util.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EbooksRepository {

    private static EbooksRepository instance;

    private EbooksRepository() {
    }

    public static EbooksRepository getInstance() {
        if (instance == null) {
            instance = new EbooksRepository();
        }
        return instance;
    }

    public List<Ebook> getAllEbooks() {
        List<Ebook> ebooks = new ArrayList<>();
        String sql = "SELECT * FROM Ebooks";

        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Ebook ebook = new Ebook(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getDouble("price"),
                        rs.getInt("downloads"));
                ebooks.add(ebook);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ebooks;
    }

    public void addEbook(Ebook ebook) {
        String sql = "INSERT INTO Ebooks(title, author, price, downloads) VALUES(?, ?, ?, ?)";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, ebook.getTitle());
            pstmt.setString(2, ebook.getAuthor());
            pstmt.setDouble(3, ebook.getPrice());
            pstmt.setInt(4, ebook.getDownloads());

            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteEbook(int ebookId) {
        String sql = "DELETE FROM Ebooks WHERE id = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ebookId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


package util;

import model.Purchase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PurchasesRepository {

    private static PurchasesRepository instance;

    private PurchasesRepository() {}

    public static PurchasesRepository getInstance() {
        if (instance == null) {
            instance = new PurchasesRepository();
        }
        return instance;
    }

    public List<Purchase> getAllPurchases() {
        List<Purchase> purchases = new ArrayList<>();
        String sql = "SELECT * FROM Purchases";

        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Purchase purchase = new Purchase(
                        rs.getInt("id"),
                        rs.getInt("userId"),
                        rs.getInt("bookId"),
                        rs.getInt("quantity"),  // Corrected from getDouble to getInt
                        rs.getDouble("totalPrice"),
                        rs.getString("purchaseDate")
                );
                purchases.add(purchase);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return purchases;
    }

    public void addPurchase(Purchase purchase) {
        String sql = "INSERT INTO Purchases(userId, bookId, quantity, totalPrice, purchaseDate) VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, purchase.getUserId());
            pstmt.setInt(2, purchase.getBookId());
            pstmt.setInt(3, purchase.getQuantity());
            pstmt.setDouble(4, purchase.getTotalPrice());
            pstmt.setString(5, purchase.getPurchaseDate());

            pstmt.executeUpdate();
            System.out.println("Purchase added successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

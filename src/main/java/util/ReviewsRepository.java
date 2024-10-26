package util;

import model.Review;
import util.DatabaseHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReviewsRepository {

    private static ReviewsRepository instance;

    private ReviewsRepository() {
    }

    public static ReviewsRepository getInstance() {
        if (instance == null) {
            instance = new ReviewsRepository();
        }
        return instance;
    }

    public List<Review> getAllReviewsByBookId(int bookId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM Reviews WHERE bookId = ?";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Review review = new Review(
                        rs.getInt("id"),
                        rs.getInt("bookId"),
                        rs.getInt("userId"),
                        rs.getString("reviewText"),
                        rs.getInt("rating"));
                reviews.add(review);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return reviews;
    }

    public void addReview(Review review) {
        String sql = "INSERT INTO Reviews(bookId, userId, reviewText, rating) VALUES(?, ?, ?, ?)";

        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, review.getBookId());
            pstmt.setInt(2, review.getUserId());
            pstmt.setString(3, review.getReviewText());
            pstmt.setInt(4, review.getRating());

            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


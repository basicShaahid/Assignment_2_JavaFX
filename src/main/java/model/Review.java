package model;

public class Review {
    private int id;
    private int userId;
    private int bookId;
    private String reviewText;
    private int rating;

    public Review(int id, int userId, int bookId, String reviewText, int rating) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.reviewText = reviewText;
        this.rating = rating;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}

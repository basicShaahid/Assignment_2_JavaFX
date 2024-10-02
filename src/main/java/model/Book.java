package model;

public class Book {
    private String title;
    private String author;
    private int physicalCopies;
    private double price;
    private int soldCopies;
    private boolean hasEbook; // Optional attribute if needed

    public Book(String title, String author, int physicalCopies, double price, int soldCopies) {
        this.title = title;
        this.author = author;
        this.physicalCopies = physicalCopies;
        this.price = price;
        this.soldCopies = soldCopies;
        this.hasEbook = true; // Default value (optional)
    }

    // Getters
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

    public boolean hasEbook() {
        return hasEbook;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPhysicalCopies(int physicalCopies) {
        if (physicalCopies >= 0) { // check for books going below zero
            this.physicalCopies = physicalCopies;
        }
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setSoldCopies(int soldCopies) {
        this.soldCopies = soldCopies;
    }

    public void setHasEbook(boolean hasEbook) {
        this.hasEbook = hasEbook;
    }

    @Override
    public String toString() {
        return title + " by " + author + " (" + physicalCopies + " copies available)";
    }
}

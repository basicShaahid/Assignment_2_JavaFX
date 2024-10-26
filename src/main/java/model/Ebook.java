package model;

public class Ebook {
    private int id;
    private String title;
    private String author;
    private double price;
    private int downloads; // New field for downloads

    public Ebook(int id, String title, String author, double price, int downloads) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.downloads = downloads; // Initialize downloads
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDownloads() {
        return downloads; // Getter for downloads
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads; // Setter for downloads
    }
}

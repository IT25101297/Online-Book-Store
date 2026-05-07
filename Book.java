package com.example.onlinebookstore.model;

import java.io.Serializable;

/**
 * Model class representing a Book in the bookstore.
 * Implements Serializable to allow storage in HTTP Sessions (for the shopping cart).
 */
public class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    // --- Book Attributes ---
    private String id;
    private String title;
    private String author;
    private double price;
    private String description;
    private String language;
    private String type;
    private String imageUrl;

    public Book() {}

    /**
     * Constructor for creating a full Book object.
     */
    public Book(String id, String title, String author, double price, String description, String language, String type, String imageUrl) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.description = description;
        this.language = language;
        this.type = type;
        this.imageUrl = imageUrl;
    }

    // --- Getters and Setters ---
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    /**
     * Converts the book object to a pipe-delimited string for file storage.
     */
    @Override
    public String toString() {
        return id + "|" + title + "|" + author + "|" + price + "|" + description + "|" + language + "|" + type + "|" + imageUrl;
    }

    /**
     * Factory method to create a Book object from a pipe-delimited string (used by Repository).
     */
    public static Book fromString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length < 8) return null;
        try {
            return new Book(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]), parts[4], parts[5], parts[6], parts[7]);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}

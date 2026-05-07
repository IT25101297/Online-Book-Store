package com.example.onlinebookstore.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Model class representing a Shopping Cart.
 * Manages the collection of CartItems and performs total calculations.
 */
public class Cart implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Map storing bookId as key and CartItem as value for efficient lookup/updates
    private Map<String, CartItem> items = new HashMap<>();

    /**
     * Adds a book to the cart. Increments quantity if the book is already present.
     */
    public void addItem(Book book) {
        String bookId = book.getId();
        if (items.containsKey(bookId)) {
            CartItem item = items.get(bookId);
            item.setQuantity(item.getQuantity() + 1);
        } else {
            items.put(bookId, new CartItem(book, 1));
        }
    }

    /**
     * Removes a book completely from the cart.
     */
    public void removeItem(String bookId) {
        items.remove(bookId);
    }

    /**
     * Updates the quantity of a book. Removes the item if quantity is set to 0 or less.
     */
    public void updateQuantity(String bookId, int quantity) {
        if (items.containsKey(bookId)) {
            if (quantity <= 0) {
                items.remove(bookId);
            } else {
                items.get(bookId).setQuantity(quantity);
            }
        }
    }

    /**
     * Returns the list of all items currently in the cart.
     */
    public List<CartItem> getItems() {
        return new ArrayList<>(items.values());
    }

    /**
     * Calculates the grand total price of all items in the cart.
     */
    public double getTotal() {
        return items.values().stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();
    }

    /**
     * Calculates the total number of books in the cart (sum of all quantities).
     */
    public int getItemCount() {
        return items.values().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    /**
     * Clears the cart by removing all items.
     */
    public void clear() {
        items.clear();
    }
}

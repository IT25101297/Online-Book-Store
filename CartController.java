package com.example.onlinebookstore.controller;

import com.example.onlinebookstore.model.Book;
import com.example.onlinebookstore.model.Cart;
import com.example.onlinebookstore.service.BookService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller handling Shopping Cart operations.
 * Uses HTTP Session to persist the cart state across requests.
 */
@Controller
@RequestMapping("/cart")
public class CartController {

    private final BookService bookService;

    public CartController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Helper method to retrieve the Cart from the session.
     * Creates a new Cart if one doesn't exist.
     */
    private Cart getCart(HttpSession session) {
        Object sessionCart = session.getAttribute("cart");
        if (sessionCart instanceof Cart) {
            return (Cart) sessionCart;
        } else {
            Cart cart = new Cart();
            session.setAttribute("cart", cart);
            return cart;
        }
    }

    // =========================================================================
    // CART VIEWING & CLEARING
    // =========================================================================

    /**
     * Display the current shopping cart contents.
     */
    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        model.addAttribute("cart", getCart(session));
        return "cart";
    }

    /**
     * Clear all items from the cart.
     */
    @GetMapping("/clear")
    public String clearCart(HttpSession session) {
        Cart cart = getCart(session);
        cart.clear();
        session.setAttribute("cart", cart); // Explicitly update session state
        return "redirect:/cart";
    }

    // =========================================================================
    // CART MODIFICATION (ADD, REMOVE, UPDATE)
    // =========================================================================

    /**
     * ADD (Standard): Adds a book to the cart via full page redirect.
     */
    @GetMapping("/add/{id}")
    public String addToCart(@PathVariable String id, @RequestParam(required = false) String target, HttpSession session) {
        Cart cart = getCart(session);
        bookService.getBookById(id).ifPresent(cart::addItem);
        session.setAttribute("cart", cart); // Explicitly update session state
        
        // Conditional redirect based on 'target' parameter (e.g., 'Buy Now' vs 'Add to Cart')
        if ("cart".equals(target)) {
            return "redirect:/cart";
        }
        return "redirect:/books";
    }

    /**
     * ADD (AJAX): Adds a book to the cart and returns JSON for dynamic UI updates.
     */
    @GetMapping("/api/add/{id}")
    @ResponseBody
    public Map<String, Object> addToCartApi(@PathVariable String id, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            Cart cart = getCart(session);
            bookService.getBookById(id).ifPresent(cart::addItem);
            session.setAttribute("cart", cart); // Explicitly update session state
            
            response.put("count", cart.getItemCount());
            response.put("success", true);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    /**
     * REMOVE: Removes a specific item from the cart.
     */
    @GetMapping("/remove/{id}")
    public String removeFromCart(@PathVariable String id, HttpSession session) {
        Cart cart = getCart(session);
        cart.removeItem(id);
        session.setAttribute("cart", cart); // Explicitly update session state
        return "redirect:/cart";
    }

    /**
     * UPDATE: Modifies the quantity of a specific item in the cart.
     */
    @PostMapping("/update")
    public String updateQuantity(@RequestParam String id, @RequestParam int quantity, HttpSession session) {
        Cart cart = getCart(session);
        cart.updateQuantity(id, quantity);
        session.setAttribute("cart", cart); // Explicitly update session state
        return "redirect:/cart";
    }
}

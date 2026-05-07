package com.example.onlinebookstore.controller;

import com.example.onlinebookstore.model.Book;
import com.example.onlinebookstore.service.BookService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Controller handling all Book-related web requests.
 * Manages the UI for listing, viewing, and managing books.
 */
@Controller
public class BookController {

    private final BookService bookService;
    private final List<String> languages = Arrays.asList("English", "Sinhala", "Tamil", "Spanish", "French", "German", "Japanese");
    private final List<String> types = Arrays.asList("Novel", "Short Story", "Non-fiction", "Biography", "Poetry", "Drama");

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * Home Page redirect.
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // =========================================================================
    // READ OPERATIONS (LIST & DETAILS)
    // =========================================================================

    /**
     * READ: Display list of books with optional filtering by language/type.
     */
    @GetMapping("/books")
    public String listBooks(@RequestParam(required = false) String lang, @RequestParam(required = false) String type, Model model) {
        List<Book> books = bookService.getAllBooks();
        if (lang != null && !lang.isEmpty()) {
            books = books.stream()
                    .filter(b -> b.getLanguage().equalsIgnoreCase(lang))
                    .toList();
            model.addAttribute("currentLanguage", lang);
        }
        if (type != null && !type.isEmpty()) {
            books = books.stream()
                    .filter(b -> b.getType().equalsIgnoreCase(type))
                    .toList();
            model.addAttribute("currentType", type);
        }
        model.addAttribute("books", books);
        model.addAttribute("languages", languages);
        model.addAttribute("types", types);
        return "book-list";
    }

    /**
     * READ: Display specific book details.
     */
    @GetMapping("/books/{id}")
    public String showDetails(@PathVariable String id, Model model) {
        bookService.getBookById(id).ifPresent(book -> model.addAttribute("book", book));
        return "book-details";
    }

    // =========================================================================
    // CREATE / UPDATE OPERATIONS (FORMS & SAVING)
    // =========================================================================

    /**
     * CREATE (UI): Show form to add a new book. (Admin Only)
     */
    @GetMapping("/books/new")
    public String showAddForm(HttpSession session, Model model) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) {
            return "redirect:/login-choice";
        }
        model.addAttribute("book", new Book());
        model.addAttribute("languages", languages);
        model.addAttribute("types", types);
        return "book-form";
    }

    /**
     * UPDATE (UI): Show form to edit an existing book. (Admin Only)
     */
    @GetMapping("/books/edit/{id}")
    public String showEditForm(@PathVariable String id, HttpSession session, Model model) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) {
            return "redirect:/login-choice";
        }
        bookService.getBookById(id).ifPresent(book -> model.addAttribute("book", book));
        model.addAttribute("languages", languages);
        model.addAttribute("types", types);
        return "book-form";
    }

    /**
     * CREATE / UPDATE (POST): Save book data submitted via form. (Admin Only)
     */
    @PostMapping("/books/save")
    public String saveBook(@ModelAttribute Book book, HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) {
            return "redirect:/login-choice";
        }
        bookService.saveBook(book);
        return "redirect:/books";
    }

    // =========================================================================
    // DELETE OPERATION
    // =========================================================================

    /**
     * DELETE: Remove a book from the system. (Admin Only)
     */
    @GetMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable String id, HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) {
            return "redirect:/login-choice";
        }
        bookService.deleteBook(id);
        return "redirect:/books";
    }
}

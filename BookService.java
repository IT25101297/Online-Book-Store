package com.example.onlinebookstore.service;

import com.example.onlinebookstore.model.Book;
import com.example.onlinebookstore.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class acting as a bridge between the Controller and the Repository.
 * Encapsulates business logic for book management.
 */
@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // =========================================================================
    // CRUD BUSINESS LOGIC
    // =========================================================================

    /**
     * READ: Business logic to fetch all available books.
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * READ: Business logic to find a specific book by ID.
     */
    public Optional<Book> getBookById(String id) {
        return bookRepository.findById(id);
    }

    /**
     * CREATE / UPDATE: Business logic to save or modify a book.
     */
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    /**
     * DELETE: Business logic to remove a book.
     */
    public void deleteBook(String id) {
        bookRepository.deleteById(id);
    }
}

package com.example.onlinebookstore.repository;

import com.example.onlinebookstore.model.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository class handling File-based Persistence for Books.
 * This class implements the core data access logic for CRUD operations.
 */
@Repository
public class BookRepository {

    private final String filePath;

    public BookRepository(@Value("${data.file.path}") String filePath) {
        this.filePath = filePath;
        ensureFileExists();
    }

    /**
     * Ensures the data file and parent directories exist.
     */
    private void ensureFileExists() {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // =========================================================================
    // CRUD OPERATIONS (READ)
    // =========================================================================

    /**
     * READ: Retrieves all books from the text file.
     */
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Book book = Book.fromString(line);
                if (book != null) {
                    books.add(book);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return books;
    }

    /**
     * READ: Retrieves a single book by its unique ID.
     */
    public Optional<Book> findById(String id) {
        return findAll().stream()
                .filter(b -> b.getId().equals(id))
                .findFirst();
    }

    // =========================================================================
    // CRUD OPERATIONS (CREATE / UPDATE)
    // =========================================================================

    /**
     * CREATE / UPDATE: Saves a book object. 
     * If ID is missing, it's a CREATE operation (generates UUID).
     * If ID exists, it's an UPDATE operation.
     */
    public Book save(Book book) {
        List<Book> books = findAll();
        if (book.getId() == null || book.getId().isEmpty()) {
            // CREATE: New book
            book.setId(UUID.randomUUID().toString());
            books.add(book);
        } else {
            // UPDATE: Existing book
            for (int i = 0; i < books.size(); i++) {
                if (books.get(i).getId().equals(book.getId())) {
                    books.set(i, book);
                    break;
                }
            }
        }
        writeAll(books);
        return book;
    }

    // =========================================================================
    // CRUD OPERATIONS (DELETE)
    // =========================================================================

    /**
     * DELETE: Removes a book from the file by ID.
     */
    public void deleteById(String id) {
        List<Book> books = findAll();
        books.removeIf(b -> b.getId().equals(id));
        writeAll(books);
    }

    /**
     * Utility method to write the entire list of books back to the file.
     */
    private void writeAll(List<Book> books) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Book book : books) {
                writer.write(book.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

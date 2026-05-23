package com.example.demo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;

@RestController
@RequestMapping("/api/books")

public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);
    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(book -> ResponseEntity.ok().body(book))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Book createBook(@RequestBody Book book) {
        try {
            System.out.println("Creating book: " + book);
            logger.info("Creating book: {}", book);
            return bookRepository.save(book);
        } catch (Exception e) {
            logger.error("Error creating book", e);
            throw e; // Rethrow the exception after logging
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
        return bookRepository.findById(id)
                .map(book -> {
                    // Use the title from bookDetails if it's not empty, otherwise retain the current title
                    if (bookDetails.getTitle() != null && !bookDetails.getTitle().isEmpty()) {
                        book.setTitle(bookDetails.getTitle());
                    }
                    ;
                    if (bookDetails.getAuthorname() != null && !bookDetails.getAuthorname().isEmpty()) {
                        book.setAuthorname(bookDetails.getAuthorname());
                    }
                    ;
                    if (bookDetails.getIsbn() != null && !bookDetails.getIsbn().isEmpty()) {
                        book.setIsbn(bookDetails.getIsbn());
                    }
                    ;
                    if (bookDetails.getPubyear() != 0) {
                        book.setPubyear(bookDetails.getPubyear());
                    }
                    ;
                    if (bookDetails.getCopies() != 0) {
                        book.setCopies(bookDetails.getCopies());
                    }
                    ;

                    Book updatedBook = bookRepository.save(book);
                    return ResponseEntity.ok().body(updatedBook);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBook(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(book -> {
                    bookRepository.delete(book);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }
}
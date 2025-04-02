package org.example.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.dtos.copies.BookCopyDto;
import org.example.entities.Book;
import org.example.entities.BookCopy;
import org.example.services.BookCopyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.Collection;

@RestController
@RequestMapping("/copies")
@Slf4j
public class BookCopyController {
    private final BookCopyService bookCopyService;

    @Autowired
    public BookCopyController(BookCopyService bookCopyService) {
        this.bookCopyService = bookCopyService;
    }

    @GetMapping
    public ResponseEntity<?> getBookCopies(){
        return ResponseEntity.ok(bookCopyService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookCopyById(@PathVariable("id") Long id){
        return ResponseEntity.ok(bookCopyService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> createBookCopy(@RequestBody @Valid BookCopyDto dto){
        BookCopyDto saved = bookCopyService.save(dto);
        return ResponseEntity.created(ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri())
                .body(saved);
    }

    @PutMapping
    public ResponseEntity<?> updateBookCopy(@RequestBody @Valid BookCopyDto dto){
        return ResponseEntity.ok(bookCopyService.update(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBookCopyById(@PathVariable("id") Long id){
        bookCopyService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

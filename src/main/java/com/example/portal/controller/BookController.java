package com.example.portal.controller;


import com.example.portal.model.dto.request.BookRequest;
import com.example.portal.model.dto.response.BasicResponse;
import com.example.portal.model.exception.WebException;
import com.example.portal.service.BookService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/book")
@RestController
@CrossOrigin
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/book")
    ResponseEntity<BasicResponse> addBook(HttpServletRequest accessToken, @RequestBody @Valid BookRequest bookRequest) throws WebException {
        BasicResponse basicResponse = bookService.addBook(accessToken, bookRequest);
        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }

    @PutMapping ("/book")
    ResponseEntity<BasicResponse> updateBook(HttpServletRequest accessToken, @RequestBody @Valid BookRequest bookRequest) throws WebException {
        BasicResponse basicResponse = bookService.updateBook(accessToken, bookRequest);
        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }

    @DeleteMapping ("/book")
    ResponseEntity<BasicResponse> deleteBook(HttpServletRequest accessToken, @RequestParam String bookName) throws WebException {
        BasicResponse basicResponse = bookService.deleteBook(accessToken, bookName);
        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }

    @PostMapping("/reserveBook")
    ResponseEntity<BasicResponse> reserveBook(HttpServletRequest accessToken, @RequestParam String bookName) throws WebException {
        BasicResponse basicResponse = bookService.reserveBook(accessToken,bookName);
        return new ResponseEntity<>(basicResponse, HttpStatus.OK);
    }


}

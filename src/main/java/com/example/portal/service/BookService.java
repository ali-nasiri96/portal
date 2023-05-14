package com.example.portal.service;


import com.example.portal.configuration.aop.RequiresAuthorization;
import com.example.portal.configuration.aop.RequiresOnlyAdminUser;
import com.example.portal.configuration.aop.RequiresOnlyNormalUser;
import com.example.portal.configuration.aop.RequiresUserByToken;
import com.example.portal.model.dto.request.BookRequest;
import com.example.portal.model.dto.response.BasicResponse;
import com.example.portal.model.entity.Book;
import com.example.portal.model.entity.User;
import com.example.portal.model.exception.WebException;
import com.example.portal.repository.BookRepository;
import com.example.portal.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class BookService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BookService(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @RequiresAuthorization
    @RequiresUserByToken
    @RequiresOnlyAdminUser
    public BasicResponse addBook(Object userObject, BookRequest bookRequest) throws WebException{
        log.info("add book call...");
        Book book = new Book();
        book.setName(bookRequest.getBookName());
        book.setTopic(bookRequest.getTopic());
        bookRepository.save(book);
        log.info("add book name :"+bookRequest.getBookName());
        return new BasicResponse(BasicResponse.Status.SUCCESS);
    }

    @RequiresAuthorization
    @RequiresUserByToken
    @RequiresOnlyAdminUser
    public BasicResponse updateBook(Object userObject, BookRequest bookRequest) throws WebException {
        log.info("update book call ...");
        Book book = bookRepository.findByName(bookRequest.getBookName());
        if (book == null) throw new WebException("book not found!");
        book.setTopic(bookRequest.getTopic());
        bookRepository.save(book);
        log.info("update book name :"+bookRequest.getBookName()+" "+"set topic : "+bookRequest.getTopic());
        return new BasicResponse(BasicResponse.Status.SUCCESS);
    }

    @RequiresAuthorization
    @RequiresUserByToken
    @RequiresOnlyAdminUser
    public BasicResponse deleteBook(Object userObject, String bookName) throws WebException {
        log.info("delete book call ...");
        Book book = bookRepository.findByName(bookName);
        if (book == null) throw new WebException("book not found!");
        bookRepository.delete(book);
        log.info("add book name : "+bookName);
        return new BasicResponse(BasicResponse.Status.SUCCESS);
    }

    @RequiresAuthorization
    @RequiresUserByToken
    @RequiresOnlyNormalUser
    public BasicResponse reserveBook(Object userObject, String bookName) throws WebException {
        log.info("reserveBook book call ...");
        User user = ((User) userObject);
        Book book = bookRepository.findByName(bookName);
        if (book == null) throw new WebException("book not found!");
        Set<Book> books= new HashSet<>();
        books.add(book);
        user.setBooks(books);
        userRepository.save(user);
        log.info("reserve book name: "+bookName+" "+"for user: "+user.getEmailAddress());
        return new BasicResponse(BasicResponse.Status.SUCCESS);
    }
}

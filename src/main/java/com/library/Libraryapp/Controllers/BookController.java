package com.library.Libraryapp.Controllers;

import com.library.Libraryapp.Entity.Book;
import com.library.Libraryapp.Service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("Http://localhost:3000")
@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService bookService;

    @Autowired
    public BookController(BookService bookService){
        this.bookService=bookService;
    }

    @GetMapping("/secure/ischeckedout/byuser")
    public boolean checkoutBookByUser(@RequestParam Long bookId) throws Exception{
        String userEmail="testuser@email.com";
        return bookService.checkoutBookByUser(userEmail,bookId);
    }

    @GetMapping("/secure/currentLoans/count")
    public int currentLoandCount() throws Exception{
        String userEmail="testuser@email.com";
        return bookService.currentLoansCount(userEmail);
    }

    @PutMapping("/secure/checkout")
    public Book checkoutBook(@RequestParam Long bookId) throws Exception{
        String userEmail="testuser@email.com";
        return bookService.checkoutBook(userEmail,bookId);
    }

}

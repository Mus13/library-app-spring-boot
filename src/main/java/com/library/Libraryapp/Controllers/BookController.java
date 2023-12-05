package com.library.Libraryapp.Controllers;

import com.library.Libraryapp.Entity.Book;
import com.library.Libraryapp.ResponseModels.ShelfCurrentLoansResponse;
import com.library.Libraryapp.Service.BookService;
import com.library.Libraryapp.Utils.ExtractJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("Https://localhost:3000")
@RestController
@RequestMapping("/api/books")
public class BookController {

    private BookService bookService;

    @Autowired
    public BookController(BookService bookService){
        this.bookService=bookService;
    }

    @GetMapping("/secure/ischeckedout/byuser")
    public boolean checkoutBookByUser(@RequestHeader(value = "Authorization") String token,@RequestParam Long bookId) throws Exception{
        String userEmail= ExtractJWT.payloadJWTExtraction(token,"\"sub\"");
        return bookService.checkoutBookByUser(userEmail,bookId);
    }

    @GetMapping("/secure/currentloans")
    public List<ShelfCurrentLoansResponse> currentLoans(@RequestHeader(value = "Authorization") String token) throws Exception{
        String userEmail= ExtractJWT.payloadJWTExtraction(token,"\"sub\"");
        return bookService.currentLoans(userEmail);
    }

    @GetMapping("/secure/currentloans/count")
    public int currentLoandCount(@RequestHeader(value = "Authorization") String token) throws Exception{
        String userEmail= ExtractJWT.payloadJWTExtraction(token,"\"sub\"");
        return bookService.currentLoansCount(userEmail);
    }

    @PutMapping("/secure/checkout")
    public Book checkoutBook(@RequestHeader(value = "Authorization") String token,@RequestParam Long bookId) throws Exception{
        String userEmail= ExtractJWT.payloadJWTExtraction(token,"\"sub\"");
        return bookService.checkoutBook(userEmail,bookId);
    }

    @PutMapping("/secure/return")
    public void returnBook(@RequestHeader(value = "Authorization") String token,@RequestParam Long bookId) throws Exception{
        String userEmail= ExtractJWT.payloadJWTExtraction(token,"\"sub\"");
        bookService.returnBook(userEmail,bookId);
    }

    @PutMapping("/secure/renewloan")
    public void renewLoan(@RequestHeader(value = "Authorization") String token,@RequestParam Long bookId) throws Exception{
        String userEmail= ExtractJWT.payloadJWTExtraction(token,"\"sub\"");
        bookService.renewLoan(userEmail,bookId);
    }
}

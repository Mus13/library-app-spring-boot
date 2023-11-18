package com.library.Libraryapp.Service;

import com.library.Libraryapp.Entity.Book;
import com.library.Libraryapp.Repository.BookRepository;
import com.library.Libraryapp.RequestModels.AddBookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminService {

    private BookRepository bookRepository;

    @Autowired
    public AdminService(BookRepository bookRepository){
        this.bookRepository=bookRepository;
    }

    public void postBook(AddBookRequest addBookRequest){
        Book book= new Book();
        book.setTitle(addBookRequest.getTitle());
        book.setAuthor(addBookRequest.getAuthor());
        book.setDescription(addBookRequest.getDescription());
        book.setCopies(addBookRequest.getCopies());
        book.setCopiesAvailable(addBookRequest.getCopies());
        book.setCategory(addBookRequest.getCategory());
        book.setImg(addBookRequest.getImg());

        bookRepository.save(book);
    }
}

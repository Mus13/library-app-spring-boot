package com.library.Libraryapp.Service;

import com.library.Libraryapp.Entity.Book;
import com.library.Libraryapp.Entity.Checkout;
import com.library.Libraryapp.Entity.History;
import com.library.Libraryapp.Repository.BookRepository;
import com.library.Libraryapp.Repository.CheckoutRepository;
import com.library.Libraryapp.Repository.HistoryRepository;
import com.library.Libraryapp.ResponseModels.ShelfCurrentLoansResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class BookService {

    private BookRepository bookRepository;
    private CheckoutRepository checkoutRepository;
    private HistoryRepository historyRepository;

    public BookService(BookRepository bookRepository, CheckoutRepository checkoutRepository, HistoryRepository historyRepository) {
        this.bookRepository = bookRepository;
        this.checkoutRepository = checkoutRepository;
        this.historyRepository= historyRepository;
    }

    public Book checkoutBook(String userEmail, Long bookId) throws Exception{
        Optional<Book> book= bookRepository.findById(bookId);
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail,bookId);
        if(!book.isPresent() || validateCheckout!=null || book.get().getCopiesAvailable()<=0){
            throw new Exception("Book doesn't exist or it's already checked out!");
        }
        book.get().setCopiesAvailable(book.get().getCopiesAvailable()-1);
        bookRepository.save(book.get());

        Checkout checkout= new Checkout(userEmail, LocalDate.now().toString(),LocalDate.now().plusDays(7).toString(),book.get().getId());
        checkoutRepository.save(checkout);
        return book.get();
    }

    public boolean checkoutBookByUser(String userEmail, Long bookId){
        Checkout validateCheckout = checkoutRepository.findByUserEmailAndBookId(userEmail,bookId);
        if(validateCheckout!=null)
            return true;
        else
            return false;
    }

    public int currentLoansCount(String userEmail){
        return checkoutRepository.findBooksByUserEmail(userEmail).size();
    }

    public List<ShelfCurrentLoansResponse> currentLoans(String userEmail) throws Exception{
        List<ShelfCurrentLoansResponse> shelfCurrentLoansResponses= new ArrayList<ShelfCurrentLoansResponse>();
        List<Checkout> checkoutList= checkoutRepository.findBooksByUserEmail(userEmail);
        List<Long> bookIdList= new ArrayList<Long>();
        for (Checkout i: checkoutList){
            bookIdList.add(i.getBookId());
        }
        List<Book> books= bookRepository.findBooksByBookIds(bookIdList);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (Book book:
             books) {
            Optional<Checkout> checkout= checkoutList.stream()
                    .filter(
                            x-> x.getBookId()==book.getId()
                    ).findFirst();
            if (checkout.isPresent()){
                Date d1= sdf.parse(checkout.get().getReturnDate());
                Date d2= sdf.parse(LocalDate.now().toString());
                TimeUnit timeUnit= TimeUnit.DAYS;
                long diffrence_in_time= timeUnit.convert(d1.getTime()-d2.getTime(),TimeUnit.MILLISECONDS);
                shelfCurrentLoansResponses.add(new ShelfCurrentLoansResponse(book,(int)diffrence_in_time));
            }
        }

        return shelfCurrentLoansResponses;
    }

    public void returnBook(String userEmail, Long bookId) throws Exception{
        Logger logger = LoggerFactory.getLogger(this.getClass());
        Optional<Book> book= bookRepository.findById(bookId);

        Checkout validateCheckout= checkoutRepository.findByUserEmailAndBookId(userEmail,bookId);

        if (book.isEmpty() || validateCheckout==null){
            throw new Exception("Book does not exist or not checked out by user.");
        }
        book.get().setCopiesAvailable(book.get().getCopiesAvailable()+1);

        bookRepository.save(book.get());
        checkoutRepository.deleteById(validateCheckout.getId());
        History history = new History(  userEmail,
                                        validateCheckout.getCheckoutDate(),
                                        LocalDate.now().toString(),
                                        book.get().getTitle(),
                                        book.get().getAuthor(),
                                        book.get().getDescription(),
                                        book.get().getImg());

        historyRepository.save(history);
    }

    public void renewLoan(String userEmail, Long bookId) throws Exception{

        Optional<Book> book= bookRepository.findById(bookId);
        Checkout validateCheckout= checkoutRepository.findByUserEmailAndBookId(userEmail,bookId);
        if (!book.isPresent() || validateCheckout==null){
            throw new Exception("Book does not exist or not checked out by user.");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d1= sdf.parse(validateCheckout.getReturnDate());
        Date d2= sdf.parse(LocalDate.now().toString());
        TimeUnit timeUnit= TimeUnit.DAYS;
        long diffrence_in_time= timeUnit.convert(d1.getTime()-d2.getTime(),TimeUnit.MILLISECONDS);
        if (diffrence_in_time<0){
            throw new Exception("Due returned date is expired.");
        }

        validateCheckout.setReturnDate(LocalDate.now().plusDays(7).toString());
        checkoutRepository.save(validateCheckout);
    }

}

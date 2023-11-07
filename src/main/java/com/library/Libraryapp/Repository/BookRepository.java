package com.library.Libraryapp.Repository;

import com.library.Libraryapp.Entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Long> {

}

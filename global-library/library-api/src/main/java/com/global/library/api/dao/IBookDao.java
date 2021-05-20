package com.global.library.api.dao;


import com.global.library.entity.Book;

import javax.persistence.Tuple;
import java.util.List;

public interface IBookDao extends IAGenericDao<Book> {

    Book findBookByIsbn(String isbn);

    Tuple findBookByIsbnWithAvgRating(String isbn);

    List<Tuple> findAllBooksWithAvgRating();

    List<Book> findAllBooksOrderByDateOfCreation();

    List<Tuple> findAllBooksOrderByRequestWithAvgRating(String orderBy, String genre);

    List<Tuple> findAllBooksBySearchAndOrderByRequestWithAvgRating(String genre, String search, String orderBy);

    boolean isBookExistByIsbn(String isbn);

}

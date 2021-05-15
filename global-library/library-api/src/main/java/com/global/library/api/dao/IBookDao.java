package com.global.library.api.dao;


import com.global.library.api.dto.GenreDtoQueryNames;
import com.global.library.entity.Book;

import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;

public interface IBookDao extends IAGenericDao<Book> {

    Book findBookByIsbn(String isbn);

    Tuple findBookByIsbnWithAvgRating(String isbn);

    List<Tuple> findAllBooksWithAvgRating();

    List<Book> findAllBooksOrderByDateOfCreation();

    List<Tuple> findAllBooksOrderByRequestWithAvgRating(String orderBy);

    List<Tuple> findAllBooksBySearchAndOrderByRequestWithAvgRating(String search, String orderBy);

    List<Tuple> findAllBooksByCheckBoxGenreQueryNames(GenreDtoQueryNames queryGenreNames);

    boolean isBookExistByIsbn(String isbn);

}

package com.global.library.api.dao;


import com.global.library.api.dto.GenreDtoQueryNames;
import com.global.library.entity.Book;

import javax.persistence.TypedQuery;
import java.util.List;

public interface IBookDao extends IAGenericDao<Book> {

    Book findBookByIsbn(String isbn);

    List<Book> findAllBooksOrderByDateOfCreation();

    List<Book> findBooksBySearchRequest(String search);

    List<Book> findBooksByCheckBoxGenreQueryNames(GenreDtoQueryNames queryGenreNames);

    boolean isBookExistByIsbn(String isbn);

    List<Book> findAllBooksOrderByName();

}

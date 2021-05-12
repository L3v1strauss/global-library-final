package com.global.library.api.dao;


import com.global.library.api.dto.GenreDtoQueryNames;
import com.global.library.entity.Book;

import javax.persistence.TypedQuery;
import java.util.List;

public interface IBookDao extends IAGenericDao<Book> {

    Book findBookByIsbn(String isbn);

    List<Book> findAllBooksOrderByDateOfCreation();

    List<Book> findBooksBySearchRequest(String request);

    List<Book> findBooksBySearchRequestWithPagination(String request, int pageNumber, int pageSize);

    List<Book> findBooksByCheckBoxGenreQueryNames(GenreDtoQueryNames queryGenreNames);

    List<Book> findBooksWithPagination(Integer pageNumber, Integer pageSize);

    boolean isBookExistByIsbn(String isbn);

    TypedQuery<Book> getTypedQueryForBooksSearch(String request);

}

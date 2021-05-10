package com.global.library.api.dao;


import com.global.library.api.dto.GenreDtoQueryNames;
import com.global.library.entity.Book;

import java.util.List;

public interface IBookDao extends IAGenericDao<Book> {

    Book findBookByIsbn(String isbn);

    List<Book> findAllBooksOrderByDateOfCreation();

    List<Book> findBooksBySearchRequest(String request);

    List<Book> findBooksByCheckBoxGenreQueryNames(GenreDtoQueryNames queryGenreNames);

    List<Book> findBooksWithPagination(Integer pageNumber, Integer pageSize);
}

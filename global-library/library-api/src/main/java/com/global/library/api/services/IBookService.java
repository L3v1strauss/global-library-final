package com.global.library.api.services;

import com.global.library.api.dto.BookDto;
import com.global.library.api.dto.GenreDtoQueryNames;
import com.global.library.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface IBookService {

    BookDto getBookById(long id);

    void deleteBook(long id);

    BookDto getBookByIsbn(String isbn);

    List<BookDto> getBooks();

    List<BookDto> getBooks(String orderBy);

    void addBook(BookDto bookDto);

    List<BookDto> getBooksBySearchRequest(String search);

    List<BookDto> getBooksBySearchRequest(String search, String orderBy);

    List<BookDto> getBooksByQueryNames(GenreDtoQueryNames queryGenreNames);






}

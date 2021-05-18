package com.global.library.api.services;

import com.global.library.api.dto.BookDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IBookService {

    BookDto getBookById(long id);

    void deleteBook(long id);

    BookDto getBookByIsbn(String isbn);

    List<BookDto> getAllBooksByGenre(String genre);

    List<BookDto> getAllBooksOrderByDateOfCreation();

    List<BookDto> getAllBooksOrderByRequestWithAvgRating(String orderBy, String genre);

    List<BookDto> getAllBooksWithAvgRating();

    void addBook(BookDto bookDto);

    List<BookDto> getAllBooksBySearchAndOrderByRequestWithAvgRating(String genre, String search, String orderBy);







}

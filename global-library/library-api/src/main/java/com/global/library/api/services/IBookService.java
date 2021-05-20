package com.global.library.api.services;

import com.global.library.api.dto.BookDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IBookService {

    BookDto getBookById(long id);

    void deleteBook(long id);

    BookDto getBookByIsbn(String isbn);

    Page<BookDto> getAllBooksOrderByDateOfCreation(int pageNumber, int pageSize);

    Page<BookDto> getAllBooksOrderByRequestWithAvgRating(String orderBy, String genre, int pageNumber, int pageSize);

    Page<BookDto> getAllBooksWithAvgRating(int pageNumber, int pageSize);

    void addBook(BookDto bookDto);

    List<Integer> getTotalPages(Page<BookDto> page);

    Page<BookDto> getAllBooksBySearchAndOrderByRequestWithAvgRating(String genre, String search, String orderBy, int pageNumber, int pageSize);









}

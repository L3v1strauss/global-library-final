package com.global.library.service.services;


import com.global.library.api.dao.IAuthorDao;
import com.global.library.api.dao.IBookDao;
import com.global.library.api.dao.IPublisherDao;
import com.global.library.api.dto.BookDto;
import com.global.library.api.dto.GenreDtoQueryNames;
import com.global.library.api.mappers.BookMapper;
import com.global.library.api.services.IBookService;
import com.global.library.api.services.IRatingService;
import com.global.library.entity.Author;
import com.global.library.entity.Book;
import com.global.library.entity.Publisher;
import com.global.library.service.utils.BookSetting;
import com.global.library.web.WebScraper;
import com.global.library.web.constants.BookDetailsNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class BookService implements IBookService {

    private final IBookDao bookDao;
    private final IRatingService ratingService;
    private final IAuthorDao authorDao;
    private final IPublisherDao publisherDao;
    private final WebScraper webScraper;

    public BookService(IBookDao bookDao, IRatingService ratingService, IAuthorDao authorDao, IPublisherDao publisherDao, WebScraper webScraper) {
        this.bookDao = bookDao;
        this.ratingService = ratingService;
        this.authorDao = authorDao;
        this.publisherDao = publisherDao;
        this.webScraper = webScraper;
    }

    @Override
    @Transactional
    public BookDto getBookById(long id) {
        Book book = this.bookDao.get(id);
        return BookMapper.mapBookDto(book);
    }

    @Override
    @Transactional
    public BookDto getBookByIsbn(String isbn) {
        Book book = this.bookDao.findBookByIsbn(isbn);
        return BookMapper.mapBookDto(book);
    }

    @Override
    @Transactional
    public void deleteBook(long id) {
        Book book = this.bookDao.get(id);
        this.bookDao.delete(book);
    }

    @Override
    @Transactional
    public List<BookDto> getBooks() {
        return BookMapper.mapAllBooksDto(this.bookDao.findAllBooksOrderByDateOfCreation());
    }

    @Override
    @Transactional
    public List<BookDto> getBooksWithPagination(Integer pageNumber, Integer pageSize) {
        List<BookDto> bookDtoList = BookMapper.mapAllBooksDto(this.bookDao.findBooksWithPagination(pageNumber, pageSize));
        List<Double> averageRatingList = this.ratingService.getAllAverageRatingsWithPagination(pageNumber, pageSize);
        for (int i = 0; i < averageRatingList.size(); i++) {
            for (BookDto bookDto : bookDtoList) {
                if (bookDto == bookDtoList.get(i)) {
                    bookDto.setAverageRating(averageRatingList.get(i));
                }
            }
        }
        return bookDtoList;
    }
    @Override
    @Transactional
    public List<BookDto> getBooksBySearchRequest(String request) {
        return BookMapper.mapAllBooksDto(this.bookDao.findBooksBySearchRequest(request));
    }

    @Override
    @Transactional
    public List<BookDto> getBooksBySearchRequestWithPagination(String request, int pageNumber, int pageSize) {
        List<BookDto> bookDtoList = BookMapper.mapAllBooksDto(this.bookDao.findBooksBySearchRequestWithPagination(request, pageNumber, pageSize ));
        List<Double> averageRatingList = this.ratingService.getAllAverageRatingsWithPaginationAndSearchRequest(request, pageNumber, pageSize);
        for (int i = 0; i < averageRatingList.size(); i++) {
            for (BookDto bookDto : bookDtoList) {
                if (bookDto == bookDtoList.get(i)) {
                    bookDto.setAverageRating(averageRatingList.get(i));
                }
            }
        }
        return bookDtoList;
    }

    @Override
    public List<BookDto> getBooksByQueryNames(GenreDtoQueryNames queryGenreNames) {
        return BookMapper.mapAllBooksDto(this.bookDao.findBooksByCheckBoxGenreQueryNames(queryGenreNames));
    }

    @Override
    @Transactional
    public void addBook(BookDto bookDto) {
        final int DEFAULT_QUANTITY = 1;
        if (this.bookDao.isBookExistByIsbn(bookDto.getIsbn())){
            Book book = this.bookDao.findBookByIsbn(bookDto.getIsbn());
            book.setQuantity(book.getQuantity() + DEFAULT_QUANTITY);
        } else {
            Map<String, String> bookDetails = webScraper.getBookDetailsFromWeb(bookDto.getIsbn());
            Book book = new Book();
            book.setIsbn(bookDto.getIsbn());
            book.setName(bookDetails.get(BookDetailsNames.NAME));
            book.setPicture(bookDetails.get(BookDetailsNames.PICTURE));
            book.setDescription(bookDetails.get(BookDetailsNames.DESCRIPTION));
            book.setQuantity(DEFAULT_QUANTITY);
            setAuthorToBook(book, bookDetails);
            setPublisherToBook(book, bookDetails);
            BookSetting.addGenre(bookDto.getGenreName(), book);
            book.setDateOfCreation(LocalDateTime.now());
            this.bookDao.create(book);
        }
    }

    @Override
    @Transactional
    public void setPublisherToBook(Book book, Map<String, String> bookDetails) {
        DateTimeFormatter format = new DateTimeFormatterBuilder()
                .appendPattern("yyyy")
                .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                .toFormatter();
        book.setYearOfPublishing(LocalDate.parse(bookDetails.get(BookDetailsNames.YEAR_OF_PUBLISHING), format));
        if (this.publisherDao.isPublisherExist(bookDetails.get(BookDetailsNames.PUBLISHER))) {
            Publisher publisher = this.publisherDao.getByName(bookDetails.get(BookDetailsNames.PUBLISHER));
            book.setPublisher(publisher);
        } else {
            Publisher publisher = Publisher.builder().name(bookDetails.get(BookDetailsNames.PUBLISHER)).build();
            book.setPublisher(publisher);
        }
    }

    @Override
    @Transactional
    public void setAuthorToBook(Book book, Map<String, String> bookDetails) {
        for (int i = 0; i < Integer.parseInt(bookDetails.get(BookDetailsNames.AUTHORS_NAMES_COUNTER)); i++) {
            if (this.authorDao.isAuthorExist(bookDetails.get(BookDetailsNames.AUTHOR + i))) {
                Author author = this.authorDao.getByName(bookDetails.get(BookDetailsNames.AUTHOR + i));
                book.getAuthors().add(author);
            } else {
                Author author = new Author();
                author.setName(bookDetails.get(BookDetailsNames.AUTHOR + i));
                book.getAuthors().add(author);
            }
        }
    }

    @Override
    @Transactional
    public Page<BookDto> getPageBookDto(List<BookDto> listBooksPerPage,
                                        List<BookDto> allBooks,
                                        int pageNumber,
                                        int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<BookDto> bookPage = new PageImpl<>(listBooksPerPage,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()),
                allBooks.size());
        return bookPage;
    }
}

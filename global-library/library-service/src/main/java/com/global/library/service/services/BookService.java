package com.global.library.service.services;


import com.global.library.api.dao.IAuthorDao;
import com.global.library.api.dao.IBookDao;
import com.global.library.api.dao.IPublisherDao;
import com.global.library.api.dto.BookDto;
import com.global.library.api.dto.GenreDtoQueryNames;
import com.global.library.api.dto.RatingDto;
import com.global.library.api.enums.GenreName;
import com.global.library.api.enums.OrderByQuerys;
import com.global.library.api.mappers.BookMapper;
import com.global.library.api.services.IBookService;
import com.global.library.entity.Author;
import com.global.library.entity.Book;
import com.global.library.entity.Genre;
import com.global.library.entity.Publisher;
import com.global.library.web.WebScraper;
import com.global.library.web.constants.BookDetailsNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookService implements IBookService {

    private final IBookDao bookDao;
    private final IAuthorDao authorDao;
    private final IPublisherDao publisherDao;
    private final WebScraper webScraper;

    public BookService(IBookDao bookDao, IAuthorDao authorDao, IPublisherDao publisherDao, WebScraper webScraper) {
        this.bookDao = bookDao;
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
        BookDto bookDto = BookMapper.mapBookDto(this.bookDao.findBookByIsbnWithAvgRating(isbn).get(0, Book.class));
            bookDto.setAverageRating(this.bookDao.findBookByIsbnWithAvgRating(isbn).get(1, Double.class));
        if (bookDto.getRatings().isEmpty()) {
            return bookDto;
        }
        bookDto.setRatings(bookDto
                .getRatings()
                .stream()
                .sorted(Comparator.comparing(RatingDto::getDateOfpost, Comparator.reverseOrder()))
                .collect(Collectors.toList()));
        return bookDto;
    }

    @Override
    @Transactional
    public void deleteBook(long id) {
        Book book = this.bookDao.get(id);
        this.bookDao.delete(book);
    }

    @Override
    @Transactional
    public List<BookDto> getAllBooksOrderByDateOfCreation() {
        return BookMapper.mapAllBooksDto(this.bookDao.findAllBooksOrderByDateOfCreation());
    }

    @Override
    @Transactional
    public  List<BookDto> getAllBooksWithAvgRating() {
        return parseTupleToListBookDto( this.bookDao.findAllBooksWithAvgRating());
    }

    @Override
    @Transactional
    public List<BookDto> getAllBooksOrderByRequestWithAvgRating(String orderBy) {
        return parseTupleToListBookDto(this.bookDao.findAllBooksOrderByRequestWithAvgRating(orderBy));
    }

    @Override
    @Transactional
    public List<BookDto> getAllBooksBySearchAndOrderByRequestWithAvgRating(String search, String orderBy) {
      return parseTupleToListBookDto(this.bookDao.findAllBooksBySearchAndOrderByRequestWithAvgRating(search, orderBy));
    }

    @Override
    @Transactional
    public List<BookDto> getAllBooksByQueryNamesWithAvgRating(GenreDtoQueryNames queryGenreNames) {
        return parseTupleToListBookDto(this.bookDao.findAllBooksByCheckBoxGenreQueryNames(queryGenreNames));
    }

    @Override
    @Transactional
    public void addBook(BookDto bookDto) {
        final int DEFAULT_QUANTITY = 1;
        if (this.bookDao.isBookExistByIsbn(bookDto.getIsbn())) {
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
            setPublisherAndYearOfPublishingToBook(book, bookDetails);
            addGenre(bookDto.getGenreName(), book);
            book.setDateOfCreation(LocalDateTime.now());
            this.bookDao.create(book);
        }
    }

    private void setPublisherAndYearOfPublishingToBook(Book book, Map<String, String> bookDetails) {
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

    private void setAuthorToBook(Book book, Map<String, String> bookDetails) {
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

    private void addGenre(String genreName, Book book) {
        for (GenreName value : GenreName.values()) {
            if (value.name().equals(genreName))
                book.setGenre(Genre.builder()
                        .id(value.getId())
                        .name(value.name())
                        .build());
        }
    }

    private List<BookDto> parseTupleToListBookDto(List<Tuple> tuples) {
        List<BookDto> booksDto = new ArrayList<>();
        int currentAddBookIndex = 0;
        for (Tuple bookWithAvgRating : tuples) {
            booksDto.add(BookMapper.mapBookDto(bookWithAvgRating.get(0, Book.class)));
            booksDto.get(currentAddBookIndex).setAverageRating(bookWithAvgRating.get(1, Double.class));
            currentAddBookIndex++;
        }
        return booksDto;
    }
}

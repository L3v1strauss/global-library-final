package com.global.library.api.mappers;

import com.global.library.api.dto.BookDto;
import com.global.library.entity.Book;
import com.global.library.entity.Publisher;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class BookMapper {

    public Book mapBook(BookDto source) {
        return Book.builder()
                .id(source.getId())
                .isbn(source.getIsbn())
                .name(source.getName())
                .picture(source.getPicture())
                .description(source.getDescription())
                .dateOfCreation(source.getDateOfCreation())
                .yearOfPublishing(source.getYearOfPublishing())
                .quantity(source.getQuantity())
                .genre(GenreMapper.mapGenre(source.getGenre()))
                .publisher(PublisherMapper.mapPublisher(source.getPublisher()))
                .authors(AuthorMapper.mapAllAuthors(source.getAuthors()))
                .ratings(RatingMapper.mapAllRatings(source.getRatings()))
                .build();

    }

    public BookDto mapBookDto(Book source) {
        return BookDto.builder()
                .id(source.getId())
                .isbn(source.getIsbn())
                .name(source.getName())
                .picture(source.getPicture())
                .description(source.getDescription())
                .dateOfCreation(source.getDateOfCreation())
                .yearOfPublishing(source.getYearOfPublishing())
                .genre(GenreMapper.mapGenreDto(source.getGenre()))
                .publisher(PublisherMapper.mapPublisherDto(source.getPublisher()))
                .quantity(source.getQuantity())
                .authors(AuthorMapper.mapAllAuthorsDto(source.getAuthors()))
                .ratings(RatingMapper.mapAllRatingsDto(source.getRatings()))
                .build();

    }

    public List<Book> mapAllBooks(List<BookDto> source) {
        return source.stream().map(BookMapper::mapBook).collect(Collectors.toList());
    }

    public List<BookDto> mapAllBooksDto(List<Book> source) {
        return source.stream().map(BookMapper::mapBookDto).collect(Collectors.toList());
    }
}

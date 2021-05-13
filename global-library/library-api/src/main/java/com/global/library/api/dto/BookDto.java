package com.global.library.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.ISBN;

import javax.validation.constraints.NotEmpty;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Slf4j
public class BookDto {

    private long id;
    @NotEmpty
    @ISBN
    private String isbn;
    private String name;
    private String picture;
    private String description;
    private LocalDateTime dateOfCreation;
    private LocalDate yearOfPublishing;
    private Set<AuthorDto> authors;
    private List<RatingDto> ratings;
    private GenreDto genre;
    private PublisherDto publisher;
    private int quantity;
    private double averageRating;

    @NotEmpty
    private String genreName;

    public String parseDateOfCreation() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return this.dateOfCreation.format(formatter).toString().replace("T", " ");
    }

    public String parseAverageRating() {
        DecimalFormat myFormatter = new DecimalFormat("###.#");
        return myFormatter.format(this.averageRating);
    }
}

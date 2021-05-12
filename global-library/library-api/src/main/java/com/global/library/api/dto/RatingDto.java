package com.global.library.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RatingDto {

    private long id;
    private LocalDateTime dateOfpost;
    private double ratingValue;
    private String review;
    private UserDto user;
    private BookDto book;

    public String parseDateOfPost() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return this.dateOfpost.format(formatter).toString().replace("T", " ");
    }

}

package com.global.library.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class RequestDto {

    long id;
    private LocalDateTime dateOfCreation;
    private UserDto user;
    private BookDto book;
    private String status;

    public String parseDateOfPost() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return this.dateOfCreation.format(formatter).toString().replace("T", " ");
    }
}

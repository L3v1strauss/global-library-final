package com.global.library.service.utils;

import com.global.library.api.enums.GenreName;
import com.global.library.entity.Book;
import com.global.library.entity.Genre;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BookSetting {

    public void addGenre(String genreName, Book book) {
        for (GenreName value : GenreName.values()) {
            if (value.name().equals(genreName))
                book.setGenre(Genre.builder()
                        .id(value.getId())
                        .name(value.name())
                        .build());
        }
    }
}


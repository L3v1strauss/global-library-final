package com.global.library.api.services;

import com.global.library.api.dto.GenreDto;
import com.global.library.entity.Genre;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IGenreService {

    void deleteGenre(long id);

    GenreDto getGenreById(long id);

    boolean isGenreExist(String name);

    List<GenreDto> getAllGenresOrderByName();

    void addGenre(GenreDto genreDto);

}

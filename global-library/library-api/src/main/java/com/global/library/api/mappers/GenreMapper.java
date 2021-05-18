package com.global.library.api.mappers;

import com.global.library.api.dto.GenreDto;
import com.global.library.api.dto.PublisherDto;
import com.global.library.entity.Genre;
import com.global.library.entity.Publisher;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class GenreMapper {

    public Genre mapGenre(GenreDto source) {
        return Genre.builder()
                .id(source.getId())
                .name(source.getName())
                .build();

    }

    public GenreDto mapGenreDto(Genre source) {
        return GenreDto.builder()
                .id(source.getId())
                .name(source.getName())
                .build();
    }

    public List<Genre> mapAllGenres(List<GenreDto> source) {
        return source.stream().map(GenreMapper::mapGenre).collect(Collectors.toList());
    }

    public List<GenreDto> mapAllGenresDto(List<Genre> source) {
        return source.stream().map(GenreMapper::mapGenreDto).collect(Collectors.toList());
    }
}

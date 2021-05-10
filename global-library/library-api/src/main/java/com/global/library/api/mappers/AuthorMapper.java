package com.global.library.api.mappers;

import com.global.library.api.dto.AuthorDto;
import com.global.library.api.dto.RoleDto;
import com.global.library.entity.Author;
import com.global.library.entity.Role;
import lombok.experimental.UtilityClass;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class AuthorMapper {

    public Author mapAuthor(AuthorDto source) {
        return Author.builder()
                .id(source.getId())
                .name(source.getName())
                .build();

    }

    public AuthorDto mapAuthorDto(Author source) {
        return AuthorDto.builder()
                .id(source.getId())
                .name(source.getName())
                .build();
    }

    public Set<Author> mapAllAuthors(Set<AuthorDto> source) {
        return source.stream().map(AuthorMapper::mapAuthor).collect(Collectors.toSet());
    }

    public Set<AuthorDto> mapAllAuthorsDto(Set<Author> source) {
        return source.stream().map(AuthorMapper::mapAuthorDto).collect(Collectors.toSet());
    }
}

package com.global.library.api.mappers;

import com.global.library.api.dto.AuthorDto;
import com.global.library.api.dto.PublisherDto;
import com.global.library.entity.Author;
import com.global.library.entity.Publisher;
import lombok.experimental.UtilityClass;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class PublisherMapper {

    public Publisher mapPublisher(PublisherDto source) {
        return Publisher.builder()
                .id(source.getId())
                .name(source.getName())
                .build();

    }

    public PublisherDto mapPublisherDto(Publisher source) {
        return PublisherDto.builder()
                .id(source.getId())
                .name(source.getName())
                .build();
    }

    public Set<Publisher> mapAllPublishers(Set<PublisherDto> source) {
        return source.stream().map(PublisherMapper::mapPublisher).collect(Collectors.toSet());
    }

    public Set<PublisherDto> mapAllPublishersDto(Set<Publisher> source) {
        return source.stream().map(PublisherMapper::mapPublisherDto).collect(Collectors.toSet());
    }
}

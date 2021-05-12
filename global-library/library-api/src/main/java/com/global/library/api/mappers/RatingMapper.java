package com.global.library.api.mappers;

import com.global.library.api.dto.GenreDto;
import com.global.library.api.dto.RatingDto;
import com.global.library.entity.Genre;
import com.global.library.entity.Rating;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class RatingMapper {

    public Rating mapRating(RatingDto source) {
        return Rating.builder()
                .id(source.getId())
                .dateOfpost(source.getDateOfpost())
                .review(source.getReview())
                .ratingValue(source.getRatingValue())
                .user(UserMapper.mapUser(source.getUser()))
                .book(BookMapper.mapBook(source.getBook()))
                .build();

    }

    public RatingDto mapRatingDto(Rating source) {
        return RatingDto.builder()
                .id(source.getId())
                .dateOfpost(source.getDateOfpost())
                .review(source.getReview())
                .ratingValue(source.getRatingValue())
                .user(UserMapper.mapUserDto(source.getUser()))
//                .book(BookMapper.mapBookDto(source.getBook()))
                .build();
    }

    public List<Rating> mapAllRatings(List<RatingDto> source) {
        return source.stream().map(RatingMapper::mapRating).collect(Collectors.toList());
    }

    public List<RatingDto> mapAllRatingsDto(List<Rating> source) {
        return source.stream().map(RatingMapper::mapRatingDto).collect(Collectors.toList());
    }
}

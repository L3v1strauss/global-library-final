package com.global.library.api.mappers;

import com.global.library.api.dto.RatingDto;
import com.global.library.api.dto.RequestDto;
import com.global.library.entity.Rating;
import com.global.library.entity.Request;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class RequestMapper {

    public Request mapRequest(RequestDto source) {
        return Request.builder()
                .id(source.getId())
                .dateOfCreation(source.getDateOfCreation())
                .user(UserMapper.mapUser(source.getUser()))
                .book(BookMapper.mapBook(source.getBook()))
                .status(source.getStatus())
                .build();

    }

    public RequestDto mapRequestDto(Request source) {
        return RequestDto.builder()
                .id(source.getId())
                .dateOfCreation(source.getDateOfCreation())
                .user(UserMapper.mapUserDto(source.getUser()))
                .book(BookMapper.mapBookDto(source.getBook()))
                .status(source.getStatus())
                .build();
    }

    public List<Request> mapAllRequests(List<RequestDto> source) {
        return source.stream().map(RequestMapper::mapRequest).collect(Collectors.toList());
    }

    public List<RequestDto> mapAllRequestsDto(List<Request> source) {
        return source.stream().map(RequestMapper::mapRequestDto).collect(Collectors.toList());
    }
}


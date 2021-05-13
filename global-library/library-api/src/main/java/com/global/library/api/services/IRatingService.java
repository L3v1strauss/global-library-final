package com.global.library.api.services;

import com.global.library.api.dto.RatingDto;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public interface IRatingService {

    void addRating(Principal principal, String isbn, RatingDto ratingDto);

    boolean isRatingExistFromCurrentUser(String isbn, String email);

    List<Double> getAllAverageRatingsWithPagination(int pageNumber, int pageSize);

    List<Double> getAllAverageRatingsWithPaginationAndSearchRequest(String request, int pageNumber, int pageSize);
}

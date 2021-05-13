package com.global.library.api.dao;

import com.global.library.entity.Rating;

import java.util.List;

public interface IRatingDao extends IAGenericDao<Rating> {

    boolean isRatingExistFromCurrentUser(String isbn, String email);

    List<Double> findAllAverageRatingsWithPagination(Integer pageNumber, Integer pageSize);

    List<Double> findAllAverageRatingsWithPaginationAndSearchRequest(String request, int pageNumber, int pageSize);

}

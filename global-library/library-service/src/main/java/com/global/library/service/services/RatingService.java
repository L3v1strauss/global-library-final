package com.global.library.service.services;

import com.global.library.api.dao.IBookDao;
import com.global.library.api.dao.IRatingDao;
import com.global.library.api.dao.IUserDao;
import com.global.library.api.dao.RatingDao;
import com.global.library.api.dto.RatingDto;
import com.global.library.api.mappers.RatingMapper;
import com.global.library.api.services.IRatingService;
import com.global.library.entity.Book;
import com.global.library.entity.Rating;
import com.global.library.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RatingService implements IRatingService {

    private final IUserDao userDao;
    private final IBookDao bookDao;
    private final IRatingDao ratingDao;

    public RatingService(IUserDao userDao, IBookDao bookDao, IRatingDao ratingDao) {
        this.userDao = userDao;
        this.bookDao = bookDao;
        this.ratingDao = ratingDao;
    }

    @Override
    @Transactional
    public void addRating(Principal principal, String isbn, RatingDto ratingDto) {
        User user = this.userDao.findUserByEmail(principal.getName());
        Book book = this.bookDao.findBookByIsbn(isbn);
        Rating rating = new Rating();
        rating.setBook(book);
        rating.setUser(user);
        rating.setRatingValue(ratingDto.getRatingValue());
        rating.setReview(ratingDto.getReview());
        rating.setDateOfpost(LocalDateTime.now());
        this.ratingDao.create(rating);
    }

    @Override
    @Transactional
    public boolean isRatingExistFromCurrentUser(String isbn, String email) {
        return this.ratingDao.isRatingExistFromCurrentUser(isbn, email);
    }

    @Override
    @Transactional
    public List<Double> getAllAverageRatingsWithPagination(int pageNumber, int pageSize) {
        return this.ratingDao.findAllAverageRatingsWithPagination(pageNumber, pageSize);
    }

    @Override
    @Transactional
    public List<Double> getAllAverageRatingsWithPaginationAndSearchRequest(String request, int pageNumber, int pageSize) {
        return this.ratingDao.findAllAverageRatingsWithPaginationAndSearchRequest(request, pageNumber, pageSize);
    }
}

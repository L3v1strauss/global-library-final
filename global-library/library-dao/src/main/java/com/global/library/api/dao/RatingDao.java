package com.global.library.api.dao;

import com.global.library.entity.*;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Repository
public class RatingDao extends AGenericDao<Rating> implements IRatingDao {
    public RatingDao() {
        super(Rating.class);
    }

    public List<Rating> findAllRatingsOrderByDateOfPost(String isbn) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Rating> query = builder.createQuery(getGenericClass());
        Root<Rating> ratingRoot = query.from(Rating.class);
        Join<Rating, Book> bookJoin = ratingRoot.join(Rating_.book);
        query.select(ratingRoot).where(builder.equal(bookJoin.get(Book_.isbn), isbn))
                .orderBy(builder.desc(ratingRoot.get(Rating_.dateOfpost)));
        TypedQuery<Rating> result = entityManager.createQuery(query);
        return result.getResultList();
    }

    public boolean isRatingExistFromCurrentUser(String isbn, String email) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Rating> query = builder.createQuery(getGenericClass());
        Root<Rating> ratingRoot = query.from(Rating.class);
        Join<Rating, User> userJoin = ratingRoot.join(Rating_.user);
        Join<Rating, Book> bookJoin = ratingRoot.join(Rating_.book);
        query.select(ratingRoot).where(builder.and(builder.equal(bookJoin.get(Book_.isbn), isbn)),
                builder.equal(userJoin.get(User_.email), email));
        TypedQuery<Rating> result = entityManager.createQuery(query);
        return result.getResultList().stream().findFirst().isPresent();
    }

    public Double findAverageRatingForBook(String isbn) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Double> query = builder.createQuery(Double.class);
        Root<Rating> ratingRoot = query.from(Rating.class);
        Join<Rating, Book> bookJoin = ratingRoot.join(Rating_.book);
        query.select(builder.avg(ratingRoot.get(Rating_.ratingValue))).where(builder.equal(bookJoin.get(Book_.isbn), isbn))
                .orderBy(builder.desc(ratingRoot.get(Rating_.dateOfpost)));
        TypedQuery<Double> result = entityManager.createQuery(query);
        return result.getSingleResult();
    }

    public List<Double> findAllAverageRatingsWithPagination(Integer pageNumber, Integer pageSize) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Double> query = builder.createQuery(Double.class);
        Root<Rating> ratingRoot = query.from(Rating.class);
        Join<Rating, Book> bookJoin = ratingRoot.join(Rating_.book);
        query.select(builder.avg(ratingRoot.get(Rating_.ratingValue))).groupBy(bookJoin.get(Book_.isbn));
        TypedQuery<Double> result = entityManager.createQuery(query);
        return result
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public List<Double> findAllAverageRatingsWithPaginationAndSearchRequest(String request, int pageNumber, int pageSize) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Double> query = builder.createQuery(Double.class);
        Root<Book> bookRoot = query.from(Book.class);
        Join<Book, Author> authorJoin = bookRoot.join(Book_.authors);
        Join<Book, Publisher> publisherJoin = bookRoot.join(Book_.publisher);
        Join<Book, Rating> ratingJoin = bookRoot.join(Book_.ratings);
        List<Predicate> predicates = new ArrayList<>();
        CriteriaBuilder.Case<Object> objectCase = builder.selectCase();
        String[] requestWords = request.replaceAll("[\\p{P}]", " ").split(" +");
        for (String requestWord : requestWords) {
            Predicate predicate = builder.or(builder.like(bookRoot.get(Book_.isbn), "%" + requestWord + "%"),
                    builder.like(bookRoot.get(Book_.name), "%" + requestWord + "%"),
                    builder.like(authorJoin.get(Author_.name), "%" + requestWord + "%"),
                    builder.like(publisherJoin.get(Publisher_.name), "%" + requestWord + "%"));
            predicates.add(predicate);
            objectCase = builder.selectCase()
                    .when(builder.like(bookRoot.get(Book_.name), "%" + requestWord + "%"), 2)
                    .when(builder.like(authorJoin.get(Author_.name), "%" + requestWord + "%"), 1)
                    .when(builder.like(publisherJoin.get(Publisher_.name), "%" + requestWord + "%"), 3);
        }
        Predicate finalPredicate = builder.or(predicates.toArray(new Predicate[0]));
        Order order = builder.desc(objectCase);
        query.select(builder.avg(ratingJoin.get(Rating_.ratingValue)))
                .groupBy(bookRoot.get(Book_.isbn)).where(finalPredicate).distinct(true).orderBy(order);
        TypedQuery<Double> result = entityManager.createQuery(query);
        return result
                .setFirstResult((pageNumber - 1) * pageSize)
                .setMaxResults(pageSize)
                .getResultList();

    }
}

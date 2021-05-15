package com.global.library.api.dao;


import com.global.library.api.dto.GenreDtoQueryNames;
import com.global.library.api.enums.OrderByQuerys;
import com.global.library.entity.*;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.criteria.CriteriaBuilder.Case;
import java.util.*;

@Repository
public class BookDao extends AGenericDao<Book> implements IBookDao {
    public BookDao() {
        super(Book.class);
    }

    public List<Book> findAllBooksOrderByDateOfCreation() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> query = builder.createQuery(getGenericClass());
        Root<Book> bookRoot = query.from(Book.class);
        query.select(bookRoot);
        TypedQuery<Book> result = entityManager.createQuery(query);
        return result.getResultList();
    }

    public List<Tuple> findAllBooksWithAvgRating() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = builder.createTupleQuery();
        Root<Book> bookRoot = query.from(Book.class);
        Join<Book, Rating> ratingJoin = bookRoot.join(Book_.ratings, JoinType.LEFT);
        Order order = builder.desc(bookRoot.get(Book_.dateOfCreation));
        query.multiselect(bookRoot, builder.avg(ratingJoin.get(Rating_.ratingValue)))
                .groupBy(bookRoot.get(Book_.isbn)).orderBy(order);
        return entityManager.createQuery(query).getResultList();
    }

    public List<Tuple> findAllBooksOrderByRequestWithAvgRating(String orderBy) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = builder.createTupleQuery();
        Root<Book> bookRoot = query.from(Book.class);
        Join<Book, Rating> ratingJoin = bookRoot.join(Book_.ratings, JoinType.LEFT);
        Order order = builder.desc(bookRoot.get(Book_.isbn));
        if (orderBy == null) {
            order = builder.desc(bookRoot.get(Book_.dateOfCreation));
        } else if (orderBy.equals(OrderByQuerys.NAME.getName())) {
            order = builder.asc(bookRoot.get(Book_.name));
        } else if (orderBy.equals(OrderByQuerys.RATING.getName())) {
            order = builder.desc(builder.avg(ratingJoin.get(Rating_.ratingValue)));
        }
        query.multiselect(bookRoot, builder.avg(ratingJoin.get(Rating_.ratingValue)));
        query.groupBy(bookRoot.get(Book_.id)).orderBy(order);
        return entityManager.createQuery(query).getResultList();
    }

    public boolean isBookExistByIsbn(String isbn) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> query = builder.createQuery(getGenericClass());
        Root<Book> bookRoot = query.from(Book.class);
        query.select(bookRoot).where(builder.equal(bookRoot.get(Book_.isbn), isbn));
        TypedQuery<Book> result = entityManager.createQuery(query);
        return result.getResultList().stream().findFirst().isPresent();
    }

    public Book findBookByIsbn(String isbn) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> query = builder.createQuery(getGenericClass());
        Root<Book> bookRoot = query.from(Book.class);
        query.select(bookRoot).where(builder.equal(bookRoot.get(Book_.isbn), isbn));
        TypedQuery<Book> result = entityManager.createQuery(query);
        return result.getSingleResult();
    }

    public Tuple findBookByIsbnWithAvgRating(String isbn) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = builder.createTupleQuery();
        Root<Book> bookRoot = query.from(Book.class);
        Join<Book, Rating> ratingJoin = bookRoot.join(Book_.ratings, JoinType.LEFT);
        query.select(builder.tuple(bookRoot, builder.avg(ratingJoin.get(Rating_.ratingValue))))
                .where(builder.equal(bookRoot.get(Book_.isbn), isbn));
        Tuple result = entityManager.createQuery(query).getSingleResult();
        return result;

    }

    public List<Tuple> findAllBooksBySearchAndOrderByRequestWithAvgRating(String search, String orderBy) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = builder.createTupleQuery();
        Root<Book> bookRoot = query.from(Book.class);
        Join<Book, Author> authorJoin = bookRoot.join(Book_.authors);
        Join<Book, Publisher> publisherJoin = bookRoot.join(Book_.publisher);
        Join<Book, Rating> ratingJoin = bookRoot.join(Book_.ratings, JoinType.LEFT);
        List<Predicate> predicates = new ArrayList<>();
        Case<Object> objectCase = builder.selectCase();
        Order order = builder.desc(bookRoot.get(Book_.isbn));
        String[] requestWords = search.replaceAll("[\\p{P}]", " ").split(" +");
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
        if (orderBy == null) {
            order = builder.desc(objectCase);
        } else if (orderBy.equals(OrderByQuerys.NAME.getName())) {
            order = builder.asc(bookRoot.get(Book_.name));
        } else if (orderBy.equals(OrderByQuerys.RATING.getName())) {
            order = builder.desc(builder.avg(ratingJoin.get(Rating_.ratingValue)));
        }
        query.multiselect(bookRoot, builder.avg(ratingJoin.get(Rating_.ratingValue)))
                .where(finalPredicate).groupBy(bookRoot.get(Book_.isbn)).orderBy(order);

        return entityManager.createQuery(query).getResultList();
    }

    public List<Tuple> findAllBooksByCheckBoxGenreQueryNames(GenreDtoQueryNames queryGenreNames) {
        List<String> names = queryGenreNames.getGenreDtoQueryNames();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = builder.createTupleQuery();
        Root<Book> bookRoot = query.from(Book.class);
        Join<Book, Genre> genreJoin = bookRoot.join(Book_.genre);
        Join<Book, Rating> ratingJoin = bookRoot.join(Book_.ratings, JoinType.LEFT);
        List<Predicate> predicates = new ArrayList<>();
        Order order = builder.desc(builder.avg(ratingJoin.get(Rating_.ratingValue)));
        for (String name : names) {
            Predicate predicate = builder.equal(genreJoin.get(Genre_.name), name);
            predicates.add(predicate);
        }
        Predicate finalPredicate = builder.or(predicates.toArray(new Predicate[0]));
        query.multiselect(bookRoot, builder.avg(ratingJoin.get(Rating_.ratingValue))).where(finalPredicate)
                .groupBy(bookRoot.get(Book_.isbn)).orderBy(order);
        return entityManager.createQuery(query).getResultList();
    }
}

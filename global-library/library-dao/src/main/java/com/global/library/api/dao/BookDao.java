package com.global.library.api.dao;


import com.global.library.api.dto.GenreDtoQueryNames;
import com.global.library.entity.*;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.criteria.CriteriaBuilder.Case;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookDao extends AGenericDao<Book> implements IBookDao {
    public BookDao() {
        super(Book.class);
    }

    public List<Book> findAllBooksOrderByDateOfCreation() {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Book> query = builder.createQuery(getGenericClass());
            Root<Book> bookRoot = query.from(Book.class);
            query.select(bookRoot).orderBy(builder.desc(bookRoot.get(Book_.dateOfCreation)));
            TypedQuery<Book> result = entityManager.createQuery(query);
            return result.getResultList();
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

    public List<Book> findBooksBySearchRequest(String request)  {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> query = builder.createQuery(getGenericClass());
        Root<Book> bookRoot = query.from(Book.class);
        Join<Book, Author> authorJoin = bookRoot.join(Book_.authors);
        Join<Book, Publisher> publisherJoin = bookRoot.join(Book_.publisher);
        List<Predicate> predicates = new ArrayList<>();
        Case<Object> objectCase = builder.selectCase();
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
        query.select(bookRoot).where(finalPredicate).distinct(true).orderBy(order);
        TypedQuery<Book> result = entityManager.createQuery(query);
        return result.getResultList();
    }

    public List<Book> findBooksByCheckBoxGenreQueryNames(GenreDtoQueryNames queryGenreNames) {
        List<String> names = queryGenreNames.getGenreDtoQueryNames();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> query = builder.createQuery(getGenericClass());
        Root<Book> bookRoot = query.from(Book.class);
        Join<Book, Genre> genreJoin = bookRoot.join(Book_.genre);
        List<Predicate> predicates = new ArrayList<>();
        for (String name : names) {
            Predicate predicate = builder.equal(genreJoin.get(Genre_.name), name);
            predicates.add(predicate);
        }
        Predicate finalPredicate = builder.or(predicates.toArray(new Predicate[0]));
        query.select(bookRoot).where(finalPredicate).distinct(true);
        TypedQuery<Book> result = entityManager.createQuery(query);
        return result.getResultList();
    }



}

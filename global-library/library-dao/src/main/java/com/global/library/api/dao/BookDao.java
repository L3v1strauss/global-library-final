package com.global.library.api.dao;


import com.global.library.api.dto.GenreDtoQueryNames;
import com.global.library.entity.*;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaBuilder.Case;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookDao extends AGenericDao<Book> implements IBookDao {
    public BookDao() {
        super(Book.class);
    }

    public List<Book> findAllBooksOrderByDateOfCreation() throws NoResultException {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Book> query = builder.createQuery(getGenericClass());
            Root<Book> bookRoot = query.from(Book.class);
            query.select(bookRoot).orderBy(builder.desc(bookRoot.get(Book_.dateOfCreation)));
            TypedQuery<Book> result = entityManager.createQuery(query);
            return result.getResultList();
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        }
    }

    public Book findBookByIsbn(String isbn) throws NoResultException {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Book> query = builder.createQuery(getGenericClass());
            Root<Book> bookRoot = query.from(Book.class);
            query.select(bookRoot).where(builder.equal(bookRoot.get(Book_.isbn), isbn));
            TypedQuery<Book> result = entityManager.createQuery(query);
            return result.getResultList().stream().findFirst().orElse(null);
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        }
    }

    public List<Book> findBooksBySearchRequest(String request) throws NoResultException {
        String[] requestWords = request.replaceAll("[\\p{P}]", " ").split(" +");
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Book> query = builder.createQuery(getGenericClass());
            Root<Book> bookRoot = query.from(Book.class);
            Join<Book, Author> authorJoin = bookRoot.join(Book_.authors);
            Join<Book, Publisher> publisherJoin = bookRoot.join(Book_.publisher);
            List<Predicate> predicates = new ArrayList<>();
            Case<Object> objectCase = builder.selectCase();
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
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        }
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

    public List<Book> findBooksWithPagination(Integer pageNumber, Integer pageSize) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> query = builder.createQuery(getGenericClass());
        Root<Book> bookRoot = query.from(Book.class);
        Join<Book, Author> authorJoin = bookRoot.join(Book_.authors);
        Join<Book, Publisher> publisherJoin = bookRoot.join(Book_.publisher);
        query.select(bookRoot).distinct(true);
        TypedQuery<Book> result = entityManager.createQuery(query);
            return result
                    .setFirstResult((pageNumber - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();

    }

    public List<Book> findBookByInExpressions(String request) throws NoResultException {
        String[] requestWords = request.replaceAll("[\\p{P}]", " ").split(" +");
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Book> query = builder.createQuery(getGenericClass());
            Root<Book> bookRoot = query.from(Book.class);
            Join<Book, Author> authorJoin = bookRoot.join(Book_.authors);
            Join<Book, Publisher> publisherJoin = bookRoot.join(Book_.publisher);
            In<String> inClauseBook = builder.in(bookRoot.get(Book_.name));
            In<String> inClauseAuthor = builder.in(authorJoin.get(Author_.name));
            In<String> inClausePublisher = builder.in(publisherJoin.get(Publisher_.name));
            for (String requestWord : requestWords) {
                inClauseBook.value(requestWord);
                inClauseAuthor.value(requestWord);
                inClausePublisher.value(requestWord);
            }
            Predicate predicate = builder.or(inClauseBook, inClauseAuthor, inClausePublisher);
            query.select(bookRoot).where(predicate);
            TypedQuery<Book> result = entityManager.createQuery(query);
            return result.getResultList();
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        }
    }


}

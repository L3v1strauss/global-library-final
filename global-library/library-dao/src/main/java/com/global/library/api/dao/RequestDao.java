package com.global.library.api.dao;

import com.global.library.api.enums.RequestStatusName;
import com.global.library.entity.*;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

@Repository
public class RequestDao extends AGenericDao<Request> implements IRequestDao {
    public RequestDao() {
        super(Request.class);
    }

    public List<Request> findAllCreatedRequestsFromUserByEmail(String email) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Request> query = builder.createQuery(getGenericClass());
        Root<Request> requestRoot = query.from(Request.class);
        Join<Request, User> userJoin = requestRoot.join(Request_.user);
        Predicate predicate = builder.equal(requestRoot.get(Request_.status), RequestStatusName.CREATED.getNameDB());
        query.select(requestRoot).where(builder.and(builder.equal(userJoin.get(User_.email), email),
                predicate));
        TypedQuery<Request> result = entityManager.createQuery(query);
        return result.getResultList();
    }

    public boolean isRequestExistForCurrentBookFromUser(String isbn, String email) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Request> query = builder.createQuery(getGenericClass());
        Root<Request> requestRoot = query.from(Request.class);
        Join<Request, Book> bookJoin = requestRoot.join(Request_.book);
        Join<Request, User> userJoin = requestRoot.join(Request_.user);
        Predicate predicate = builder.or(builder.notEqual(requestRoot.get(Request_.status), RequestStatusName.CANCELLED.getNameDB()),
                builder.notEqual(requestRoot.get(Request_.status), RequestStatusName.RETURNED.getNameDB()));
        query.select(requestRoot).where(builder.and(builder.equal(bookJoin.get(Book_.isbn), isbn),
                builder.equal(userJoin.get(User_.email), email),
                predicate));
        TypedQuery<Request> result = entityManager.createQuery(query);
        return result.getResultList().stream().findFirst().isPresent();
    }

    public List<Request> findAllConfirmedRequestsFromUserByEmail(String email) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Request> query = builder.createQuery(getGenericClass());
        Root<Request> requestRoot = query.from(Request.class);
        Join<Request, User> userJoin = requestRoot.join(Request_.user);
        Predicate predicate = builder.or(builder.equal(requestRoot.get(Request_.status), RequestStatusName.CONFIRMED.getNameDB()),
                builder.equal(requestRoot.get(Request_.status), RequestStatusName.PROCESSED.getNameDB()));
        query.select(requestRoot).where(builder.and(builder.equal(userJoin.get(User_.email), email),
                predicate));
        TypedQuery<Request> result = entityManager.createQuery(query);
        return result.getResultList();
    }

    public List<Request> findAllConfirmedRequests() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Request> query = builder.createQuery(getGenericClass());
        Root<Request> requestRoot = query.from(Request.class);
        Join<Request, User> userJoin = requestRoot.join(Request_.user);
        Predicate predicate = builder.equal(requestRoot.get(Request_.status), RequestStatusName.CONFIRMED.getNameDB());
        query.select(requestRoot).where(predicate);
        TypedQuery<Request> result = entityManager.createQuery(query);
        return result.getResultList();
    }

    public List<Request> findAllProcessedRequests() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Request> query = builder.createQuery(getGenericClass());
        Root<Request> requestRoot = query.from(Request.class);
        Join<Request, User> userJoin = requestRoot.join(Request_.user);
        Predicate predicate = builder.equal(requestRoot.get(Request_.status), RequestStatusName.PROCESSED.getNameDB());
        query.select(requestRoot).where(predicate);
        TypedQuery<Request> result = entityManager.createQuery(query);
        return result.getResultList();
    }
}

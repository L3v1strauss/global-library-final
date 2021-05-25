package com.global.library.dao;

import com.global.library.api.dao.IUserDao;
import com.global.library.entity.*;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDao extends AGenericDao<User> implements IUserDao {
    public UserDao() {
        super(User.class);
    }

    public boolean isUserExist(String email) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(getGenericClass());
        Root<User> userRoot = query.from(User.class);
        query.select(userRoot).where(builder.equal(userRoot.get(User_.email), email));
        TypedQuery<User> result = entityManager.createQuery(query);
        return result.getResultList().stream().findFirst().isPresent();
    }

    public User findUserByEmail(String email) throws NoResultException {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(getGenericClass());
            Root<User> userRoot = query.from(User.class);
            query.select(userRoot).where(builder.equal(userRoot.get(User_.email), email));
            TypedQuery<User> result = entityManager.createQuery(query);
            return result.getResultList().stream().findFirst().orElse(null);
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        }
    }

    public List<User> findUsersBySearchRequest(String request) {
        String[] requestWords = request.replaceAll("[\\p{P}]", " ").split(" +");
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<User> query = builder.createQuery(getGenericClass());
            Root<User> userRoot = query.from(User.class);
            Join<User, UserDetail> userDetailJoin = userRoot.join(User_.userDetails);
            List<Predicate> predicates = new ArrayList<>();
            CriteriaBuilder.Case<Object> objectCase = builder.selectCase();
            for (String requestWord : requestWords) {
                Predicate predicate = builder.or(builder.like(userRoot.get(User_.email), "%" + requestWord + "%"),
                        builder.like(userRoot.get(User_.firstName), "%" + requestWord + "%"),
                        builder.like(userRoot.get(User_.lastName), "%" + requestWord + "%"),
                        builder.like(userDetailJoin.get(UserDetail_.passportNumber), "%" + requestWord + "%"));
                predicates.add(predicate);
                objectCase = builder.selectCase()
                        .when(builder.like(userRoot.get(User_.email), "%" + requestWord + "%"), 2)
                        .when(builder.like(userRoot.get(User_.lastName), "%" + requestWord + "%"), 1)
                        .when(builder.like(userRoot.get(User_.firstName), "%" + requestWord + "%"), 3);
            }
            Predicate finalPredicate = builder.or(predicates.toArray(new Predicate[0]));
            Order order = builder.desc(objectCase);
            query.select(userRoot).where(finalPredicate).distinct(true).orderBy(order);
            TypedQuery<User> result = entityManager.createQuery(query);
            return result.getResultList();
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        }
    }
}

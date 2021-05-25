package com.global.library.dao;

import com.global.library.api.dao.IAuthorDao;
import com.global.library.entity.Author;
import com.global.library.entity.Author_;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class AuthorDao extends AGenericDao<Author> implements IAuthorDao {
    public AuthorDao() {
        super(Author.class);
    }

    @Override
    public boolean isAuthorExist(String name) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Author> query = builder.createQuery(getGenericClass());
        Root<Author> authorRoot = query.from(Author.class);
        query.select(authorRoot).where(builder.equal(authorRoot.get(Author_.name), name));
        TypedQuery<Author> result = entityManager.createQuery(query);
        return result.getResultList().stream().findFirst().isPresent();
    }


    @Override
    public Author getByName(String name) {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Author> query = builder.createQuery(getGenericClass());
            Root<Author> authorRoot = query.from(Author.class);
            query.select(authorRoot).where(builder.equal(authorRoot.get(Author_.name), name));
            TypedQuery<Author> result = entityManager.createQuery(query);
            return result.getResultList().stream().findFirst().orElse(null);
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        }
    }
}

package com.global.library.api.dao;

import com.global.library.entity.Genre;
import com.global.library.entity.Genre_;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class GenreDao extends AGenericDao<Genre> implements IGenreDao {

    public GenreDao() {
        super(Genre.class);
    }

    @Override
    public Genre getByName(String name) {
        try {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Genre> query = builder.createQuery(getGenericClass());
            Root<Genre> genreRoot = query.from(Genre.class);
            query.select(genreRoot).where(builder.equal(genreRoot.get(Genre_.name), name));
            TypedQuery<Genre> result = entityManager.createQuery(query);
            return result.getResultList().stream().findFirst().get();
        } catch (NoResultException e) {
            throw new NoResultException(e.getMessage());
        }
    }
}

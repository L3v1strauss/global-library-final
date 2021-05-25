package com.global.library.dao;

import com.global.library.api.dao.IGenreDao;
import com.global.library.entity.Genre;
import com.global.library.entity.Genre_;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class GenreDao extends AGenericDao<Genre> implements IGenreDao {

    public GenreDao() {
        super(Genre.class);
    }

    @Override
    public boolean isGenreExistByName(String name) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Genre> query = builder.createQuery(getGenericClass());
        Root<Genre> genreRoot = query.from(Genre.class);
        query.select(genreRoot).where(builder.equal(genreRoot.get(Genre_.name), name));
        TypedQuery<Genre> result = entityManager.createQuery(query);
        return result.getResultList().stream().findFirst().isPresent();
    }

    @Override
    public Genre getGenreById(long id) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Genre> query = builder.createQuery(getGenericClass());
        Root<Genre> genreRoot = query.from(Genre.class);
        query.select(genreRoot).where(builder.equal(genreRoot.get(Genre_.id), id));
        TypedQuery<Genre> result = entityManager.createQuery(query);
        return result.getSingleResult();
    }

    public List<Genre> findAllGenresOrderByName() {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Genre> query = builder.createQuery(getGenericClass());
        Root<Genre> genreRoot = query.from(Genre.class);
        query.select(genreRoot).orderBy(builder.asc(genreRoot.get(Genre_.name)));
        TypedQuery<Genre> result = entityManager.createQuery(query);
        return result.getResultList();
    }

    @Override
    public Genre getGenreByName(String name) {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Genre> query = builder.createQuery(getGenericClass());
            Root<Genre> genreRoot = query.from(Genre.class);
            query.select(genreRoot).where(builder.equal(genreRoot.get(Genre_.name), name));
            TypedQuery<Genre> result = entityManager.createQuery(query);
            return result.getSingleResult();
    }

}

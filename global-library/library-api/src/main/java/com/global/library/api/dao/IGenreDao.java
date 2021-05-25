package com.global.library.api.dao;

import com.global.library.entity.Genre;

import java.util.List;

public interface IGenreDao extends IAGenericDao<Genre> {

    boolean isGenreExistByName(String name);

    Genre getGenreById(long id);

    List<Genre> findAllGenresOrderByName();

    Genre getGenreByName(String name);
}

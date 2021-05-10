package com.global.library.api.dao;

import com.global.library.entity.Genre;

public interface IGenreDao extends IAGenericDao<Genre> {

    Genre getByName(String name);
}

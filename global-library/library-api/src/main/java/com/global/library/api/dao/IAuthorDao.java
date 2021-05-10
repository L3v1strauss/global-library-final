package com.global.library.api.dao;

import com.global.library.entity.Author;


public interface IAuthorDao extends IAGenericDao<Author> {

    Author getByName(String name);

    boolean isAuthorExist(String name);

}

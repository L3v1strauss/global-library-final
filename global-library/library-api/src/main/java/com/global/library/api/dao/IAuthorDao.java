package com.global.library.api.dao;

import com.global.library.entity.Author;


public interface IAuthorDao extends IAGenericDao<Author> {

    boolean isAuthorExist(String name);

    Author getByName(String name);

}

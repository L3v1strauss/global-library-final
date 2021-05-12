package com.global.library.api.dao;

import com.global.library.entity.User;

import java.util.List;

public interface IUserDao extends IAGenericDao<User> {

    User findUserByEmail(String email);

    User findUserByName(String name);

    List<User> findUsersBySearchRequest(String request);

    boolean isUserExist(String email);


}

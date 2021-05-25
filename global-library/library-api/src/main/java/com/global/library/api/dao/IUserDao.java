package com.global.library.api.dao;

import com.global.library.entity.User;

import java.util.List;

public interface IUserDao extends IAGenericDao<User> {

    boolean isUserExist(String email);

    User findUserByEmail(String email);

    List<User> findUsersBySearchRequest(String request);


}

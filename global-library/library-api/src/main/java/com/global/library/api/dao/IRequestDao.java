package com.global.library.api.dao;

import com.global.library.entity.Request;

import java.util.List;

public interface IRequestDao extends IAGenericDao<Request> {

    boolean isRequestExistForCurrentBookFromUser(String isbn, String email);

    List<Request> findAllCreatedRequestsFromUserByEmail(String email);

    List<Request> findAllConfirmedRequestsFromUserByEmail(String email);

    List<Request> findAllRequests(String status);

    List<Request> findAllRequestsBySearch(String status, String search);

}

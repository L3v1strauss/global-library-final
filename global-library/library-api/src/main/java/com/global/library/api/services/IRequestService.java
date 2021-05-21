package com.global.library.api.services;

import com.global.library.api.dto.RequestDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IRequestService {

    void createRequest(String isbn, String email);

    boolean isRequestExistForCurrentBookFromUser(String isbn, String email);

    List<RequestDto> getAllCreatedRequestsFromUserByEmail(String email);

    List<RequestDto> getAllConfirmedRequestsFromUserByEmail(String email);

    List<RequestDto> getAllRequests(String status);

    List<RequestDto> getAllRequestsBySearch(String status, String search);

    void deleteRequest(long id);

    void confirmRequests(List<RequestDto> requestDtos);

    void processRequest(long id);

    void returnRequest(long id);
}

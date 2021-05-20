package com.global.library.service.services;

import com.global.library.api.dao.IBookDao;
import com.global.library.api.dao.IRequestDao;
import com.global.library.api.dao.IUserDao;
import com.global.library.api.dto.RequestDto;
import com.global.library.api.enums.RequestStatusName;
import com.global.library.api.mappers.RequestMapper;
import com.global.library.api.services.IRequestService;
import com.global.library.entity.Request;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RequestService implements IRequestService {

    private final IRequestDao requestDao;
    private final IBookDao bookDao;
    private final IUserDao userDao;

    public RequestService(IRequestDao requestDao, IBookDao bookDao, IUserDao userDao) {
        this.requestDao = requestDao;
        this.bookDao = bookDao;
        this.userDao = userDao;
    }

    @Override
    @Transactional
    public void createRequest(String isbn, String email) {
        Request request = new Request();
        request.setBook(this.bookDao.findBookByIsbn(isbn));
        request.setUser(this.userDao.findUserByEmail(email));
        request.setDateOfCreation(LocalDateTime.now());
        request.setStatus(RequestStatusName.CREATED.getNameDB());
        this.requestDao.create(request);
    }

    @Override
    @Transactional
    public void deleteRequest(long id) {
        this.requestDao.delete(this.requestDao.get(id));
    }

    @Override
    @Transactional
    public List<RequestDto> getAllCreatedRequestsFromUserByEmail(String email) {
        return RequestMapper.mapAllRequestsDto(this.requestDao.findAllCreatedRequestsFromUserByEmail(email));
    }

    @Override
    @Transactional
    public List<RequestDto> getAllConfirmedRequestsFromUserByEmail(String email) {
        return RequestMapper.mapAllRequestsDto(this.requestDao.findAllConfirmedRequestsFromUserByEmail(email));
    }

    @Override
    @Transactional
    public List<RequestDto> getAllConfirmedRequests() {
        return RequestMapper.mapAllRequestsDto(this.requestDao.findAllConfirmedRequests());
    }

    @Override
    @Transactional
    public List<RequestDto> getAllProcessedRequests() {
        return RequestMapper.mapAllRequestsDto(this.requestDao.findAllProcessedRequests());
    }

    @Override
    @Transactional
    public boolean isRequestExistForCurrentBookFromUser(String isbn, String email) {
        return this.requestDao.isRequestExistForCurrentBookFromUser(isbn, email);
    }

    @Override
    @Transactional
    public void confirmRequests(List<RequestDto> requestDtos) {
        for (RequestDto requestDto : requestDtos) {
            Request request = RequestMapper.mapRequest(requestDto);
            request.setStatus(RequestStatusName.CONFIRMED.getNameDB());
            this.requestDao.update(request);
        }
    }

    @Override
    @Transactional
    public void processRequest(long id) {
        Request request = this.requestDao.get(id);
        request.setStatus(RequestStatusName.PROCESSED.getNameDB());
        request.setDateOfExtradition(LocalDateTime.now());
        this.requestDao.update(request);
    }

    @Override
    @Transactional
    public void returnRequest(long id) {
        Request request = this.requestDao.get(id);
        request.setStatus(RequestStatusName.RETURNED.getNameDB());
        request.setDateOfReturn(LocalDateTime.now());
        this.requestDao.update(request);
    }
}

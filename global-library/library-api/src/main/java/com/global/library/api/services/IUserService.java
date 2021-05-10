package com.global.library.api.services;

import com.global.library.api.dto.BookDto;
import com.global.library.api.dto.UserDto;
import com.global.library.api.dto.UserDtoMyAccount;
import com.global.library.api.dto.UserDtoPasswordChange;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Service
public interface IUserService {

     UserDto getUserById(long id);

     UserDto getUserByEmail(String email);

     boolean isUserExist(String mail);

     List<UserDto> getAllUsers();

     void createUser(UserDto user);

     void updateUser(Principal principal, UserDtoMyAccount user, MultipartFile file);

     void deleteUser(long id);

     void roleChangeUser(long id, UserDto user);

     public void statusChangeUser(long id, int status);

     void changeForgotPassword (String email);

     boolean isPasswordMatches(Principal principal, UserDtoPasswordChange userDtoPasswordChange);

     void updateUserPassword(Principal principal, UserDtoPasswordChange userDtoPasswordChange);

     List<UserDto> getUsersBySearchRequest(String request);

}

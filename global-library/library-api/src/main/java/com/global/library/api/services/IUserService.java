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

     boolean isUserExist(String mail);

     UserDto getUserByEmail(String email);

     List<UserDto> getUsersBySearchRequest(String request);

     void deleteUser(long id);

     List<UserDto> getAllUsers();

     void createUser(UserDto user);

     void roleChangeUser(long id, UserDto user);

     void statusChangeUser(long id, int status);

     void updateUser(Principal principal, UserDtoMyAccount user, MultipartFile file);

     boolean isPasswordMatches(Principal principal, UserDtoPasswordChange userDtoPasswordChange);

     void updateUserPassword(Principal principal, UserDtoPasswordChange userDtoPasswordChange);

     void changeForgotPassword (String email);


}

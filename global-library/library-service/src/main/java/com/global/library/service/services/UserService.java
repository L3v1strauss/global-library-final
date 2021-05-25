package com.global.library.service.services;

import com.global.library.api.dao.IUserDao;
import com.global.library.api.dto.UserDto;
import com.global.library.api.dto.UserDtoMyAccount;
import com.global.library.api.dto.UserDtoPasswordChange;
import com.global.library.api.enums.RoleName;
import com.global.library.api.enums.UserStatus;
import com.global.library.api.mappers.UserDetailMapper;
import com.global.library.api.mappers.UserMapper;
import com.global.library.api.services.IUserService;
import com.global.library.api.utils.IEmailSendler;
import com.global.library.entity.Role;
import com.global.library.entity.User;
import com.global.library.entity.UserDetail;
import com.global.library.service.utils.LogoFileUploader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class UserService implements IUserService {

    private final IUserDao userDao;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final IEmailSendler emailService;

    public UserService(IUserDao userDao,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       @Qualifier("emailSendler") IEmailSendler emailService) {
        this.userDao = userDao;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public UserDto getUserById(long id) {
        User user = this.userDao.get(id);
        return UserMapper.mapUserDto(user);
    }

    @Override
    @Transactional
    public boolean isUserExist(String mail) {
        return this.userDao.isUserExist(mail);
    }

    @Override
    @Transactional
    public UserDto getUserByEmail(String email) {
        User user = this.userDao.findUserByEmail(email);
        return UserMapper.mapUserDto(user);
    }

    @Override
    @Transactional
    public List<UserDto> getUsersBySearchRequest(String request) {
        return UserMapper.mapAllUsersDto(this.userDao.findUsersBySearchRequest(request));
    }

    @Override
    @Transactional
    public void deleteUser(long id) {
        User entity = this.userDao.get(id);
        this.userDao.delete(entity);
        LogoFileUploader.deleteLogo(entity.getEmail());
    }

    @Override
    @Transactional
    public List<UserDto> getAllUsers() {
        return UserMapper.mapAllUsersDto(this.userDao.getAll());

    }

    @Override
    @Transactional
    public void createUser(UserDto user) {
        User entity = UserMapper.mapUser(user);
        UserDetail userDetail = UserDetailMapper.mapUserDetail(user.getUserDetailDto());
        entity.setDateOfCreation(LocalDateTime.now());
        entity.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        entity.setUserDetails(userDetail);
        roleAddOrChange(entity, user.getRoleName());
        startStatusChange(entity, user.getRoleName());
        userDetail.setUser(entity);
        this.userDao.create(entity);
        if (!user.getRoleName().equals(RoleName.ROLE_USER.name())) {
            this.emailService.sendMessageWithActivationInstruction(entity, "Welcome to global library!");
        }
    }

    @Override
    @Transactional
    public void roleChangeUser(long id, UserDto user) {
        User entity = this.userDao.get(id);
        roleAddOrChange(entity, user.getRoleName());
        this.userDao.update(entity);
    }

    private void roleAddOrChange(User user, String roleName) {
        for (RoleName value : RoleName.values()) {
            if (value.name().equals(roleName)) {
                if (!user.getRoles().isEmpty()) {
                    user.getRoles().clear();
                }
                user.getRoles().add(Role.builder()
                        .id(value.getId())
                        .name(value.toString())
                        .build());
            }
        }
    }

    private void startStatusChange(User user, String roleName) {
        if (roleName.equals(RoleName.ROLE_USER.name())) {
            user.setStatus(UserStatus.Enabled.getId());
        } else {
            user.setStatus(UserStatus.Disabled.getId());
        }
    }

    @Override
    @Transactional
    public void statusChangeUser(long id, int status) {
        User entity = this.userDao.get(id);
        entity.setStatus(status);
        this.userDao.update(entity);
    }

    @Override
    @Transactional
    public void updateUser(Principal principal, UserDtoMyAccount user, MultipartFile file) {
        User entity = this.userDao.findUserByEmail(principal.getName());
        entity.setEmail(user.getEmail());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.getUserDetails().setAddress(user.getUserDetailDto().getAddress());
        entity.getUserDetails().setPassportNumber(user.getUserDetailDto().getPassportNumber());
        entity.getUserDetails().setEducationalInstitution(user.getUserDetailDto().getEducationalInstitution());
        entity.getUserDetails().setEducationalInstitutionAddress(user.getUserDetailDto().getEducationalInstitutionAddress());
        this.userDao.update(entity);
        try {
            LogoFileUploader.updateOrCreateLogo(file, user);
        } catch (IOException e) {
            log.error("Failed to upload image. Error message: {}", e.getMessage());
        }
    }

    @Override
    @Transactional
    public boolean isPasswordMatches(Principal principal, UserDtoPasswordChange userDtoPasswordChange) {
        User entity = this.userDao.findUserByEmail(principal.getName());
        return bCryptPasswordEncoder.matches(userDtoPasswordChange.getPasswordConfirm(), entity.getPassword());
    }

    @Override
    @Transactional
    public void updateUserPassword(Principal principal, UserDtoPasswordChange userDtoPasswordChange) {
        User entity = this.userDao.findUserByEmail(principal.getName());
        entity.setPassword(bCryptPasswordEncoder.encode(userDtoPasswordChange.getPassword()));
        entity.setPasswordConfirm(bCryptPasswordEncoder.encode(userDtoPasswordChange.getPassword()));
    }

    @Override
    @Transactional
    public void changeForgotPassword(String email) {
        User entity = this.userDao.findUserByEmail(email);
        String generatedNewPassword = RandomStringUtils.randomAlphanumeric(10);
        entity.setPassword(bCryptPasswordEncoder.encode(generatedNewPassword));
        entity.setPasswordConfirm(bCryptPasswordEncoder.encode(generatedNewPassword));
        this.emailService.sendMessageWithNewPassword(entity, "Your new password", generatedNewPassword);

    }
}

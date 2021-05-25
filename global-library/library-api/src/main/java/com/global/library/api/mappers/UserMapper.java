package com.global.library.api.mappers;

import com.global.library.api.dto.UserDto;
import com.global.library.entity.User;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class UserMapper {

    public User mapUser(UserDto source) {
        return User.builder()
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .email(source.getEmail())
                .status(source.getStatus())
                .password(source.getPassword())
                .passwordConfirm(source.getPasswordConfirm())
                .userDetails(UserDetailMapper.mapUserDetail(source.getUserDetailDto()))
                .roles(RoleMapper.mapAllRoles(source.getRolesDto()))
                .build();

    }

    public UserDto mapUserDto(User source) {
        return UserDto.builder()
                .id(source.getId())
                .firstName(source.getFirstName())
                .lastName(source.getLastName())
                .email(source.getEmail())
                .status(source.getStatus())
                .password(source.getPassword())
                .passwordConfirm(source.getPasswordConfirm())
                .userDetailDto(UserDetailMapper.mapUserDetailDto(source.getUserDetails()))
                .rolesDto(RoleMapper.mapAllRolesDto(source.getRoles()))
                .build();

    }

    public List<User> mapAllUsers(List<UserDto> source) {
        return source.stream().map(UserMapper::mapUser).collect(Collectors.toList());
    }

    public List<UserDto> mapAllUsersDto(List<User> source) {
        return source.stream().map(UserMapper::mapUserDto).collect(Collectors.toList());
    }
}

package com.global.library.api.mappers;

import com.global.library.api.dto.UserDetailDto;
import com.global.library.api.dto.UserDto;
import com.global.library.entity.User;
import com.global.library.entity.UserDetail;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class UserDetailMapper {

    public UserDetail mapUserDetail(UserDetailDto source) {
        return UserDetail.builder()
                .address(source.getAddress())
                .passportNumber(source.getPassportNumber())
                .telephoneNumber(source.getTelephoneNumber())
                .educationalInstitution(source.getEducationalInstitution())
                .educationalInstitutionAddress(source.getEducationalInstitutionAddress())
                .build();

    }

    public UserDetailDto mapUserDetailDto(UserDetail source) {
        return UserDetailDto.builder()
                .id(source.getId())
                .address(source.getAddress())
                .passportNumber(source.getPassportNumber())
                .telephoneNumber(source.getTelephoneNumber())
                .educationalInstitution(source.getEducationalInstitution())
                .educationalInstitutionAddress(source.getEducationalInstitutionAddress())
                .build();

    }

    public List<User> mapAllUserDetails(List<UserDto> source) {
        return source.stream().map(UserMapper::mapUser).collect(Collectors.toList());
    }

    public List<UserDto> mapAllUserDetailssDto(List<User> source) {
        return source.stream().map(UserMapper::mapUserDto).collect(Collectors.toList());
    }
}

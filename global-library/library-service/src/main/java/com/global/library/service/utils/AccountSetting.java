package com.global.library.service.utils;

import com.global.library.api.dto.UserDto;
import com.global.library.entity.Role;
import com.global.library.entity.User;
import com.global.library.api.enums.RoleName;
import com.global.library.api.enums.UserStatus;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AccountSetting {

    public void roleAddOrChange(User user, String roleName) {
        if (roleName == RoleName.ROLE_USER.name()) {
            user.getRoles().add(Role.builder()
                    .id(RoleName.ROLE_USER.getId())
                    .name(RoleName.ROLE_USER.name()).build());
        }
        for (RoleName value : RoleName.values()) {
            if (value.toString().equals(roleName)) {
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

    public void startStatusChange(User user, String roleName) {
        if (roleName == RoleName.ROLE_USER.name()) {
            user.setStatus(UserStatus.Enabled.getId());
        } else {
            user.setStatus(UserStatus.Disabled.getId());
        }
    }
}


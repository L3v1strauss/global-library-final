package com.global.library.api.dto;

import com.global.library.api.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
public class UserDto {

    private long id;
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @Email
    @NotEmpty
    private String email;
    private int status;
    @Size(min = 5, message = "Не меньше 5 знаков")
    private String password;
    private String passwordConfirm;
    private UserDetailDto userDetailDto;
    private Set<RoleDto> rolesDto;
    @NotEmpty
    private String roleName;


    public UserDto() {
        userDetailDto = new UserDetailDto();
    }

    public String namedStatus() {
        if (this.status == UserStatus.Enabled.getId()) {
            return UserStatus.Enabled.getName();
        }
        return UserStatus.Disabled.getName();
    }

}

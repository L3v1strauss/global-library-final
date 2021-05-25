package com.global.library.api.dto;

import com.global.library.api.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
@Slf4j
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
    private Set<RoleDto> rolesDto = new HashSet<>();
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

    public boolean isLogoExist() {
       final String IMAGE_EXTENSION = ".jpg";
       final String LOGOS_FOLDER_PATH = "classpath:static/images/";
        boolean result;
        String filePath = LOGOS_FOLDER_PATH + this.email + IMAGE_EXTENSION;
        try {
            URL url = ResourceUtils.getURL(filePath);
            File logo = new File(url.getPath());
            result = logo.exists();
        } catch (FileNotFoundException exception) {
            return false;
        }
        return result;
    }

}

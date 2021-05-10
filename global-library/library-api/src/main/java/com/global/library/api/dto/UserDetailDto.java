package com.global.library.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserDetailDto {

    private long id;
    @NotEmpty
    private String passportNumber;
    private String address;
    private String educationalInstitution;
    private String educationalInstitutionAddress;
}

package com.global.library.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@ToString
@SuperBuilder
@Table(name = "user_detail")
public class UserDetail extends AEntity<Long> {

    @Column(name = "passport_number")
    private String passportNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "educational_institution")
    private String educationalInstitution;

    @Column(name = "ei_address")
    private String educationalInstitutionAddress;

    @OneToOne(mappedBy = "userDetails", cascade = CascadeType.ALL)
    private User user;
}

package com.global.library.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@SuperBuilder
@Entity
@Table(name = "user")
public class User extends AEntity<Long> {

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "enabled")
    private int status;

    @Size(min = 5, message = "Не меньше 5 знаков")
    private String password;

    @Transient
    private String passwordConfirm;

    @Column(name = "date_creation")
    private LocalDateTime dateOfCreation;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "user",
            fetch = FetchType.LAZY)
    private List<Request> requests;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "user",
            fetch = FetchType.LAZY)
    private List<Extradition> extraditions;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "user",
            fetch = FetchType.LAZY)
    private List<Request> ratings;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private UserDetail userDetails;

    public Set<Role> getRoles() {
        if (roles == null) {
            roles = new HashSet<>();
        }
        return roles;
    }
}

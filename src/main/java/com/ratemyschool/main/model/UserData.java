package com.ratemyschool.main.model;

import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.units.qual.C;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class UserData implements UserDetails, DomainRepresented<User> {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "password")
    private String password;
    @Column
    private String lastName;
    @Column
    private String firstName;
    @Column(name = "is_admin")
    private boolean isAdmin;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { //TODO encode roles to the jwt token
        return isAdmin ? List.of(new SimpleGrantedAuthority("ADMIN"), new SimpleGrantedAuthority("MODERATOR"))
                : Collections.singletonList(new SimpleGrantedAuthority("MODERATOR")) ;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public User toDomainModel() {
        return User.builder()
                .id(id)
                .email(email)
                .isAdmin(isAdmin)
                .lastName(lastName)
                .firstName(firstName)
                .build();
    }
}

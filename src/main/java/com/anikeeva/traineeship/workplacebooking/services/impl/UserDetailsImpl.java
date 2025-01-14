package com.anikeeva.traineeship.workplacebooking.services.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.anikeeva.traineeship.workplacebooking.entities.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private UUID id;
    private String fullName;
    private String phoneNumber;
    private String email;
    @JsonIgnore
    private String password;
    private boolean isDeleted;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean passwordNeedsChange;


    public UserDetailsImpl(UUID id, String fullName, String phoneNumber, String email, String password,
                           boolean isDeleted, Collection<? extends GrantedAuthority> authorities,
                           boolean passwordNeedsChange) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.isDeleted = isDeleted;
        this.authorities = authorities;
        this.passwordNeedsChange = passwordNeedsChange;
    }

    public UserDetailsImpl(UUID id, String fullName, String phoneNumber, String email, String password,
                           boolean isDeleted) {
        this.id = id;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.isDeleted = isDeleted;
    }

    public static UserDetailsImpl build(UserEntity userEntity) {
        List<GrantedAuthority> authorities = userEntity.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoles().name()))
                .collect(Collectors.toList());
        
        return new UserDetailsImpl(
                userEntity.getId(),
                userEntity.getFullName(),
                userEntity.getPhoneNumber(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getIsDeleted(),
                authorities,
                userEntity.isPasswordNeedsChange()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
        return !isDeleted;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !isDeleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}

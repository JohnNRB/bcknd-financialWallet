package com.evertix.financialwallet.security.service;

import com.evertix.financialwallet.model.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class UserDetailsImpl implements UserDetails {
    private final Long id;

    private final String username;

    private final String password;

    private final String email;

    private final Collection<? extends GrantedAuthority> authorities;

    private final String name;

    private final String lastName;

    private final String dni;

    private final String phone;

    public UserDetailsImpl(Long id, String username, String password, String email,
                           String name, String lastName, String dni, String phone, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
        this.lastName = lastName;
        this.dni = dni;
        this.phone = phone;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user) {
        return new UserDetailsImpl(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(), user.getName(),
                user.getLastName(), user.getDni(), user.getPhone(), Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getName().toString())));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }

    @Override
    public String getPassword() { return password; }

    @Override
    public String getUsername() { return username; }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}

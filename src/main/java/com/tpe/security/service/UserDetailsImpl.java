package com.tpe.security.service;

import com.fasterxml.jackson.annotation.*;
import com.tpe.domain.User;
import lombok.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.*;
import org.springframework.security.core.userdetails.*;

import java.util.*;
import java.util.stream.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private Long id;

    private String userName;

    @JsonIgnore // client tarafına giderse , password gitmesin
    private String password;

    private  Collection<? extends GrantedAuthority> authorities;

    public static UserDetailsImpl build(User user) {
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream().
                map(role-> new SimpleGrantedAuthority(role.getName().name())).
                collect(Collectors.toList());
        return new UserDetailsImpl(user.getId(),
                user.getUserName(),
                user.getPassword(),
                authorities);

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
        return userName;
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
}

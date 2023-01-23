package com.tpe.security.service;

import com.tpe.domain.User;
import com.tpe.exception.*;
import com.tpe.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository; //UserRepository'i enjekte ediyoruz.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username).
                orElseThrow(()-> new ResourceNotFoundException("User not found"));
        return UserDetailsImpl.build(user);
    }
}

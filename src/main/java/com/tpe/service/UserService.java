package com.tpe.service;

import com.tpe.controller.dto.*;
import com.tpe.domain.*;
import com.tpe.domain.enums.*;
import com.tpe.exception.*;
import com.tpe.repository.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository; //user çekeceğiz.

    @Autowired
    private RoleRepository roleRepository; //rol çekeceğiz

    @Autowired
    private PasswordEncoder passwordEncoder; // şifreleme
    public void registerUser(RegisterRequest registerRequest) {

        if(userRepository.existsByUserName(registerRequest.getUserName())) { //username control
            throw new ConflictException("Girdiğiniz username kullanımda");
        }
        
        Set<Role> roles = new HashSet<>(); //rolleri ata
        roles.add(role);
        User user = new User(); //Dto'yu User'a çevir
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setUserName(registerRequest.getUserName());
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        userRepository.save(user);

    }
}

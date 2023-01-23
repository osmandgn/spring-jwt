package com.tpe.controller;

import com.tpe.controller.dto.*;
import com.tpe.security.*;
import com.tpe.service.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.*;

@RestController
@RequestMapping
@AllArgsConstructor
public class UserJWTController {

    @Autowired
    private UserService userService; //service katmanına gitmek için

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    // !!! *************** Register ************
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @Valid @RequestBody RegisterRequest registerRequest) { //DTO objemiz.
        userService.registerUser(registerRequest);
        String message = "Kullanıcı kaydınız başarıyla gerçekleşmiştir";
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    // !!! ************** Login ***********************
    @PostMapping("/login") //Login olan kullanıcıya token gönderilir.
    public ResponseEntity<String> login (@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(loginRequest.getUserName(),
                loginRequest.getPassword()));
        String token = jwtUtils.generateToken(authentication);
        return new ResponseEntity<>(token, HttpStatus.CREATED);


    }
}

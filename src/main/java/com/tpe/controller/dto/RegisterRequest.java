package com.tpe.controller.dto;

import lombok.*;

import javax.validation.constraints.*;
import java.util.*;

//Rgister işlemlerimiz için kullanacağımız DTO classı

@Data
public class RegisterRequest {

    @NotBlank
    @NotNull
    @Size(min=1, max=15, message="First name '${validatedValue}' must be between {min} and {max} chars long")
    private String firstName;
    @NotBlank
    @NotNull
    @Size(min=1, max=15, message="Last name '${validatedValue}' must be between {min} and {max} chars long")
    private String lastName;

    @NotBlank
    @NotNull
    @Size(min=1, max=20, message="User name '${validatedValue}' must be between {min} and {max} chars long")
    private String userName;
    @NotBlank
    @NotNull
    @Size(min=5, max=20, message="PassWord '${validatedValue}' must be between {min} and {max} chars long")
    private String password;

    private Set<String> roles;

}

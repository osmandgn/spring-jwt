package com.tpe.controller.dto;

import lombok.*;

import javax.validation.constraints.*;

//Login işlemlerimiz için kullanacağımız DTO classı
@Data //tüm getter setter constuctor notasyonlarını içerir.
public class LoginRequest {

    @NotBlank
    @NotNull
    private String userName;

    @NotBlank
    @NotNull
    private String password;


}

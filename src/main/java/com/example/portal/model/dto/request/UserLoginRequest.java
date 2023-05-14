package com.example.portal.model.dto.request;

import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class UserLoginRequest {
    @NotEmpty(message = "EmailAddress is required")
    private String emailAddress;
    @NotEmpty(message = "Password is required")
    private String password;
}

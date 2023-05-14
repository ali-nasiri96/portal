package com.example.portal.model.dto.request;

import com.example.portal.model.dto.internal.UserType;
import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class UserRegistrationRequest {
    @NotEmpty(message = "userName Address is required")
    private String emailAddress;
    @NotEmpty(message = "password is required")
    private String password;
    private UserType userType;


}

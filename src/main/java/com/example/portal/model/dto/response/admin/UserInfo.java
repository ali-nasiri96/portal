package com.example.portal.model.dto.response.admin;

import com.example.portal.model.dto.internal.UserType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfo {
    private String emailAddress;
    private UserType userType;


    public UserInfo(String emailAddress, UserType userType) {
        this.emailAddress = emailAddress;
        this.userType = userType;
    }
}

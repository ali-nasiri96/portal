package com.example.portal.model.dto.response;

import com.example.portal.model.dto.internal.UserType;
import com.example.portal.model.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoResponse {
    private Long userId;
    private UserType userType;

    public UserInfoResponse(User user) {
        this.setUserId(user.getId());
        this.setUserType(user.getUserType());
    }
}

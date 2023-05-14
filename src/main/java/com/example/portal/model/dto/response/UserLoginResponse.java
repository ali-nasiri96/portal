package com.example.portal.model.dto.response;
import com.example.portal.model.dto.internal.UserType;
import com.example.portal.model.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginResponse extends BasicResponse {
    private String token;
    private UserInfoResponse userInfo;
    private UserType userType;

    public UserLoginResponse(String token, User user, UserType userType) {
        super(Status.SUCCESS);
        this.token = token;
        this.userInfo = new UserInfoResponse(user);
        this.userType = userType;
    }

}

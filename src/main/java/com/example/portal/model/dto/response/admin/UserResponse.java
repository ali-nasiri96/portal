package com.example.portal.model.dto.response.admin;


import com.example.portal.model.dto.response.BasicResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserResponse extends BasicResponse {
    List<UserInfo> userInfos;

    public UserResponse(List<UserInfo> userInfos) {
        super(Status.SUCCESS);
        this.userInfos = userInfos;
    }
}

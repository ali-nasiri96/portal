package com.example.portal.model.dto.internal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class AccessToken {
    private String tokenId;
    private String userId;
    private Date issuanceDate;
    private UserType userType;
}

package com.example.portal.model.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasicResponse {
    private Status status;
    private String message;

    public BasicResponse() {
    }

    public BasicResponse(Status status) {
        this.status = status;
    }

    public BasicResponse(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public enum Status {SUCCESS, FAILURE}
}

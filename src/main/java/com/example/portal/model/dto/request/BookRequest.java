package com.example.portal.model.dto.request;


import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookRequest {
    @NotEmpty(message = "bookName is required")
    private String bookName;
    @NotEmpty(message = "topic is required")
    private String topic;
}

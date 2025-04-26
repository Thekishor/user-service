package com.user_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class FieldErrorResponse {

    private Map<String, String> errors;
    private String timestamp;

    public FieldErrorResponse(Map<String, String> errors){
        this.errors = errors;
        this.timestamp = LocalDateTime.now().toString();
    }
}

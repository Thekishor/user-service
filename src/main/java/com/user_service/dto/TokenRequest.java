package com.user_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRequest {

    @Email(message = "Enter a valid email address")
    @NotBlank(message = "Email Cannot be blank")
    private String email;
}

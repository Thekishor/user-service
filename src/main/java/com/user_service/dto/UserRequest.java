package com.user_service.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {

    @NotBlank(message = "Name Cannot be blank")
    private String name;

    @Email(message = "Enter a valid email address")
    @NotBlank(message = "Email Cannot be blank")
    private String email;

    @NotBlank(message = "Password Cannot be blank")
    @Size(min = 4, max = 100, message = "Password must be between 4 and 100 characters")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{4,100}$",
    message = "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character")
    private String password;

    @NotBlank(message = "User Role Cannot be blank")
    private String role;
}

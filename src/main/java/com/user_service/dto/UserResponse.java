package com.user_service.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserResponse {

    private Long id;

    private String name;

    private String email;

    private String password;

    private String role;

    private boolean status;
}

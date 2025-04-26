package com.user_service.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAlreadyVerifiedException extends RuntimeException{

    private String resourcename;

    public UserAlreadyVerifiedException(String resourcename){
        super(String.format("%s already verified with Email Id ",resourcename));
        this.resourcename = resourcename;
    }
}

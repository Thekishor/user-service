package com.user_service.exception;

import lombok.*;

@NoArgsConstructor
@Setter
@Getter
public class UserAlreadyExistsException extends RuntimeException{

    private String resourcename;
    private String fieldname;

    public UserAlreadyExistsException(String resourcename, String fieldname){
        super(String.format("%s already exists with %s ",resourcename,fieldname));
        this.resourcename=resourcename;
        this.fieldname=fieldname;
    }
}

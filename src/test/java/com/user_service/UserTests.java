package com.user_service;

import com.user_service.dto.UserRequest;
import com.user_service.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserTests {

    @Autowired
    private UserService userService;

    @Test
    void registerUserTest(){
        UserRequest userRequest = new UserRequest();
        userRequest.setName("Kishor");
        userRequest.setEmail(UUID.randomUUID().toString() + "@gmail.com");
        userRequest.setPassword("Kishor@33443#");
        userRequest.setRole("User");

       var createUser = userService.createUser(userRequest);
       assertNotNull(createUser);
       assertEquals("Kishor", createUser.getName());
        System.out.println("Your Email Address is : " +userRequest.getEmail());
    }
}

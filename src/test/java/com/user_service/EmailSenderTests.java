package com.user_service;

import com.user_service.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailSenderTests {

    @Autowired
    private EmailService emailService;

    @Test
    void TestMail(){
        emailService.sendMail(
                "kishorpandey981@gmail.com",
                "Java is high level programming languages",
                "This is simple spring boot user-service project demonstrate the user service and operations"
        );
    }
}

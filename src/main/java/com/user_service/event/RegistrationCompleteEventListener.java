package com.user_service.event;

import com.user_service.entities.User;
import com.user_service.service.EmailService;
import com.user_service.service.EmailTemplateService;
import com.user_service.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailTemplateService emailTemplateService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        User saveUser = userService.saveVerificationTokenForUser(token, user);

        String url = event.getApplicationUrl()
                + "/verifyRegistration?token=" + token;

        String htmlBody = emailTemplateService.createVerificationEmailBody(url);
        emailService.sendMail(saveUser.getEmail(), "Verify Account: User-Service", htmlBody);
        log.info("Verification email sent to your account: {}", user.getEmail());
    }
}

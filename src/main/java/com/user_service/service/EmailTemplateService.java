package com.user_service.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailTemplateService {

    @Value("${token.expiration.minutes:2}")
    private int tokenExpirationMinutes;

    public String createVerificationEmailBody(String verificationUrl){
        return "Hello,<br><br>"
                + "Please click the link below to verify your account:<br><br>"
                + "<a href='" + verificationUrl + "'>Verify Your Account</a><br><br>"
                + "This Verification link will expire in <strong>" + tokenExpirationMinutes + " minutes</strong>.<br><br>"
                + "If the button doesn't work, copy and paste this link into your browser:<br>"
                + verificationUrl + "<br><br>"
                + "Thank you,<br>"
                + "Kishor Pandey (User-Service Developer)";
    }

    public String createPasswordResetVerificationTokenMail(String resetPasswordUrl){
        return "Hello,<br><br>"
                +  "Please copy the link below to reset your password:<br><br>"
                + "This Password Reset link will expire in <strong>" + tokenExpirationMinutes + " minutes</strong>.<br><br>"
                + "Copy and paste this link into your browser:<br>"
                + resetPasswordUrl + "<br><br>"
                + "Thank you,<br>"
                + "Kishor Pandey (User-Service Developer)";
    }

    public String createResendVerificationTokenMail(String resendVerificationUrl){
        return "Hello,<br><br>"
                + "Please click the link below to verify your account:<br><br>"
                + "<a href='" + resendVerificationUrl + "'>Verify Your Account</a><br><br>"
                + "This Verification link will expire in <strong>" + tokenExpirationMinutes + " minutes</strong>.<br><br>"
                + "If the button doesn't work, copy and paste this link into your browser:<br>"
                + resendVerificationUrl + "<br><br>"
                + "Thank you,<br>"
                + "Kishor Pandey (User-Service Developer)";
    }
}

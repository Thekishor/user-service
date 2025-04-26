package com.user_service.controller;

import com.user_service.dto.*;
import com.user_service.entities.User;
import com.user_service.entities.VerificationToken;
import com.user_service.event.RegistrationCompleteEvent;
import com.user_service.service.EmailService;
import com.user_service.service.EmailTemplateService;
import com.user_service.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private EmailTemplateService emailTemplateService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    public ResponseEntity<?> creteUser(
            @Valid @RequestBody UserRequest userRequest,
            final HttpServletRequest request){
        User user = userService.createUser(userRequest);
        RegistrationCompleteEvent registrationCompleteEvent =
                new RegistrationCompleteEvent(user, applicationUrl(request));
        publisher.publishEvent(registrationCompleteEvent);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllUsers(){
        List<UserResponse> userResponses = userService.getAllUsers();
        return new ResponseEntity<>(userResponses, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id){
        UserResponse userResponse = userService.getUserById(id);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
        return new ResponseEntity<>(new ApiResponse("User Deleted Successfully"), HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable("id") Long id,
            @Valid @RequestBody UserRequest userRequest)
    {
        UserResponse userResponse = userService.updateUser(id, userRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @GetMapping("/verifyRegistration")
    public ResponseEntity<?> verifyRegistration(@RequestParam("token") String token){
        String result = userService.validVerificationToken(token);
        if (result.equalsIgnoreCase("valid")){
            return new ResponseEntity<>(new ApiResponse("User Verifies Successfully"), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new ApiResponse("Token Expired"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/resendVerifyToken")
    public ResponseEntity<?> resendVerificationToken(
            @Valid @RequestBody TokenRequest tokenRequest,
            final HttpServletRequest request
    ){
        VerificationToken verificationToken = userService.generateNewVerificationToken(tokenRequest.getEmail());
        User user = verificationToken.getUser();
        resendVerificationTokenMail(user, applicationUrlForResend(request), verificationToken);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(
            @Valid @RequestBody ResetPasswordModel resetPasswordModel,
            final HttpServletRequest request){
        User user = userService.findUserByEmail(resetPasswordModel.getEmail());
        if (user != null){
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);
            passwordResetVerificationTokenMail(user, applicationUrl(request), token);
        }
        return new ResponseEntity<>(new ApiResponse("Password reset link sent Successfully"), HttpStatus.OK);
    }

    @PostMapping("/savePassword")
    public ResponseEntity<?> savePassword(
            @RequestParam("token") String token,
            @Valid @RequestBody SavePassword savePassword
    )
    {
        String result = userService.validatePasswordResetToken(token);
        if (!result.equalsIgnoreCase("valid")){
            return new ResponseEntity<>("Invalid Token", HttpStatus.BAD_REQUEST);
        }
        Optional<User> user = userService.getUserByPasswordResetToken(token);
        if (user.isPresent()){
            userService.changePassword(user.get(), savePassword.getNewPassword());

            userService.deletePasswordResetToken(token);

            return new ResponseEntity<>("Password Reset Successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordModel passwordModel){
        User user = userService.findUserByEmail(passwordModel.getEmail());
        if (!userService.checkIfValidOldPassword(user, passwordModel.getOldPassword())){
            return new ResponseEntity<>("Invalid Old Password", HttpStatus.NOT_FOUND);
        }
        if (userService.passwordMatches(user, passwordModel.getNewPassword())){
            return new ResponseEntity<>(new ApiResponse("New password must be different from the previous password"), HttpStatus.BAD_REQUEST);
        }
        else {
            userService.changePassword(user, passwordModel.getNewPassword());
            return new ResponseEntity<>("Password Change Successfully", HttpStatus.OK);
        }
    }

    private void passwordResetVerificationTokenMail(User user, String applicationUrl, String token) {
        String url = applicationUrl +
                "/savePassword?token=" + token;

        //sendPasswordResetEmail()
        String htmlBody = emailTemplateService.createPasswordResetVerificationTokenMail(url);
        emailService.sendMail(user.getEmail(), "Password Reset Token: User-Service", htmlBody);
        log.info("Click the link to Reset your password: {}", url);
    }

    private void resendVerificationTokenMail(
            User user,
            String applicationUrl,
            VerificationToken verificationToken)
    {
        String url = applicationUrl
                + "/verifyRegistration?token=" + verificationToken.getToken();

        //sendVerificationEmail()
        String htmlBody = emailTemplateService.createResendVerificationTokenMail(url);
        emailService.sendMail(user.getEmail(), "Verify Account: User-Service", htmlBody);
        log.info("Click the link to verify your account: {}", url);
    }

    private String applicationUrlForResend(HttpServletRequest request) {
        return "http://" +
                request.getServerName() +
                ":" + request.getServerPort() +
                "/user" ;
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://" +
                request.getServerName() +
                ":" + request.getServerPort() +
                "/user" +
                request.getContextPath();
    }
}
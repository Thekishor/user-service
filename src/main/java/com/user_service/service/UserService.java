package com.user_service.service;

import com.user_service.dto.UserRequest;
import com.user_service.dto.UserResponse;
import com.user_service.entities.User;
import com.user_service.entities.VerificationToken;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User createUser(UserRequest userRequest);

    UserResponse getUserById(Long id);

    List<UserResponse> getAllUsers();

    void deleteUser(Long id);

    UserResponse updateUser(Long id, UserRequest userRequest);

    User saveVerificationTokenForUser(String token, User user);

    String validVerificationToken(String token);

    VerificationToken generateNewVerificationToken(String email);

    User findUserByEmail(String email);

    void createPasswordResetTokenForUser(User user, String token);

    String validatePasswordResetToken(String token);

    Optional<User> getUserByPasswordResetToken(String token);

    void changePassword(User userInfo, String newPassword);

    boolean checkIfValidOldPassword(User user, String oldPassword);

    void deletePasswordResetToken(String token);

    boolean passwordMatches(User user, String newPassword);
}

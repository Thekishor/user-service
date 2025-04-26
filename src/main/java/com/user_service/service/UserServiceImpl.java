package com.user_service.service;

import com.user_service.dto.UserRequest;
import com.user_service.dto.UserResponse;
import com.user_service.entities.PasswordResetToken;
import com.user_service.entities.User;
import com.user_service.entities.VerificationToken;
import com.user_service.exception.ResourceNotFoundException;
import com.user_service.exception.UserAlreadyExistsException;
import com.user_service.exception.UserAlreadyVerifiedException;
import com.user_service.repository.PasswordResetTokenRepo;
import com.user_service.repository.UserRepo;
import com.user_service.repository.VerificationTokenRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordResetTokenRepo passwordResetTokenRepo;

    @Autowired
    private VerificationTokenRepo verificationTokenRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User createUser(UserRequest userRequest) {
        if (userRepo.existsByEmail(userRequest.getEmail())){
            throw new UserAlreadyExistsException("User", "email Id");
        }
        User user = modelMapper.map(userRequest, User.class);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        User saveUser = userRepo.save(user);
        return user;
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("User", "id", id));
        return modelMapper.map(user, UserResponse.class);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepo.findAll();
        return users.stream().map(user -> modelMapper.map(user, UserResponse.class)).toList();
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("User", "id", id));
        userRepo.delete(user);
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest userRequest) {
        User user = userRepo.findById(id).orElseThrow(()-> new ResourceNotFoundException("User", "id", id));
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(userRequest.getRole());
        User updatedUser = userRepo.save(user);
        return modelMapper.map(updatedUser, UserResponse.class);
    }

    @Override
    public User saveVerificationTokenForUser(String token, User user) {
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepo.save(verificationToken);
        return user;
    }

    @Override
    @Transactional
    public String validVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepo.findByToken(token);
        if (verificationToken == null){
            return "Invalid Token";
        }

        User user = verificationToken.getUser();

        Calendar calendar = Calendar.getInstance();

        if (verificationToken.getExpirationToken().before(calendar.getTime())){
            verificationTokenRepo.delete(verificationToken);
            return "Token Expired";
        }
        user.setStatus(true);
        userRepo.save(user);
        verificationTokenRepo.delete(verificationToken);
        return "Valid";
    }

    @Override
    @Transactional
    public VerificationToken generateNewVerificationToken(String email) {
        User user = userRepo.findByEmail(email);

        if (user == null){
            throw new UsernameNotFoundException("User not found with email");
        }

        if (user.getStatus()){
            throw new UserAlreadyVerifiedException("User");
        }

        verificationTokenRepo.deleteByUserId(user.getId());

        String token = generateNewToken();

        VerificationToken verificationToken = new VerificationToken(user, token);
        return verificationTokenRepo.save(verificationToken);

    }

    @Override
    public User findUserByEmail(String email) {
        User user = userRepo.findByEmail(email);

        if (user == null){
            throw new UsernameNotFoundException("User not found with email");
        }
        return user;
    }

    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(user, token);
        passwordResetTokenRepo.save(passwordResetToken);
    }

    @Override
    @Transactional
    public String validatePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepo.findByToken(token);
        if (passwordResetToken == null){
            return "Invalid Token";
        }
        User user = passwordResetToken.getUser();
        Calendar calendar = Calendar.getInstance();
        if (passwordResetToken.getExpiredTime().before(calendar.getTime())){
            passwordResetTokenRepo.delete(passwordResetToken);
            return "Token Expired";
        }
        return "valid";
    }

    @Override
    public Optional<User> getUserByPasswordResetToken(String token) {
        return Optional.ofNullable(passwordResetTokenRepo.findByToken(token).getUser());
    }

    @Override
    public void changePassword(User userInfo, String newPassword) {
        userInfo.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(userInfo);
    }

    @Override
    public boolean checkIfValidOldPassword(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    @Override
    @Transactional
    public void deletePasswordResetToken(String token) {
        passwordResetTokenRepo.deleteByToken(token);
    }

    @Override
    public boolean passwordMatches(User user, String newPassword) {
        return passwordEncoder.matches(newPassword, user.getPassword());
    }

    private String generateNewToken() {
        return UUID.randomUUID().toString();
    }
}

package com.user_service.mapper;

import com.user_service.dto.UserRequest;
import com.user_service.dto.UserResponse;
import com.user_service.entities.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public User userDtoToUser(UserRequest userRequest){
        User user = new User();
        user.setName(userRequest.getName());
        user.setEmail(userRequest.getEmail());
        user.setPassword(userRequest.getPassword());
        user.setRole(userRequest.getRole());
        return user;
    }

    public UserResponse userToUserResponse(User user){
        UserResponse userResponse = new UserResponse();
        userResponse.setName(user.getName());
        userResponse.setEmail(user.getEmail());
        userResponse.setPassword(user.getPassword());
        userResponse.setRole(user.getRole());
        userResponse.setStatus(user.getStatus());
        return userResponse;
    }
}

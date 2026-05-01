package com.crediario.crediario_api.business.mapper;

import com.crediario.crediario_api.business.dto.user.request.CreateUserRequest;
import com.crediario.crediario_api.business.dto.user.response.UserResponse;
import com.crediario.crediario_api.business.entity.User;

public class UserMapper {

    public static User toEntity(CreateUserRequest request){
        return new User(
                request.login(),
                request.password(),
                request.userType()
        );
    }

    public static UserResponse toResponse(User user){
        return new UserResponse(
                user.getId(),
                user.getLogin(),
                user.getUserType()
        );
    }
}

package com.crediario.crediario_api.business.dto.user.response;


import com.crediario.crediario_api.business.enums.UserType;

public record UserResponse(Long id, String login, UserType userType) {
}

package com.crediario.crediario_api.business.dto.user.request;

import com.crediario.crediario_api.business.enums.UserType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;



public record CreateUserRequest(@NotBlank(message = "The login cannot be empty") String login,
                                @NotBlank(message = "The password cannot be empty") String password,
                                @NotNull(message = "The user type cannot be empty") UserType userType) {
}

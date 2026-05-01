package com.crediario.crediario_api.business.dto.user.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String login,
        @NotBlank String password
){}

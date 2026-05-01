package com.crediario.crediario_api.business.dto.user.request;

import jakarta.validation.constraints.NotBlank;

public record UpdatePasswordRequest(@NotBlank(message = "Login cannot be empty.") String login,
                                    @NotBlank(message = "Current password cannot be empty.")String currentPassword,
                                    @NotBlank(message = "New password cannot be empty.")String newPassword) {
}

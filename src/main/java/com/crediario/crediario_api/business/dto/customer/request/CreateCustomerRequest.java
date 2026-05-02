package com.crediario.crediario_api.business.dto.customer.request;

import com.crediario.crediario_api.business.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateCustomerRequest(
        @NotBlank String name,
        @NotBlank String cpf,
        @NotBlank String phone,
        @NotBlank String profession,
        @JsonFormat(pattern = "yyyy-MM-dd")
        @NotNull(message = "Birth date cannot be empty")
                LocalDate birthDate,
        @NotNull Long registeredById
) { }

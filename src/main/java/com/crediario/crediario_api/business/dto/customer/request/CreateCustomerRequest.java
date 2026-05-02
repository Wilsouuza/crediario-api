package com.crediario.crediario_api.business.dto.customer.request;

import com.crediario.crediario_api.business.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateCustomerRequest(
        @NotBlank String name,
        @NotBlank String cpf,
        @NotBlank String phone,
        @NotBlank String profession,
        @NotNull LocalDate birthDate,
        @NotNull Long registeredById
) { }

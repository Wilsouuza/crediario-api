package com.crediario.crediario_api.business.dto.customer.response;

import com.crediario.crediario_api.business.dto.user.response.UserResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CustomerResponse(
         Long id,
         String name,
         String cpf,
         String phone,
         String profession,
         LocalDate birthDate,
         BigDecimal creditLimit,
         LocalDateTime registrationDate,
         UserResponse registeredBy
) { }

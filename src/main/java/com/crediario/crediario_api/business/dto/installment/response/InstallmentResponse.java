package com.crediario.crediario_api.business.dto.installment.response;

import com.crediario.crediario_api.business.enums.InstallmentStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record InstallmentResponse(
        Long id,
        Long purchaseId,
        BigDecimal value,
        LocalDate dueDate,
        InstallmentStatus status
) {}

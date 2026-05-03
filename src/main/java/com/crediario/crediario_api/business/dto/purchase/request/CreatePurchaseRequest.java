package com.crediario.crediario_api.business.dto.purchase.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;




public record CreatePurchaseRequest(
        @NotNull Long customerId,
        @NotNull BigDecimal value,
        @NotNull int qtyInstallments,
        @NotBlank String description

        ) {}

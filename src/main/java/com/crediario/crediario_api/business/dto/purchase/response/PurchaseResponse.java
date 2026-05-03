package com.crediario.crediario_api.business.dto.purchase.response;

import com.crediario.crediario_api.business.dto.customer.response.CustomerResponse;
import com.crediario.crediario_api.business.entity.Customer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PurchaseResponse(
        Long id,
        CustomerResponse customer,
        BigDecimal value,
        LocalDate date,
        int qtyInstallments,
        String description

) {}

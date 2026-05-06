package com.crediario.crediario_api.business.dto.payment.response;

import com.crediario.crediario_api.business.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        Long installmentId,
        LocalDateTime date,
        BigDecimal originalAmount,
        BigDecimal fineAmount,
        BigDecimal interestAmount,
        BigDecimal paidAmount,
        PaymentMethod paymentMethod
) { }

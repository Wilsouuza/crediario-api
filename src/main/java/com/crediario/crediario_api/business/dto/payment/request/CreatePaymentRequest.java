package com.crediario.crediario_api.business.dto.payment.request;

import com.crediario.crediario_api.business.enums.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record CreatePaymentRequest(
        @NotNull Long installmentId,
        @NotNull PaymentMethod paymentMethod
        ) {}

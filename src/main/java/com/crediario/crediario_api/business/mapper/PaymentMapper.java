package com.crediario.crediario_api.business.mapper;

import com.crediario.crediario_api.business.dto.payment.response.PaymentResponse;
import com.crediario.crediario_api.business.entity.Payment;

public class PaymentMapper {

    public static PaymentResponse toResponse(Payment payment){
        return new PaymentResponse(
                payment.getId(),
                payment.getInstallment().getId(),
                payment.getDate(),
                payment.getOriginalAmount(),
                payment.getFineAmount(),
                payment.getInterestAmount(),
                payment.getPaidAmount(),
                payment.getPaymentMethod()
        );
    }
}

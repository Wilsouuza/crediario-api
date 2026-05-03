package com.crediario.crediario_api.business.mapper;

import com.crediario.crediario_api.business.dto.installment.response.InstallmentResponse;
import com.crediario.crediario_api.business.entity.Installment;
import com.crediario.crediario_api.business.entity.Purchase;

public class InstallmentMapper {

    public static InstallmentResponse toResponse(Installment installment){

         return  new InstallmentResponse(
                installment.getId(),
                installment.getPurchase().getId(),
                installment.getValue(),
                installment.getDueDate(),
                installment.getStatus()
        );
    }
}

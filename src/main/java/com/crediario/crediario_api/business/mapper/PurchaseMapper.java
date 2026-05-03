package com.crediario.crediario_api.business.mapper;

import com.crediario.crediario_api.business.dto.purchase.request.CreatePurchaseRequest;
import com.crediario.crediario_api.business.dto.purchase.response.PurchaseResponse;
import com.crediario.crediario_api.business.entity.Customer;
import com.crediario.crediario_api.business.entity.Purchase;

public class PurchaseMapper {

    public static Purchase toEntity(CreatePurchaseRequest request, Customer customer) {
        return new Purchase(
                customer,
                request.value(),
                request.qtyInstallments(),
                request.description()
        );
    }

    public static PurchaseResponse toResponse(Purchase purchase) {
        return new PurchaseResponse(
                purchase.getId(),
                CustomerMapper.toResponse(purchase.getCustomer()),
                purchase.getValue(),
                purchase.getDate(),
                purchase.getQtyInstallments(),
                purchase.getDescription()
        );
    }
}

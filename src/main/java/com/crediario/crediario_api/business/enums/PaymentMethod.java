package com.crediario.crediario_api.business.enums;

public enum PaymentMethod {
    PIX("Pix"),
    CASH("Dinheiro");

    private final String description;

    PaymentMethod(String description) {
        this.description = description;
    }
}

package com.crediario.crediario_api.business.enums;

public enum InstallmentStatus {
    PENDING("Pendente"),
    LATE("Atrasada"),
    PAID("Paga");

    private final String description;

    InstallmentStatus(String description) {
        this.description = description;
    }
}

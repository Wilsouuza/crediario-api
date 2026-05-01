package com.crediario.crediario_api.business.enums;

public enum UserType {
    CUSTOMER("Cliente"),
    CASHIER("Caixa"),
    SELLER("Venadedor"),
    ADMIN("Administrador");

    private final String description;

    UserType(String description) {
        this.description = description;
    }
}

package com.crediario.crediario_api.business.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Setter
@Getter
@Component
public class SystemConfig {

    private BigDecimal interestRatePerDay = new BigDecimal("0.001");
    private BigDecimal fineRate = new BigDecimal("0.02");
    private int maxInstallments = 6;
    private BigDecimal minPurchaseAmount = new BigDecimal("10.00");
    private BigDecimal defaultCreditLimit = new BigDecimal("500.00");

}
package com.crediario.crediario_api.business.entity;

import com.crediario.crediario_api.business.enums.PaymentMethod;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "installment_id")
    private Installment installment;

    @NotNull
    @Column(updatable = false)
    private LocalDateTime date = LocalDateTime.now();

    @NotNull
    @Column(name = "original_amount",precision = 10, scale = 2)
    private BigDecimal originalAmount;

    @NotNull
    @Column(name = "fine_amount",precision = 10, scale = 2)
    private BigDecimal fineAmount;

    @NotNull
    @Column(name = "interest_amount",precision = 10, scale = 2)
    private BigDecimal interestAmount;

    @NotNull
    @Column(name = "paid_amount",precision = 10, scale = 2)
    private BigDecimal paidAmount;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
}

package com.crediario.crediario_api.business.entity;


import com.crediario.crediario_api.business.enums.InstallmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "installments")
public class Installment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;

    @NotNull
    @Column(precision = 10, scale = 2)
    private BigDecimal value;

    @NotNull
    @Column(name = "due_date", updatable = false)
    private LocalDate dueDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private InstallmentStatus status = InstallmentStatus.PENDING;

    public Installment(Purchase purchase, BigDecimal value, LocalDate dueDate) {
        this.purchase = purchase;
        this.value = value;
        this.dueDate = dueDate;

    }
}

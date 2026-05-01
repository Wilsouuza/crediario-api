package com.crediario.crediario_api.business.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "purchases")
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @NotNull
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @NotNull
    @Column(precision = 10, scale = 2)
    private BigDecimal value;

    @NotNull
    @Column(updatable = false)
    private LocalDate date = LocalDate.now();

    @NotNull
    @Column(name = "qty_installments")
    private int qtyInstallments;

    @NotBlank
    private String description;
}

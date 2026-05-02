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
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    @Column(unique = true)
    private String cpf;

    @NotBlank
    private String phone;

    @NotBlank
    private String profession;

    @NotNull
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "credit_limit", precision = 10, scale = 2)
    private BigDecimal creditLimit = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "registered_by")
    private User registeredBy;

    @NotNull
    @Column(name = "registration_date", updatable = false)
    private LocalDateTime registrationDate = LocalDateTime.now();

    public Customer(String name, String cpf, String phone, String profession, LocalDate birthDate, BigDecimal creditLimit, User registeredBy) {
        this.name = name;
        this.cpf = cpf;
        this.phone = phone;
        this.profession = profession;
        this.birthDate = birthDate;
        this.creditLimit = creditLimit;
        this.registeredBy = registeredBy;
    }
}

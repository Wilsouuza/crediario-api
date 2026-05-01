package com.crediario.crediario_api.infrastructure.repository;

import com.crediario.crediario_api.business.entity.Customer;
import com.crediario.crediario_api.business.entity.Payment;
import com.crediario.crediario_api.business.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    public List<Payment> findByInstallmentPurchase(Purchase purchase);
    public List<Payment> findByInstallmentPurchaseCustomer(Customer customer);
    public List<Payment> findByDateBetween(LocalDateTime start, LocalDateTime end);
}

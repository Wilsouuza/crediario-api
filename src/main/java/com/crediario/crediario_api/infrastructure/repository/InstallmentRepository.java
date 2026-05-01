package com.crediario.crediario_api.infrastructure.repository;

import com.crediario.crediario_api.business.entity.Customer;
import com.crediario.crediario_api.business.entity.Installment;
import com.crediario.crediario_api.business.entity.Purchase;
import com.crediario.crediario_api.business.enums.InstallmentStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstallmentRepository extends JpaRepository<Installment, Long> {
    public List<Installment> findByPurchase(Purchase purchase);
    public List<Installment> findByPurchaseCustomer(Customer customer);
    public List<Installment> findByStatus(InstallmentStatus status);
    public List<Installment> findByPurchaseCustomerAndStatus(Customer customer, InstallmentStatus status);

    @Modifying
    @Transactional
    @Query("UPDATE Installment i SET i.status = :status WHERE i.id = :id")
    void updateStatus(Long id, InstallmentStatus status);

}

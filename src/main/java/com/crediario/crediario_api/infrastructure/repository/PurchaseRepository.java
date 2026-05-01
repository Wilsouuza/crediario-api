package com.crediario.crediario_api.infrastructure.repository;

import com.crediario.crediario_api.business.entity.Customer;
import com.crediario.crediario_api.business.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    public List<Purchase> findByCustomer(Customer customer);
}

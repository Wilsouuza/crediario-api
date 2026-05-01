package com.crediario.crediario_api.infrastructure.repository;

import com.crediario.crediario_api.business.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    public Optional<Customer> findByCpf(String cpf);
}

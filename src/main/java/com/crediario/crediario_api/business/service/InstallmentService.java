package com.crediario.crediario_api.business.service;

import com.crediario.crediario_api.business.dto.installment.response.InstallmentResponse;
import com.crediario.crediario_api.business.entity.Customer;
import com.crediario.crediario_api.business.entity.Installment;
import com.crediario.crediario_api.business.entity.Purchase;
import com.crediario.crediario_api.business.enums.InstallmentStatus;
import com.crediario.crediario_api.business.exception.BusinessException;
import com.crediario.crediario_api.business.mapper.InstallmentMapper;
import com.crediario.crediario_api.infrastructure.repository.CustomerRepository;
import com.crediario.crediario_api.infrastructure.repository.InstallmentRepository;
import com.crediario.crediario_api.infrastructure.repository.PurchaseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InstallmentService {
    private  final InstallmentRepository installmentRepository;
    private final PurchaseRepository purchaseRepository;
    private final CustomerRepository customerRepository;

    public InstallmentService(InstallmentRepository installmentRepository, PurchaseRepository purchaseRepository, CustomerRepository customerRepository) {
        this.installmentRepository = installmentRepository;
        this.purchaseRepository = purchaseRepository;
        this.customerRepository = customerRepository;
    }

    public void generateInstallments(Purchase purchase){
        BigDecimal installmentValue = purchase.getValue().divide(new BigDecimal(purchase.getQtyInstallments()), 2, RoundingMode.HALF_UP);

        for (int i = 0; i < purchase.getQtyInstallments(); i++) {
            LocalDate dueDate = purchase.getDate().plusMonths(i + 1);
            Installment installment = new Installment(purchase,installmentValue, dueDate);
            installmentRepository.save(installment);
        }
    }

    public List<InstallmentResponse> findByPurchase (Long purchaseId){
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(()-> new BusinessException("Purchase not found."));

        return installmentRepository.findByPurchase(purchase)
                .stream()
                .map(InstallmentMapper::toResponse)
                .toList();
    }

    public List<InstallmentResponse> findByCustomer(String cpf){
        Customer customer = customerRepository.findByCpf(cpf)
                .orElseThrow(()-> new BusinessException("Customer not found."));

        return installmentRepository.findByPurchaseCustomer(customer)
                .stream()
                .map(InstallmentMapper::toResponse)
                .toList();
    }
    public boolean hasLateInstallments(Customer customer){
        List<Installment> installments = installmentRepository.findByPurchaseCustomerAndStatus(customer,InstallmentStatus.LATE);
        return !installments.isEmpty();
    }

    public void updateOverdueInstallments(){
        List<Installment> installments = installmentRepository.findAll();

        for (Installment i : installments){
            if (i.getStatus() == InstallmentStatus.PENDING && i.getDueDate().isBefore(LocalDate.now())){
                i.setStatus(InstallmentStatus.LATE);
                installmentRepository.save(i);
            }
        }
    }

    public List<Installment> getOpenInstallmentByCustomer(Long id){
        Customer customer = customerRepository.findById(id)
                .orElseThrow(()-> new BusinessException("Customer not found."));

        List<Installment> lateInstallments = installmentRepository.findByPurchaseCustomerAndStatus(customer,InstallmentStatus.LATE);
        List<Installment> pendingInstallments = installmentRepository.findByPurchaseCustomerAndStatus(customer, InstallmentStatus.PENDING);

        List<Installment> openInstallments = new ArrayList<>();

        openInstallments.addAll(lateInstallments);
        openInstallments.addAll(pendingInstallments);

        return openInstallments;

    }
}

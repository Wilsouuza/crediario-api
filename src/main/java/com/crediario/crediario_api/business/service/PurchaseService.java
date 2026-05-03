package com.crediario.crediario_api.business.service;

import com.crediario.crediario_api.business.config.SystemConfig;
import com.crediario.crediario_api.business.dto.customer.response.CustomerResponse;
import com.crediario.crediario_api.business.dto.purchase.request.CreatePurchaseRequest;
import com.crediario.crediario_api.business.dto.purchase.response.PurchaseResponse;
import com.crediario.crediario_api.business.entity.Customer;
import com.crediario.crediario_api.business.entity.Purchase;
import com.crediario.crediario_api.business.exception.BusinessException;
import com.crediario.crediario_api.business.mapper.CustomerMapper;
import com.crediario.crediario_api.business.mapper.PurchaseMapper;
import com.crediario.crediario_api.infrastructure.repository.CustomerRepository;
import com.crediario.crediario_api.infrastructure.repository.PurchaseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseService {
    private final CustomerService customerService;
    private final SystemConfig systemConfig;
    private final UserService userService;
    private final PurchaseRepository purchaseRepository;

    //TODO
    //InstallmentService installmentService;


    public PurchaseService(PurchaseRepository purchaseRepository, CustomerService customerService, SystemConfig systemConfig, UserService userService) {
        this.purchaseRepository = purchaseRepository;
        this.customerService = customerService;
        this.systemConfig = systemConfig;
        this.userService = userService;
    }

    public PurchaseResponse createPurchase(CreatePurchaseRequest request) {
        Customer customer = customerService.findEntityById(request.customerId());

        validatePurchase(request,customer);

        List<Purchase> existingPurchases = purchaseRepository.findByCustomer(customer);

        Purchase newPurchase = new Purchase(customer, request.value(),request.qtyInstallments(), request.description());
        Purchase savedPurchase = purchaseRepository.save(newPurchase);
        //installmentService.generateInstallments(savedPurchase);

        checkBonusCommission(customer,existingPurchases);

        return PurchaseMapper.toResponse(savedPurchase);
    }

    private void validatePurchase(CreatePurchaseRequest request, Customer customer){
        if (request.value().compareTo(customerService.getAvailableLimit(customer.getId())) > 0) {
            throw new BusinessException("Insufficient Limit");
        }

        /*if (installmentService.hasLateInstallments(customer.getId())){
            throw new BusinessException("Customer has late installments");
        }*/

        if (request.value().compareTo(systemConfig.getMinPurchaseAmount()) < 0){
            throw new BusinessException("Value below the minimum purchase.");
        }

        if (request.qtyInstallments()<= 0){
            throw new BusinessException("Qty installments must be greater than zero.");
        }

        if(request.qtyInstallments() > systemConfig.getMaxInstallments()){
            throw new BusinessException("Max installments exceeded.");
        }
    }

    private void checkBonusCommission(Customer customer, List<Purchase> existingPurchases) {
        long hoursBetween = ChronoUnit.HOURS.between(customer.getRegistrationDate(), LocalDateTime.now());
        if (existingPurchases.isEmpty() && hoursBetween <= 24) {
            userService.addCommission(customer.getRegisteredBy().getId(), new BigDecimal("10.00"));
        }
    }

    public List<PurchaseResponse> findAll(){
        return purchaseRepository.findAll()
                .stream()
                .map(PurchaseMapper::toResponse)
                .toList();
    }

    public PurchaseResponse findById(Long id){
        Purchase purchase =  purchaseRepository.findById(id)
                .orElseThrow(()-> new BusinessException("Purchase not found"));

        return PurchaseMapper.toResponse(purchase);
    }

    public List<PurchaseResponse> findByCustomer(Long id) {
        Customer customer = customerService.findEntityById(id);

        return purchaseRepository.findByCustomer(customer)
                .stream()
                .map(PurchaseMapper::toResponse)
                .toList();
    }
}

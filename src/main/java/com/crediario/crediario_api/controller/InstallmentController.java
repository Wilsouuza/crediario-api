package com.crediario.crediario_api.controller;

import com.crediario.crediario_api.business.dto.installment.response.InstallmentResponse;
import com.crediario.crediario_api.business.service.InstallmentService;
import com.crediario.crediario_api.business.service.PurchaseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/installments")
public class InstallmentController {

    private final InstallmentService installmentService;

    @GetMapping("/purchase/{purchaseId}")
    public ResponseEntity<List<InstallmentResponse>> getByPurchase(@PathVariable Long purchaseId){
        return ResponseEntity.ok(installmentService.findByPurchase(purchaseId));
    }

    @GetMapping("/customer/{cpf}")
    public ResponseEntity<List<InstallmentResponse>> getByCpf(@PathVariable String cpf){
        return ResponseEntity.ok(installmentService.findByCustomer(cpf));
    }

    @PutMapping("/update-overdue")
    public ResponseEntity<Void> updateOverdueInstallments(){
        installmentService.updateOverdueInstallments();
        return ResponseEntity.noContent().build();
    }

}

package com.crediario.crediario_api.controller;

import com.crediario.crediario_api.business.dto.purchase.request.CreatePurchaseRequest;
import com.crediario.crediario_api.business.dto.purchase.response.PurchaseResponse;
import com.crediario.crediario_api.business.service.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    @GetMapping
    public ResponseEntity<List<PurchaseResponse>> findAll(){
        return ResponseEntity.ok(purchaseService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(purchaseService.findById(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<PurchaseResponse>> findByCustomer(@PathVariable Long customerId){
        return ResponseEntity.ok(purchaseService.findByCustomer(customerId));
    }

    @PostMapping
    public ResponseEntity<PurchaseResponse> createPurchase (@Valid @RequestBody CreatePurchaseRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(purchaseService.createPurchase(request));
    }
}

package com.crediario.crediario_api.controller;

import com.crediario.crediario_api.business.dto.payment.request.CreatePaymentRequest;
import com.crediario.crediario_api.business.dto.payment.response.PaymentResponse;
import com.crediario.crediario_api.business.service.PaymentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> findAll(){
        return ResponseEntity.ok(paymentService.findAll());
    }

    @GetMapping("customer/{cpf}")
    public ResponseEntity<List<PaymentResponse>> findByCustomer(@PathVariable String cpf){
        return ResponseEntity.ok(paymentService.findByCustomer(cpf));
    }

    @GetMapping("/purchase/{purchaseId}")
    public ResponseEntity<List<PaymentResponse>> findByPurchase(@PathVariable Long purchaseId){
        return ResponseEntity.ok(paymentService.findByPurchase(purchaseId));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<PaymentResponse>> findByDateRange(@RequestParam LocalDateTime start, @RequestParam LocalDateTime end){
        return ResponseEntity.ok(paymentService.findByDateRange(start,end));
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody CreatePaymentRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createPayment(request));
    }
}

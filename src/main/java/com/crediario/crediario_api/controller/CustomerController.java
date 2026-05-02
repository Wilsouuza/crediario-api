package com.crediario.crediario_api.controller;

import com.crediario.crediario_api.business.dto.customer.request.CreateCustomerRequest;
import com.crediario.crediario_api.business.dto.customer.response.CustomerResponse;
import com.crediario.crediario_api.business.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> findAll(){
        return ResponseEntity.ok(customerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> findById(@PathVariable Long id){
        return ResponseEntity.ok(customerService.findById(id));
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<CustomerResponse> findByCpf (@PathVariable String cpf){
        return ResponseEntity.ok(customerService.findByCpf(cpf));
    }

    @GetMapping("/{id}/balance")
    public ResponseEntity<BigDecimal> getAvailableCreditLimit(@PathVariable Long id){
        return ResponseEntity.ok(customerService.getAvailableLimit(id));
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer (@Valid  @RequestBody CreateCustomerRequest request){
        System.out.println("Request received: " + request);
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(request));
    }
}

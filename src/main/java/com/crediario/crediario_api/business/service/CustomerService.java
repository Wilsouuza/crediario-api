package com.crediario.crediario_api.business.service;

import com.crediario.crediario_api.business.config.SystemConfig;
import com.crediario.crediario_api.business.dto.customer.request.CreateCustomerRequest;
import com.crediario.crediario_api.business.dto.customer.response.CustomerResponse;
import com.crediario.crediario_api.business.entity.Customer;
import com.crediario.crediario_api.business.entity.Installment;
import com.crediario.crediario_api.business.entity.User;
import com.crediario.crediario_api.business.exception.BusinessException;
import com.crediario.crediario_api.business.mapper.CustomerMapper;
import com.crediario.crediario_api.infrastructure.repository.CustomerRepository;
import com.crediario.crediario_api.infrastructure.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final SystemConfig systemConfig;
    private final InstallmentService installmentService;

    public CustomerService(CustomerRepository customerRepository, UserRepository userRepository, UserService userService, SystemConfig systemConfig, InstallmentService installmentService) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.systemConfig = systemConfig;
        this.installmentService = installmentService;
    }

    public CustomerResponse createCustomer(CreateCustomerRequest request){
        Optional<Customer> existing = customerRepository.findByCpf(request.cpf());
        if (existing.isPresent()){
            throw new BusinessException("CPF already registered.");
        }

       User registeredBy = userRepository.findById(request.registeredById())
               .orElseThrow(() -> new  BusinessException("User not found"));

        BigDecimal creditLimit = systemConfig.getDefaultCreditLimit();

        Customer newCustomer =  CustomerMapper.toEntity(request,registeredBy,creditLimit);
        customerRepository.save(newCustomer);

        userService.addCommission(registeredBy.getId(), new BigDecimal("4.00"));

        return CustomerMapper.toResponse(newCustomer);
    }

    public List<CustomerResponse> findAll(){
        return customerRepository.findAll()
                .stream()
                .map(CustomerMapper::toResponse)
                .toList();
    }

    public CustomerResponse findById(Long id){
        Customer customer = customerRepository.findById(id)
                .orElseThrow(()-> new BusinessException("Customer not found"));

        return CustomerMapper.toResponse(customer);
    }

    public CustomerResponse findByCpf(String cpf){
        Customer customer = customerRepository.findByCpf(cpf)
                .orElseThrow(()-> new BusinessException("Customer not found"));

        return CustomerMapper.toResponse(customer);
    }

    public Customer findByEntityByCpf(String cpf){
        return customerRepository.findByCpf(cpf)
                .orElseThrow(()-> new BusinessException("Customer not found"));
    }

    public Customer findEntityById(Long id){
        return customerRepository.findById(id)
                .orElseThrow(()-> new BusinessException("Customer not found."));
    }

    public BigDecimal getAvailableLimit(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Customer not found."));

        List<Installment> openInstallments = installmentService.getOpenInstallmentByCustomer(customer.getId());

        BigDecimal totalDebit = openInstallments.stream()
                .map(Installment::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return customer.getCreditLimit().subtract(totalDebit);
    }

}

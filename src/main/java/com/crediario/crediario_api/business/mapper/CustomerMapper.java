package com.crediario.crediario_api.business.mapper;

import com.crediario.crediario_api.business.dto.customer.request.CreateCustomerRequest;
import com.crediario.crediario_api.business.dto.customer.response.CustomerResponse;
import com.crediario.crediario_api.business.entity.Customer;
import com.crediario.crediario_api.business.entity.User;

import java.math.BigDecimal;

public class CustomerMapper {

    public static Customer toEntity(CreateCustomerRequest request, User registeredBy, BigDecimal creditLimit){
        return new Customer(
                request.name(),
                request.cpf(),
                request.phone(),
                request.profession(),
                request.birthDate(),
                creditLimit,
                registeredBy

        );
    }

    public static CustomerResponse toResponse(Customer customer){
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getCpf(),
                customer.getPhone(),
                customer.getProfession(),
                customer.getBirthDate(),
                customer.getCreditLimit(),
                customer.getRegistrationDate(),
                UserMapper.toResponse(customer.getRegisteredBy())
        );
    }
}

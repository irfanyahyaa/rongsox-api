package com.enigma.bank_sampah.service;

import com.enigma.bank_sampah.dto.request.SearchCustomerRequest;
import com.enigma.bank_sampah.dto.request.UpdateCustomerRequest;
import com.enigma.bank_sampah.dto.response.CustomerResponse;
import com.enigma.bank_sampah.entity.Customer;
import org.springframework.data.domain.Page;

public interface CustomerService {
    Customer create(Customer customer);

    Page<Customer> getAll(SearchCustomerRequest request);

    Customer getByIdEntity(String id);

    Customer getByUserAccountId(String id);

    CustomerResponse getByIdDTO(String id);

    CustomerResponse update(UpdateCustomerRequest customer);

    void updateStatusById(String id, Boolean status);

    void deleteById(String id);

    void findByPhoneNumber(String phoneNumber);
}

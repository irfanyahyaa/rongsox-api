package com.enigma.bank_sampah.service;

import com.enigma.bank_sampah.entity.Customer;
import org.springframework.data.domain.Page;

public interface CustomerService {
    Customer create(Customer customer);
//    CustomerResponse getOneById(String id);
    Customer getById(String id);
    Customer getByIdUserAccount(String id);
//    Page<CustomerResponse> getAll(SearchCustomerRequest request);
//    CustomerResponse update(UpdateCustomerRequest customer);
    void deleteById(String id);
    void updateStatusById(String id, Boolean status);

//    Customer getByToken();
}

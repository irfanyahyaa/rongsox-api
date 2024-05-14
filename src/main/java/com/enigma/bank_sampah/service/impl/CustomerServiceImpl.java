package com.enigma.bank_sampah.service.impl;

import com.enigma.bank_sampah.constant.ResponseMessage;
import com.enigma.bank_sampah.dto.request.SearchCustomerRequest;
import com.enigma.bank_sampah.dto.request.UpdateCustomerRequest;
import com.enigma.bank_sampah.dto.response.BankAccountResponse;
import com.enigma.bank_sampah.dto.response.CustomerResponse;
import com.enigma.bank_sampah.entity.Customer;
import com.enigma.bank_sampah.entity.UserAccount;
import com.enigma.bank_sampah.repository.CustomerRepository;
import com.enigma.bank_sampah.service.CustomerService;
import com.enigma.bank_sampah.service.ImageService;
import com.enigma.bank_sampah.service.UserAccountService;
import com.enigma.bank_sampah.specification.CustomerSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final UserAccountService userAccountService;
    private final CustomerRepository customerRepository;
    private final ImageService imageService;
    private final PasswordEncoder passwordEncoder;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Customer create(Customer customer) {
        return customerRepository.saveAndFlush(customer);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<CustomerResponse> getAll(SearchCustomerRequest request) {
        if (request.getPage() <= 0) request.setPage(1);

        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());

        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getSize(), sort);
        Specification<Customer> specification = CustomerSpecification.getSpecification(request);

        Page<Customer> customerPage = customerRepository.findAll(specification, pageable);

        return customerPage.map(customer -> CustomerResponse.builder()
                .id(customer.getId())
                .address(customer.getAddress())
                .name(customer.getName())
                .birthDate(customer.getBirthDate())
                .phoneNumber(customer.getPhoneNumber())
                .ktpNumber(customer.getKtpNumber())
                .ktpImage(customer.getKtpImage())
                .profileImage(customer.getProfileImage())
                .email(customer.getUserAccount().getEmail())
                .username(customer.getUserAccount().getUsername())
                .bankAccounts(customer.getBankAccounts().stream().map(bankAccount ->
                        BankAccountResponse.builder()
                                .id(bankAccount.getId())
                                .bankId(bankAccount.getBank().getId())
                                .bankCode(bankAccount.getBank().getBankCode())
                                .bankName(bankAccount.getBank().getName())
                                .accountNumber(bankAccount.getAccountNumber())
                                .dateCreated(bankAccount.getDateCreated())
                                .customerId(bankAccount.getCustomer().getId())
                                .status(bankAccount.getStatus())
                                .build()).toList())
                .status(customer.getStatus())
                .build());
    }

    @Transactional(readOnly = true)
    @Override
    public Customer getByIdEntity(String id) {
        return findByIdOrThrowNotFound(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Customer getByUserAccountId(String id) {
        return customerRepository.findByUserAccount_Id(id).orElse(null);
    }

    @Transactional(readOnly = true)
    @Override
    public CustomerResponse getByIdDTO(String id) {
        Customer customerFound = findByIdOrThrowNotFound(id);

        List<BankAccountResponse> bankAccountResponses = customerFound.getBankAccounts().stream().map(bankAccount ->
                BankAccountResponse.builder()
                        .id(bankAccount.getId())
                        .accountNumber(bankAccount.getAccountNumber())
                        .dateCreated(bankAccount.getDateCreated())
                        .customerId(bankAccount.getCustomer().getId())
                        .status(bankAccount.getStatus())
                        .build()).toList();

        return CustomerResponse.builder()
                .id(customerFound.getId())
                .address(customerFound.getAddress())
                .name(customerFound.getName())
                .birthDate(customerFound.getBirthDate())
                .phoneNumber(customerFound.getPhoneNumber())
                .ktpNumber(customerFound.getKtpNumber())
                .ktpImage(customerFound.getKtpImage())
                .profileImage(customerFound.getProfileImage())
                .email(customerFound.getUserAccount().getEmail())
                .username(customerFound.getUserAccount().getUsername())
                .bankAccounts(bankAccountResponses)
                .status(customerFound.getStatus())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public CustomerResponse update(UpdateCustomerRequest request) {
        Customer customerFound = findByIdOrThrowNotFound(request.getId());

        if (request.getPhoneNumber() != null && !request.getPhoneNumber().equals(customerFound.getPhoneNumber())) {
            findByPhoneNumber(request.getPhoneNumber());
        } else {
            customerFound.setPhoneNumber(null);
        }

        if (request.getKtpNumber() != null && !request.getKtpNumber().equals(customerFound.getKtpNumber())) {
            findByKtpNumber(request.getKtpNumber());
        } else {
            customerFound.setKtpNumber(null);
        }

        Customer customer = Customer.builder()
                .id(customerFound.getId())
                .address(request.getAddress())
                .name(request.getName())
                .birthDate(request.getBirthDate())
                .phoneNumber(request.getPhoneNumber())
                .ktpNumber(request.getKtpNumber())
                .ktpImage(request.getKtpImage())
                .profileImage(request.getProfileImage())
                .userAccount(customerFound.getUserAccount())
                .bankAccounts(customerFound.getBankAccounts())
                .status(customerFound.getStatus())
                .build();
        customerRepository.saveAndFlush(customer);

        return CustomerResponse.builder()
                .id(customer.getId())
                .address(customer.getAddress())
                .name(customer.getName())
                .birthDate(customer.getBirthDate())
                .phoneNumber(customer.getPhoneNumber())
                .ktpNumber(customer.getKtpNumber())
                .ktpImage(customer.getKtpImage())
                .profileImage(customer.getProfileImage())
                .email(customer.getUserAccount().getEmail())
                .username(customer.getUserAccount().getUsername())
                .status(customer.getStatus())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStatusById(String id, Boolean status) {
        findByIdOrThrowNotFound(id);

        customerRepository.updateStatus(id, status);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        Customer customer = findByIdOrThrowNotFound(id);
        UserAccount userAccount = userAccountService.getByUserId(customer.getUserAccount().getId());

        if (customer.getKtpImage() != null) {
            imageService.deleteById(customer.getKtpImage().getId());
        }

        if (customer.getProfileImage() != null) {
            imageService.deleteById(customer.getProfileImage().getId());
        }

        customerRepository.updateStatus(id, false);
        userAccount.setIsEnable(false);
    }

    @Override
    public void findByPhoneNumber(String phoneNumber) {
        Optional<Customer> existingPhoneNumber = customerRepository.findByPhoneNumber(phoneNumber);

        if (existingPhoneNumber.isPresent()) {
            throw new DataIntegrityViolationException("Phone number already exists");
        }
    }

    @Override
    public void findByKtpNumber(String ktpNumber) {
        Optional<Customer> existingKtpNumber = customerRepository.findByKtpNumber(ktpNumber);

        if (existingKtpNumber.isPresent()) {
            throw new DataIntegrityViolationException("Ktp number already exists");
        }
    }

    private Customer findByIdOrThrowNotFound(String id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
    }
}

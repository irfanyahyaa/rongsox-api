package com.enigma.bank_sampah.service.impl;

import com.enigma.bank_sampah.constant.ResponseMessage;
import com.enigma.bank_sampah.dto.request.BankAccountRequest;
import com.enigma.bank_sampah.dto.request.SearchBankAccountRequest;
import com.enigma.bank_sampah.dto.request.UpdateBankAccountRequest;
import com.enigma.bank_sampah.dto.response.BankAccountResponse;
import com.enigma.bank_sampah.entity.Bank;
import com.enigma.bank_sampah.entity.BankAccount;
import com.enigma.bank_sampah.entity.Customer;
import com.enigma.bank_sampah.repository.BankAccountRepository;
import com.enigma.bank_sampah.service.BankAccountService;
import com.enigma.bank_sampah.service.BankService;
import com.enigma.bank_sampah.service.CustomerService;
import com.enigma.bank_sampah.specification.BankAccountSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final CustomerService customerService;
    private final BankService bankService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BankAccountResponse create(BankAccountRequest request) {
        Customer customer = customerService.getByIdEntity(request.getCustomerId());
        Bank bank = bankService.getByIdEntity(request.getBankId());

        BankAccount bankAccount = BankAccount.builder()
                .accountNumber(request.getAccountNumber())
                .dateCreated(new Timestamp(System.currentTimeMillis()))
                .customer(customer)
                .bank(bank)
                .status(request.getStatus())
                .build();
        bankAccountRepository.saveAndFlush(bankAccount);

        return BankAccountResponse.builder()
                .id(bankAccount.getId())
                .bankId(bankAccount.getBank().getId())
                .bankCode(bankAccount.getBank().getBankCode())
                .bankName(bankAccount.getBank().getName())
                .accountNumber(bankAccount.getAccountNumber())
                .customerId(bankAccount.getCustomer().getId())
                .dateCreated(bankAccount.getDateCreated())
                .status(bankAccount.getStatus())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<BankAccountResponse> getAll(SearchBankAccountRequest request) {
        if (request.getPage() <= 0) request.setPage(1);

        Sort sort = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());

        Pageable pageable = PageRequest.of((request.getPage() - 1), request.getSize(), sort);
        Specification<BankAccount> specification = BankAccountSpecification.getSpecification(request);

        Page<BankAccount> bankAccountPage = bankAccountRepository.findAll(specification, pageable);

        return bankAccountPage.map(bankAccount -> BankAccountResponse.builder()
                .id(bankAccount.getId())
                .accountNumber(bankAccount.getAccountNumber())
                .dateCreated(bankAccount.getDateCreated())
                .customerId(bankAccount.getCustomer().getId())
                .bankId(bankAccount.getBank().getId())
                .status(bankAccount.getStatus())
                .build());
    }

    @Transactional(readOnly = true)
    @Override
    public BankAccount getByIdEntity(String id) {
        return findByIdOrThrowNotFound(id);
    }

    @Transactional(readOnly = true)
    @Override
    public BankAccountResponse getByIdDTO(String id) {
        BankAccount bankAccountFound = findByIdOrThrowNotFound(id);

        return BankAccountResponse.builder()
                .id(bankAccountFound.getId())
                .accountNumber(bankAccountFound.getAccountNumber())
                .dateCreated(bankAccountFound.getDateCreated())
                .customerId(bankAccountFound.getCustomer().getId())
                .bankId(bankAccountFound.getBank().getId())
                .status(bankAccountFound.getStatus())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BankAccountResponse update(UpdateBankAccountRequest request) {
        BankAccount bankAccountFound = findByIdOrThrowNotFound(request.getId());

        BankAccount bankAccount = BankAccount.builder()
                .id(bankAccountFound.getId())
                .accountNumber(request.getAccountNumber())
                .dateCreated(new Timestamp(System.currentTimeMillis()))
                .customer(bankAccountFound.getCustomer())
                .bank(bankAccountFound.getBank())
                .status(bankAccountFound.getStatus())
                .build();
        bankAccountRepository.saveAndFlush(bankAccount);

        return BankAccountResponse.builder()
                .id(bankAccount.getId())
                .accountNumber(bankAccount.getAccountNumber())
                .dateCreated(bankAccount.getDateCreated())
                .customerId(bankAccount.getCustomer().getId())
                .bankId(bankAccount.getBank().getId())
                .status(bankAccount.getStatus())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStatus(String id, Boolean status) {
        findByIdOrThrowNotFound(id);

        bankAccountRepository.updateStatus(id, status);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(String id) {
        findByIdOrThrowNotFound(id);

        bankAccountRepository.updateStatus(id, false);
    }

    private BankAccount findByIdOrThrowNotFound(String request) {
        return bankAccountRepository.findById(request)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ResponseMessage.ERROR_NOT_FOUND));
    }
}

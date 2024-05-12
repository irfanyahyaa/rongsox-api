package com.enigma.bank_sampah.service.impl;

import com.enigma.bank_sampah.constant.ResponseMessage;
import com.enigma.bank_sampah.dto.request.BankAccountRequest;
import com.enigma.bank_sampah.dto.response.BankAccountResponse;
import com.enigma.bank_sampah.entity.Bank;
import com.enigma.bank_sampah.entity.BankAccount;
import com.enigma.bank_sampah.entity.Customer;
import com.enigma.bank_sampah.repository.BankAccountRepository;
import com.enigma.bank_sampah.service.BankAccountService;
import com.enigma.bank_sampah.service.BankService;
import com.enigma.bank_sampah.service.CustomerService;
import lombok.RequiredArgsConstructor;
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
                .accountNumber(bankAccount.getAccountNumber())
                .customerId(bankAccount.getCustomer().getId())
                .dateCreated(bankAccount.getDateCreated())
                .bankId(bankAccount.getBank().getId())
                .status(bankAccount.getStatus())
                .build();
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

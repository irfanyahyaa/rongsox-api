package com.enigma.bank_sampah.service;

import com.enigma.bank_sampah.dto.request.BankAccountRequest;
import com.enigma.bank_sampah.dto.response.BankAccountResponse;

public interface BankAccountService {
    BankAccountResponse create(BankAccountRequest admin);

    void deleteById(String id);
}

package com.enigma.bank_sampah.service;

import com.enigma.bank_sampah.dto.request.BankRequest;
import com.enigma.bank_sampah.dto.request.SearchBankRequest;
import com.enigma.bank_sampah.dto.request.UpdateBankRequest;
import com.enigma.bank_sampah.dto.response.BankResponse;
import com.enigma.bank_sampah.entity.Bank;
import org.springframework.data.domain.Page;

public interface BankService {
    BankResponse create (BankRequest request);
    BankResponse getOneById(String id);
    Bank getById(String id);

    Page<BankResponse> getAll (SearchBankRequest request);

    BankResponse update (UpdateBankRequest request);

    Bank update(Bank bank);

    void deleteById(String id);

}

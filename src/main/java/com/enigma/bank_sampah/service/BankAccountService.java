package com.enigma.bank_sampah.service;

import com.enigma.bank_sampah.dto.request.BankAccountRequest;
import com.enigma.bank_sampah.dto.request.SearchBankAccountRequest;
import com.enigma.bank_sampah.dto.request.UpdateBankAccountRequest;
import com.enigma.bank_sampah.dto.response.BankAccountResponse;
import com.enigma.bank_sampah.entity.BankAccount;
import org.springframework.data.domain.Page;

public interface BankAccountService {
    BankAccountResponse create(BankAccountRequest admin);

    Page<BankAccountResponse> getAll(SearchBankAccountRequest request);

    BankAccount getByIdEntity(String id);

    BankAccountResponse getByIdDTO(String id);

    BankAccountResponse update(UpdateBankAccountRequest request);

    void updateStatus(String id, Boolean status);

    void deleteById(String id);
}

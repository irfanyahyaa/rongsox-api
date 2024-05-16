package com.enigma.bank_sampah.service;

import com.enigma.bank_sampah.dto.request.DepositRequest;
import com.enigma.bank_sampah.dto.request.SearchTransactionRequest;
import com.enigma.bank_sampah.dto.request.UpdateWithdrawalRequest;
import com.enigma.bank_sampah.dto.request.WithdrawalRequest;
import com.enigma.bank_sampah.dto.response.TransactionResponse;
import org.springframework.data.domain.Page;

public interface TransactionService {
    TransactionResponse createDeposit(DepositRequest request);

    TransactionResponse createWithdrawal(WithdrawalRequest request);

    Page<TransactionResponse> getAll(SearchTransactionRequest request);

    TransactionResponse updateStatusWithdrawal(UpdateWithdrawalRequest request);

    void updateStatusById(String id, String status);
}

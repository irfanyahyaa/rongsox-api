package com.enigma.bank_sampah.service;

import com.enigma.bank_sampah.entity.TransactionDetail;

import java.util.List;

public interface TransactionDetailService {
    List<TransactionDetail> createBulk(List<TransactionDetail> transactionDetails);
}

package com.enigma.bank_sampah.dto.response;

import com.enigma.bank_sampah.entity.Admin;
import com.enigma.bank_sampah.entity.BankAccount;
import com.enigma.bank_sampah.entity.Customer;
import com.enigma.bank_sampah.entity.Image;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {
    private String id;
    private Date transactionDate;
    private String transactionType;
    private String customerId;
    private String customerName;
    private String customerPhoneNumber;
    private String customerAddress;
    private String adminId;
    private String adminName;
    private String adminPhoneNumber;
    private String adminAddress;
    private String bankName;
    private String bankCode;
    private String accountNumber;
    private String paymentMethod;
    private Long amount;
    private ImageResponse transferReceipt;
    private List<TransactionDetailResponse> transactionDetails;
    private String status;
}

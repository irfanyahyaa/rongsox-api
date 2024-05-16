package com.enigma.bank_sampah.dto.request;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DepositRequest {
    private String transactionType;
    private String adminId;
    private String customerId;
    private Long amount;
    private String status;
    private List<TransactionDetailRequest> transactionDetails;
}

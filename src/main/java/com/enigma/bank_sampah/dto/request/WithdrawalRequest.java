package com.enigma.bank_sampah.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawalRequest {
    private String adminId;
    private String customerId;
    private String bankAccountId;
    private Long amount;
}

package com.enigma.bank_sampah.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankAccountRequest {
    private String id;
    private String accountNumber;
    private String customerId;
    private String bankId;
    private Boolean status;
}

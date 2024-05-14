package com.enigma.bank_sampah.dto.response;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankAccountResponse {
    private String id;
    private String bankId;
    private String bankCode;
    private String bankName;
    private String accountNumber;
    private Date dateCreated;
    private String customerId;
    private Boolean status;
}

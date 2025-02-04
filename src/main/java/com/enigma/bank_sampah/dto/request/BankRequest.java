package com.enigma.bank_sampah.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankRequest {
    private String bankCode;
    private String bankName;
}

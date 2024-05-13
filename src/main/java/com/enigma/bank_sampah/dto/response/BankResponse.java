package com.enigma.bank_sampah.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankResponse {
    private String id;
    private String bankCode;
    private String bankName;
}

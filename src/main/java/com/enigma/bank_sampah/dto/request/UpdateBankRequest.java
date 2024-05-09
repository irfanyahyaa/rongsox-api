package com.enigma.bank_sampah.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateBankRequest {
    private String bankId;
    private String bankName;
    private String bankCode;
}

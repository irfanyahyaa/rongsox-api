package com.enigma.bank_sampah.dto.request;

import com.enigma.bank_sampah.entity.Bank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateBankAccountRequest {
    private String id;
    private String accountNumber;
    private String bankId;
}

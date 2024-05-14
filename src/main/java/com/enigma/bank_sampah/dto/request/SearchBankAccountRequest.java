package com.enigma.bank_sampah.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchBankAccountRequest {
    private Integer page;
    private Integer size;
    private String sortBy;
    private String direction;
    private String accountNumber;
    private Boolean status;
}

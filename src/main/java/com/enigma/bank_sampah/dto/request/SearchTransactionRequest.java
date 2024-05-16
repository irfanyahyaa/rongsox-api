package com.enigma.bank_sampah.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchTransactionRequest {
    private Integer page;
    private Integer size;
    private String sortBy;
    private String direction;
    private String transactionType;
    private String status;
}

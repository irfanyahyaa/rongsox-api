package com.enigma.bank_sampah.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDetailRequest {
    private String stuffId;
    private Float weight;
    private Long amount;
}

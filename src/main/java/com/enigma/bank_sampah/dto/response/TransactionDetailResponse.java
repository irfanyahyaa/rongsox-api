package com.enigma.bank_sampah.dto.response;

import com.enigma.bank_sampah.entity.Stuff;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDetailResponse {
    private String id;
    private String stuffName;
    private Long buyingPrice;
    private Float weight;
}

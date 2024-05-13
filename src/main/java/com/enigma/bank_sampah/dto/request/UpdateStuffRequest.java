package com.enigma.bank_sampah.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateStuffRequest {
    private String id;
    private String stuffName;
    private Long buyingPrice;
    private Long sellingPrice;
    private Float weight;
    private Boolean status;
}

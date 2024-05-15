package com.enigma.bank_sampah.dto.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StuffResponse {
    private String id;
    private String stuffName;
    private Long buyingPrice;
    private Long sellingPrice;
    private Float weight;
    private ImageResponse image;
    private Boolean status;
}

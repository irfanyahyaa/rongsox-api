package com.enigma.bank_sampah.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchCustomerRequest {
    private Integer page;
    private Integer size;
    private String sortBy;
    private String direction;
    private String name;
    private Boolean status;
}

package com.enigma.bank_sampah.dto.request;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchStuffRequest {
    private Integer page;
    private Integer size;
    private String sortBy;
    private String direction;
    private String stuffName;
    private Boolean status;
}

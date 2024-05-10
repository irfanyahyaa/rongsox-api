package com.enigma.bank_sampah.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchAdminRequest {
    private Integer page;

    private Integer size;

    @Pattern(regexp = "[a-zA-Z]+", message = "sortBy must be alphabetic")
    private String sortBy;

    @Pattern(regexp = "[a-zA-Z]+", message = "direction must be alphabetic")
    private String direction;

    @Pattern(regexp = "[a-zA-Z]+", message = "name cannot be blank or must be alphabetic")
    private String name;

    private Boolean status;

    private String query;
}

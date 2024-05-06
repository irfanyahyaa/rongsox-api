package com.enigma.bank_sampah.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminRequest {
    private String username;
    private String password;
    private String name;
    private String email;
    private String address;
    private String position;
    private Boolean status;
}

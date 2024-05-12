package com.enigma.bank_sampah.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminRequest {
    private String email;
    private String username;
    private String password;
    private String name;
    private String phoneNumber;
    private String address;
}
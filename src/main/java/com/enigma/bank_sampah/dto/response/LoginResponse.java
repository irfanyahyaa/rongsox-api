package com.enigma.bank_sampah.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String userAccountId;
    private String AdminId;
    private String CustomerId;
    private String username;
    private String token;
    private List<String> roles;
}

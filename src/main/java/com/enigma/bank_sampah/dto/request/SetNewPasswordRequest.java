package com.enigma.bank_sampah.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SetNewPasswordRequest {
    private String otp;
    private String typeOtp;
    private String email;
    private String password;
}

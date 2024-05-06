package com.enigma.bank_sampah.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidationOtpRequest {
    private String otp;
    private String email;
    private String typeOtp;
}

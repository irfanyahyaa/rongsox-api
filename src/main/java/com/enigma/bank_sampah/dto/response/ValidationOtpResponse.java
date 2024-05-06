package com.enigma.bank_sampah.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValidationOtpResponse {
    private String email;
    private String typeOtp;
    private String message;
    private String otp;
    private Boolean isValid;
}

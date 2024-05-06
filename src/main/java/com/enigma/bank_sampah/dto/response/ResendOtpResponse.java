package com.enigma.bank_sampah.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResendOtpResponse {
    private String email;
    private String typeOtp;
    private String message;
}

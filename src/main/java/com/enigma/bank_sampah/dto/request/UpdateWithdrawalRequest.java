package com.enigma.bank_sampah.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateWithdrawalRequest {
    private String id;
    private MultipartFile image;
    private String status;
}

package com.enigma.bank_sampah.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StuffRequest {
    private String stuffName;
    private Long buyingPrice;
    private Long sellingPrice;
    private MultipartFile image;
}

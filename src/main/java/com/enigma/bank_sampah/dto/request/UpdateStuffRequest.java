package com.enigma.bank_sampah.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateStuffRequest {
    private String id;
    private String stuffName;
    private Long buyingPrice;
    private Long sellingPrice;
    private Float weight;
    private Boolean status;
    private MultipartFile image;
}

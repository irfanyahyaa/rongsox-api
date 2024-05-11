package com.enigma.bank_sampah.dto.request;

import com.enigma.bank_sampah.entity.Image;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateAdminRequest {
    private String id;
    private String address;
    private String name;
    private String position;
    private String phoneNumber;
    private Image image;
}

package com.enigma.bank_sampah.dto.response;

import com.enigma.bank_sampah.entity.Image;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminResponse {
    private String id;
    private String address;
    private String name;
    private String position;
    private String phoneNumber;
    private Image image;
    private String email;
    private String username;
    private Boolean status;
}

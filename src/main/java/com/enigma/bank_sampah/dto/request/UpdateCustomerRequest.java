package com.enigma.bank_sampah.dto.request;

import com.enigma.bank_sampah.entity.Image;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCustomerRequest {
    private String id;
    private String address;
    private String name;
    private Date birthDate;
    private String phoneNumber;
    private String ktpNumber;
    private Image ktpImage;
    private Image profileImage;
}

package com.enigma.bank_sampah.dto.response;

import com.enigma.bank_sampah.entity.Image;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerResponse {
    private String id;
    private String address;
    private String name;
    private Date birthDate;
    private String phoneNumber;
    private String ktpNumber;
    private Image ktpImage;
    private Image profileImage;
    private String email;
    private String username;
    private List<BankAccountResponse> bankAccounts;
    private Boolean status;
}

package com.enigma.bank_sampah.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerRequest {
    private String username;
    private String password;
    private String name;
    private String email;
    private String phoneNumber;
    private String ktpNumber;
    private String address;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;
}

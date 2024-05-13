package com.enigma.bank_sampah.service;

import com.enigma.bank_sampah.dto.response.JwtClaims;
import com.enigma.bank_sampah.entity.UserAccount;

public interface JwtService {
    String generateToken(UserAccount userAccount);

    boolean verifyJwtToken(String token);

    JwtClaims getClaimsByToken(String token);
}

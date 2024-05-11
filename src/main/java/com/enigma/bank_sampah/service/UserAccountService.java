package com.enigma.bank_sampah.service;


import com.enigma.bank_sampah.entity.UserAccount;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserAccountService extends UserDetailsService {
    UserAccount getByUserId(String id);
    UserAccount getByContext();
}

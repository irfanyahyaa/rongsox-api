package com.enigma.bank_sampah.service;

import com.enigma.bank_sampah.entity.Customer;
import com.enigma.bank_sampah.entity.Token;

public interface TokenService {

    Token createToken();

    void saveToken(Token token);

    Token findByToken(String token);

    void removeToken(Token token);

    void removeTokenByToken(String token);

    void removeTokenByCustomerAndType (Customer customer, String type);


}

package com.enigma.bank_sampah.service.impl;

import com.enigma.bank_sampah.entity.Customer;
import com.enigma.bank_sampah.entity.Token;
import com.enigma.bank_sampah.repository.TokenRepository;
import com.enigma.bank_sampah.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;

    @Override
    public Token createToken() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, 1);

        Random random = new Random();
        int otpValue = 100000 + random.nextInt(900000);
        Token token = new Token();
        token.setToken(String.valueOf(otpValue));
        token.setExpiredAt(calendar.getTime());
        return token;
    }

    @Override
    public void saveToken(Token token) {
        tokenRepository.saveAndFlush(token);
    }

    @Override
    public Token findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public void removeToken(Token token) {
        tokenRepository.delete(token);
    }

    @Override
    public void removeTokenByToken(String token) {
        Token foundToken = findByToken(token);
        if (foundToken != null) {
            removeToken(foundToken);
        }
    }

    @Override
    public void removeTokenByCustomerAndType(Customer customer, String type) {
        tokenRepository.removeTokensByCustomerAndTokenType(customer.getId(), type);
    }
}
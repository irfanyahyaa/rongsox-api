package com.enigma.bank_sampah.repository;

import com.enigma.bank_sampah.entity.Customer;
import com.enigma.bank_sampah.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token , String> {
    Token findByToken(String token);
    Long removeByToken(String token);
    Long removeTokensByCustomerAndTokenType(Customer customer, String tokenType);

}

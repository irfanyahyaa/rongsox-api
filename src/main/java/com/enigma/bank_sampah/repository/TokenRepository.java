package com.enigma.bank_sampah.repository;

import com.enigma.bank_sampah.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TokenRepository extends JpaRepository<Token, String> {
    Token findByToken(String token);

    Long removeByToken(String token);

    //    removeTokensByCustomerAndTokenType
    @Modifying
    @Query(value = "delete from m_user_token where customer_id = :customer_id and token_type = :token_type", nativeQuery = true)
    void removeTokensByCustomerAndTokenType(@Param("customer_id") String customer_id, @Param("token_type") String token_type);

}

package com.enigma.bank_sampah.repository;

import com.enigma.bank_sampah.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {


    Optional<UserAccount> findByUsername(String username);

    Optional<UserAccount> findByEmail(String email);
}

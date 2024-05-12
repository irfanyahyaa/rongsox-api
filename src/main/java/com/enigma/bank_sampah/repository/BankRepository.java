package com.enigma.bank_sampah.repository;

import com.enigma.bank_sampah.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<Bank, String>, JpaSpecificationExecutor<Bank> {
}

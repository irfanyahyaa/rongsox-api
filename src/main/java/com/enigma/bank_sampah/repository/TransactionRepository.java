package com.enigma.bank_sampah.repository;

import com.enigma.bank_sampah.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TransactionRepository extends JpaRepository<Transaction, String>, JpaSpecificationExecutor<Transaction> {
    @Modifying
    @Query(value = "UPDATE m_transaction SET status = :status where id = :id", nativeQuery = true)
    void updateStatus(@Param("id") String id, @Param("status") String status);
}

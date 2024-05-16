package com.enigma.bank_sampah.repository;

import com.enigma.bank_sampah.entity.TransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TransactionDetailRepository extends JpaRepository<TransactionDetail, String> {
}

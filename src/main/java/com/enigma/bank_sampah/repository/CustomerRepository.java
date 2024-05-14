package com.enigma.bank_sampah.repository;

import com.enigma.bank_sampah.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface CustomerRepository extends JpaRepository<Customer, String>, JpaSpecificationExecutor<Customer> {
    Optional<Customer> findByUserAccount_Id(String userAccount_id);

    Optional<Customer> findByPhoneNumber(String phoneNumber);

    Optional<Customer> findByKtpNumber(String ktpNumber);

    @Modifying
    @Query(value = "UPDATE m_customer SET status = :status WHERE id = :id", nativeQuery = true)
    void updateStatus(@Param("id") String id, @Param("status") Boolean status);
}

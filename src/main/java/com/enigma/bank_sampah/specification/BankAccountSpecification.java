package com.enigma.bank_sampah.specification;

import com.enigma.bank_sampah.dto.request.SearchBankAccountRequest;
import com.enigma.bank_sampah.entity.BankAccount;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BankAccountSpecification {
    public static Specification<BankAccount> getSpecification(SearchBankAccountRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getAccountNumber() != null) {
                predicates.add(cb.like(cb.lower(root.get("bankCode")), "%" + request.getAccountNumber().toLowerCase() + "%"));
            }

            if (request.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), request.getStatus()));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }
}

package com.enigma.bank_sampah.specification;

import com.enigma.bank_sampah.dto.request.SearchTransactionRequest;
import com.enigma.bank_sampah.entity.Transaction;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TransactionSpecification {
    public static Specification<Transaction> getSpecification(SearchTransactionRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getCustomerId() != null) {
                predicates.add(cb.like(cb.lower(root.get("customerId")), "%" + request.getCustomerId().toLowerCase() + "%"));
            }

            if (request.getTransactionType() != null) {
                predicates.add(cb.like(cb.lower(root.get("transactionType")), "%" + request.getTransactionType().toLowerCase() + "%"));
            }

            if (request.getStatus() != null) {
                predicates.add(cb.like(cb.lower(root.get("status")), "%" + request.getStatus().toLowerCase() + "%"));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }
}

package com.enigma.bank_sampah.specification;

import com.enigma.bank_sampah.entity.Bank;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class BankSpecification {
    public static Specification<Bank> getSpecification(String q) {
        return ((root, query, cb) -> {
            if (!StringUtils.hasText(q)) return cb.conjunction();

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + q.toLowerCase() + "%"));

            return cb.or(predicates.toArray(new Predicate[]{}));
        }
        );
    }
}

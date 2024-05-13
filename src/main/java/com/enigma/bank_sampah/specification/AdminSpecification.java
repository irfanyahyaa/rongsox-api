package com.enigma.bank_sampah.specification;

import com.enigma.bank_sampah.dto.request.SearchAdminRequest;
import com.enigma.bank_sampah.entity.Admin;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AdminSpecification {
    public static Specification<Admin> getSpecification(SearchAdminRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getName() != null) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + request.getName().toLowerCase() + "%"));
            }

            if (request.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), request.getStatus()));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }
}

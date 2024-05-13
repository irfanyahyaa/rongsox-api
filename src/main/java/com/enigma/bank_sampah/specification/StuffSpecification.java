package com.enigma.bank_sampah.specification;

import com.enigma.bank_sampah.dto.request.SearchStuffRequest;
import com.enigma.bank_sampah.entity.Stuff;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class StuffSpecification {
    public static Specification<Stuff> getSpecification(SearchStuffRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getStuffName() != null) {
                predicates.add(cb.like(cb.lower(root.get("stuff_name")), "%" + request.getStuffName().toLowerCase() + "%"));
            }

            if (request.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), request.getStatus()));
            }

            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }
}

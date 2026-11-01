package com.example.phone_store.repository;

import com.example.phone_store.entity.Invoice;
import org.springframework.data.jpa.domain.Specification;

public class InvoiceSpecification {

    public static Specification<Invoice> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) return null;
            String likePattern = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("customer").get("name")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("status")), likePattern)
            );
        };
    }
}

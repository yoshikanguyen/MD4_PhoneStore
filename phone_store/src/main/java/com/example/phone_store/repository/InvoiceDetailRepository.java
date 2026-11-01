package com.example.phone_store.repository;

import com.example.phone_store.entity.InvoiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceDetailRepository extends JpaRepository<InvoiceDetail, Integer> {
}

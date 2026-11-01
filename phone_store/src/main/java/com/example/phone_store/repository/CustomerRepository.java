package com.example.phone_store.repository;

import com.example.phone_store.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    List<Customer> findByIsDeletedFalse();

    Page<Customer> findByIsDeletedFalse(Pageable pageable);
}

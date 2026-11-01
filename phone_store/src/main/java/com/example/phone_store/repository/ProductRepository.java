package com.example.phone_store.repository;

import com.example.phone_store.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
    List<Product> findByIsDeletedFalse();

    List<Product> findByIsDeletedFalseAndBrandContainingIgnoreCaseOrNameContainingIgnoreCase(String brand, String name);
}

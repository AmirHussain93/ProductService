package com.example.productservice.repositories;

import com.example.productservice.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 1st argument: entity name for the repository
 * 2nd argument: Type of primary key
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    Product save(Product product);

    Optional<Product> findById(long id);

    List<Product> findAll();
}

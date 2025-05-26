package com.example.productservice.repositories;

import com.example.productservice.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    Category save(Category category);
    List<Category> findAll();

    @Query("select c from Category c join fetch c.products")
    List<Category> getCategoriesUsingJoinFetch();
}

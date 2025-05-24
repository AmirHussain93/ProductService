package com.example.productservice.repositories;

import com.example.productservice.models.Category;
import com.example.productservice.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    List<Product> findByCategory(Category category);

    List<Product> findByCategory_Name(String categoryName);

    @Query("SELECT p FROM Product p WHERE p.category.name = :categoryName")
    List<Product> getProductByCategoryName(@Param("categoryName") String categoryName);

    @Query(value = CustomeNativeQuery.GET_PRODUCT_FROM_CATEGORY_NAME, nativeQuery = true)
    List<Product> getProductsByCategoryNameNative(@Param("categoryName") String categoryName);
}

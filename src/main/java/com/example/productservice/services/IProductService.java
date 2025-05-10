package com.example.productservice.services;

import com.example.productservice.exceptions.ProductNotFoundException;
import com.example.productservice.models.Product;

import java.util.List;

public interface IProductService {
    Product getProductById(long id) throws ProductNotFoundException;
    List<Product> getAllProducts();
    Product createProduct(String name, double price, String description, String imageUrl, String category);
    Product replaceProduct(long id, String name, double price, String description, String imageUrl, String category) throws ProductNotFoundException;
}

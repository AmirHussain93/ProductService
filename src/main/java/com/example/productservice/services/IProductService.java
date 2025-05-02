package com.example.productservice.services;

import com.example.productservice.models.Product;

public interface IProductService {
    Product getProductById(long id);
}

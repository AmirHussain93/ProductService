package com.example.productservice.services;

import com.example.productservice.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

public interface SearchService {

    Page<Product> search(String query, int pageNumber, int pageSize, String sortBy);
}

package com.example.productservice.controllers;

import com.example.productservice.dtos.CreateFakeStoreRequestDto;
import com.example.productservice.dtos.ProductResponseDto;
import com.example.productservice.exceptions.ProductNotFoundException;
import com.example.productservice.models.Product;
import com.example.productservice.services.IProductService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {

    IProductService productService;

    public ProductController(
           @Qualifier("productDbService") IProductService productService
    ) {
        this.productService = productService;
    }

    @GetMapping("/products/{id}")
    public ProductResponseDto getProductById(@PathVariable("id") long id) throws ProductNotFoundException {

        Product product = productService.getProductById(id);

        return ProductResponseDto.from(product);
    }

    @GetMapping("/products")
    public List<ProductResponseDto> getAllProducts() {
        List<Product> products = productService.getAllProducts();

        List<ProductResponseDto> productResponseDtos = new ArrayList<>();

        for (Product product : products) {
            ProductResponseDto productResponseDto = ProductResponseDto.from(product);
            productResponseDtos.add(productResponseDto);
        }
        return  productResponseDtos;
    }

    @PostMapping("/products")
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody CreateFakeStoreRequestDto createFakeStoreRequestDto) {

        Product product =  productService.createProduct(
                createFakeStoreRequestDto.getName(),
                createFakeStoreRequestDto.getPrice(),
                createFakeStoreRequestDto.getDescription(),
                createFakeStoreRequestDto.getImageUrl(),
                createFakeStoreRequestDto.getCategory()
        );

        return new ResponseEntity<>(ProductResponseDto.from(product), HttpStatus.CREATED);
    }

    @PutMapping("/products/{id}")
    public ProductResponseDto replaceProduct(@PathVariable("id") long id, @RequestBody CreateFakeStoreRequestDto createFakeStoreRequestDto) throws ProductNotFoundException {
        Product product =  productService.replaceProduct(
                id,
                createFakeStoreRequestDto.getName(),
                createFakeStoreRequestDto.getPrice(),
                createFakeStoreRequestDto.getDescription(),
                createFakeStoreRequestDto.getImageUrl(),
                createFakeStoreRequestDto.getCategory()
        );
        return ProductResponseDto.from(product);
    }

}

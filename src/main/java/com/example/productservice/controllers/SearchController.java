package com.example.productservice.controllers;

import com.example.productservice.dtos.ProductResponseDto;
import com.example.productservice.dtos.SearchRequestDto;
import com.example.productservice.models.Product;
import com.example.productservice.services.SearchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping("/search")
    public Page<ProductResponseDto> Search(@RequestBody SearchRequestDto searchRequestDto) {

        Page<Product> productPage = searchService.search(
                searchRequestDto.getQuery(),
                searchRequestDto.getPageNumber(),
                searchRequestDto.getPageSize(),
                searchRequestDto.getSortBy()
        );

        return getProductResponseDtoPageFromProductPage(productPage);
    }

    @GetMapping("/search")
    public Page<ProductResponseDto> Search(
            @RequestParam String query,
            @RequestParam int pageNumber,
            @RequestParam int pageSize,
            @RequestParam String sortBy
    ) {

        Page<Product> productPage = searchService.search(query, pageNumber, pageSize, sortBy);

        return getProductResponseDtoPageFromProductPage(productPage);
    }

    private Page<ProductResponseDto> getProductResponseDtoPageFromProductPage(Page<Product> productPage) {
        List<ProductResponseDto> productResponseDtoList = new ArrayList<>();
        List<Product> products = productPage.getContent();

        for (Product product : products) {
            ProductResponseDto productResponseDto = ProductResponseDto.from(product);
            productResponseDtoList.add(productResponseDto);
        }

        return new PageImpl<>(productResponseDtoList,  productPage.getPageable(), productPage.getTotalElements());
    }
}

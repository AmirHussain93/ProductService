package com.example.productservice.services;

import com.example.productservice.dtos.FakeStoreProductResponseDto;
import com.example.productservice.dtos.FakeStoreRequestDto;
import com.example.productservice.exceptions.ProductNotFoundException;
import com.example.productservice.models.Product;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service("fakeStoreProductService")
public class FakeStoreProductService implements IProductService {

    RestTemplate restTemplate;
    RedisTemplate<String, Object> redisTemplate;

    public FakeStoreProductService(
            RestTemplate restTemplate,
            RedisTemplate<String, Object> redisTemplate
    ) {
        this.restTemplate = restTemplate;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Product getProductById(long id) throws ProductNotFoundException {

        Product productFromRedis = (Product) redisTemplate.opsForValue().get(String.valueOf(id));

        if (productFromRedis != null) {
            return productFromRedis;
        }

        FakeStoreProductResponseDto fakeStoreProductResponseDto = restTemplate.getForObject(
                "https://fakestoreapi.com/products/" + id,
                FakeStoreProductResponseDto.class);

        if (fakeStoreProductResponseDto == null) {
            throw new ProductNotFoundException("Product with id:" + id + " not found");
        }

        Product productFromFakeStore = fakeStoreProductResponseDto.toProduct();
        redisTemplate.opsForValue().set(String.valueOf(id), productFromFakeStore);
        return productFromFakeStore;
    }

    @Override
    public List<Product> getAllProducts() {

        FakeStoreProductResponseDto[] fakeStoreProductResponseDtos = restTemplate.getForObject(
                "https://fakestoreapi.com/products",
                FakeStoreProductResponseDto[].class);

        List<Product> products = new ArrayList<>();

        for (FakeStoreProductResponseDto fakeStoreProductResponseDto : fakeStoreProductResponseDtos) {
            Product product = fakeStoreProductResponseDto.toProduct();
            products.add(product);
        }

        return products;
    }

    @Override
    public Product createProduct(String name, double price, String description, String imageUrl, String category) {

        FakeStoreRequestDto fakeStoreRequestDto = createDtoFromParams(name, price, description, imageUrl, category);

        FakeStoreProductResponseDto fakeStoreProductResponseDto = restTemplate.postForObject(
                "https://fakestoreapi.com/products",
                fakeStoreRequestDto,
                FakeStoreProductResponseDto.class
        );

        return fakeStoreProductResponseDto.toProduct();
    }

    @Override
    public Product replaceProduct(long id, String name, double price, String description, String imageUrl, String category) throws ProductNotFoundException {

        FakeStoreRequestDto updateFakeStoreDto = createDtoFromParams(name, price, description, imageUrl, category);

       // We will not be using put as this returns void
//        restTemplate.put(
//                "https://fakestoreapi.com/products/" + id,
//                fakeStoreRequestDto,
//                FakeStoreProductResponseDto.class
//        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<FakeStoreRequestDto> requestEntity = new HttpEntity<>(updateFakeStoreDto, headers);

        ResponseEntity<FakeStoreProductResponseDto> responseEntity = restTemplate.exchange(
                "https://fakestoreapi.com/products/" + id,
                HttpMethod.PUT,
                requestEntity,
                FakeStoreProductResponseDto.class
        );


        return responseEntity.getBody().toProduct();
    }

    @Override
    public List<Product> getProductsByCategory(String categoryName) {
        return List.of();
    }

    private FakeStoreRequestDto createDtoFromParams(String name, double price, String description, String imageUrl, String category) {
        FakeStoreRequestDto fakeStoreRequestDto = new FakeStoreRequestDto();
        fakeStoreRequestDto.setTitle(name);
        fakeStoreRequestDto.setPrice(price);
        fakeStoreRequestDto.setDescription(description);
        fakeStoreRequestDto.setImage(imageUrl);
        fakeStoreRequestDto.setCategory(category);

        return fakeStoreRequestDto;
    }
}

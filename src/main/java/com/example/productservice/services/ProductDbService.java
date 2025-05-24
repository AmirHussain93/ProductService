package com.example.productservice.services;

import com.example.productservice.exceptions.CategoryNotFoundException;
import com.example.productservice.exceptions.ProductNotFoundException;
import com.example.productservice.models.Category;
import com.example.productservice.models.Product;
import com.example.productservice.repositories.CategoryRepository;
import com.example.productservice.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("productDbService")
public class ProductDbService implements IProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public ProductDbService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Product getProductById(long id) throws ProductNotFoundException {

       Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isEmpty()) {
            throw new ProductNotFoundException("Product with id " + id + " not found");
        }

        return productOptional.get();
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product createProduct(String name, double price, String description, String imageUrl, String category) {

        Product product = new Product();
        buildProduct(product, name, price, description, imageUrl, category);

        return productRepository.save(product);
    }

    @Override
    public Product replaceProduct(long id, String name, double price, String description, String imageUrl, String category) throws ProductNotFoundException {

        Product product = new Product();
        product.setId(id);
        buildProduct(product, name, price, description, imageUrl, category);

        return productRepository.save(product);
    }

    @Override
    public List<Product> getProductsByCategory(String categoryName) throws CategoryNotFoundException{

        Optional<Category> categoryOptional = categoryRepository.findByName(categoryName);

        if(categoryOptional.isEmpty()) {
            throw new CategoryNotFoundException("Category with name " + categoryName + " not found");
        }

//        return productRepository.findByCategory_Name(categoryOptional.get().getName());
//        return productRepository.getProductByCategoryName(categoryOptional.get().getName());
          return productRepository.getProductsByCategoryNameNative(categoryOptional.get().getName());
    }

    private void buildProduct(Product product, String name, double price, String description, String imageUrl, String category) {
        product.setName(name);
        product.setPrice(price);
        product.setDescription(description);
        product.setImageUrl(imageUrl);

        Category categoryObj = getCategoryFromDb(category);
        categoryObj.setName(category);
        product.setCategory(categoryObj);
    }

    private Category getCategoryFromDb(String categoryName) {
        Optional<Category> categoryOptional = categoryRepository.findByName(categoryName);

        if (categoryOptional.isPresent()) {
                return  categoryOptional.get();
        }

        Category newCategory = new Category();
        newCategory.setName(categoryName);
        return categoryRepository.save(newCategory);
    }
}

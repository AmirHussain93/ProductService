package com.example.productservice.repositories;

public class CustomeNativeQuery {
    public static final String GET_PRODUCT_FROM_CATEGORY_NAME
            = "select * from product where category_id in (select id from category where name = :categoryName)";
}

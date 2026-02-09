package com.diamon.manufacturinginventory.Mapper;

import com.diamon.manufacturinginventory.DTOs.Product.ProductRequest;
import com.diamon.manufacturinginventory.Entity.Product;

public class ProductMapper {

    public static Product toEntity(ProductRequest request) {
        return new Product(null, request.code(), request.name(), request.price());
    }
}

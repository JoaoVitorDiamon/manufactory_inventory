package com.diamon.manufacturinginventory.DTOs.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank(message = "The product code must not be blank")
        String code,
        @NotBlank(message = "The product name must not be blank")
        String name,
        @Positive(message = "Please enter a valid value for the product price")
        BigDecimal price
) {
}

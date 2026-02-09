package com.diamon.manufacturinginventory.DTOs.ProductRecipe;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record ProductProductionSuggestion(
        @NotBlank(message = "Product name must not be blank") String productName,
        @Positive(message = "Maximum quantity must be positive") int maxQuantity,
        @Positive(message = "Product value must be positive") double productValue,
        @Positive(message = "Total value must be positive") double totalValue
) {}

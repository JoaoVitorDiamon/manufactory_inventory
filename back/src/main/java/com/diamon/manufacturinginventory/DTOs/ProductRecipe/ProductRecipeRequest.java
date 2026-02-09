package com.diamon.manufacturinginventory.DTOs.ProductRecipe;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

public record ProductRecipeRequest(
    String productId,
    @NotNull(message = "Raw material ID must not be null") String rawMaterialId,
    @Positive(message = "Quantity must be positive") int quantity
) {
}


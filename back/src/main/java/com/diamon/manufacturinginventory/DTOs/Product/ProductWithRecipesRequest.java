package com.diamon.manufacturinginventory.DTOs.Product;

import com.diamon.manufacturinginventory.DTOs.ProductRecipe.ProductRecipeRequest;
import jakarta.validation.Valid;
import java.util.List;

public record ProductWithRecipesRequest(
    @Valid ProductRequest productRequest,
    @Valid List<ProductRecipeRequest> recipeRequests
) {}

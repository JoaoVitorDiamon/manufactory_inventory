package com.diamon.manufacturinginventory.DTOs.ProductRecipe;

import java.util.List;

public record ProductRecipeResponse(
        String recipeId,
        String productName,
        List<RawMaterialInfo> rawMaterials
) {
    public static record RawMaterialInfo(
        String rawMaterialName,
        int requiredQuantity
    ) {}
}


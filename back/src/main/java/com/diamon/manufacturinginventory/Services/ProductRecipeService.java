package com.diamon.manufacturinginventory.Services;

import com.diamon.manufacturinginventory.DTOs.ProductRecipe.ProductProductionSuggestion;
import com.diamon.manufacturinginventory.DTOs.ProductRecipe.ProductRecipeRequest;
import com.diamon.manufacturinginventory.DTOs.ProductRecipe.ProductRecipeResponse;
import com.diamon.manufacturinginventory.Entity.ProductRecipes;
import com.diamon.manufacturinginventory.Entity.Product;
import com.diamon.manufacturinginventory.Entity.RawMaterials;
import com.diamon.manufacturinginventory.Exceptions.EntityNotFoundException;
import com.diamon.manufacturinginventory.Repository.ProductRecipeRepository;
import com.diamon.manufacturinginventory.Repository.ProductRepository;
import com.diamon.manufacturinginventory.Repository.RawMaterialsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductRecipeService {

    private final ProductRecipeRepository productRecipeRepository;
    private final ProductRepository productRepository;
    private final RawMaterialsRepository rawMaterialsRepository;

    public ProductRecipeService(ProductRecipeRepository productRecipeRepository,
                                ProductRepository productRepository,
                                RawMaterialsRepository rawMaterialsRepository) {
        this.productRecipeRepository = productRecipeRepository;
        this.productRepository = productRepository;
        this.rawMaterialsRepository = rawMaterialsRepository;
    }

    public ProductRecipes createRecipe(ProductRecipeRequest request) {
        Product product = productRepository.findById(UUID.fromString(request.productId()))
            .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        RawMaterials rawMaterial = rawMaterialsRepository.findById(UUID.fromString(request.rawMaterialId()))
            .orElseThrow(() -> new EntityNotFoundException("Raw material not found"));
        ProductRecipes recipe = new ProductRecipes();
        recipe.setProduct(product);
        recipe.setRawProduct(rawMaterial);
        recipe.setRequiredQuantity(request.quantity());
        return productRecipeRepository.save(recipe);
    }

    public ProductRecipes getRecipeById(UUID id) {
        return productRecipeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Recipe not found"));
    }

    public java.util.List<ProductRecipes> getAllRecipes() {
        return productRecipeRepository.findAll();
    }

    public ProductRecipes updateRecipe(UUID id, ProductRecipeRequest request) {
        ProductRecipes recipe = productRecipeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Recipe not found"));
        Product product = productRepository.findById(UUID.fromString(request.productId()))
            .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        RawMaterials rawMaterial = rawMaterialsRepository.findById(UUID.fromString(request.rawMaterialId()))
            .orElseThrow(() -> new EntityNotFoundException("Raw material not found"));
        recipe.setProduct(product);
        recipe.setRawProduct(rawMaterial);
        recipe.setRequiredQuantity(request.quantity());
        return productRecipeRepository.save(recipe);
    }

    public List<ProductRecipeResponse> getAllRecipeDetails() {
        return productRecipeRepository.findAll().stream()
            .map(recipe -> new ProductRecipeResponse(
                recipe.getId().toString(), // Corrigido: UUID para String
                recipe.getProduct().getName(),
                List.of(
                    new ProductRecipeResponse.RawMaterialInfo(
                        recipe.getRawProduct().getName(),
                        recipe.getRequiredQuantity()
                    )
                )
            ))
            .toList();
    }


    public List<ProductProductionSuggestion> getProductionSuggestions() {
        List<Product> products = productRepository.findAll();
        List<ProductProductionSuggestion> suggestions = new java.util.ArrayList<>();
        for (Product product : products) {
            List<ProductRecipes> recipes = productRecipeRepository.findAll().stream()
                .filter(r -> r.getProduct().getId().equals(product.getId()))
                .toList();
            if (recipes.isEmpty()) continue;
            int maxQuantity = Integer.MAX_VALUE;
            for (ProductRecipes recipe : recipes) {
                RawMaterials raw = recipe.getRawProduct();
                int available = raw.getStockQuantity() / recipe.getRequiredQuantity();
                if (available < maxQuantity) maxQuantity = available;
            }
            if (maxQuantity > 0) {
                double totalValue = maxQuantity * product.getPrice().doubleValue();
                suggestions.add(new ProductProductionSuggestion(
                    product.getName(),
                    maxQuantity,
                    product.getPrice().doubleValue(),
                    totalValue
                ));
            }
        }
        suggestions.sort((a, b) -> Double.compare(b.productValue(), a.productValue()));
        return suggestions;
    }
}

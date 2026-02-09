package com.diamon.manufacturinginventory.Services;

import com.diamon.manufacturinginventory.DTOs.Product.ProductRequest;
import com.diamon.manufacturinginventory.DTOs.ProductRecipe.ProductRecipeRequest;
import com.diamon.manufacturinginventory.Entity.Product;
import com.diamon.manufacturinginventory.Entity.ProductRecipes;
import com.diamon.manufacturinginventory.Entity.RawMaterials;
import com.diamon.manufacturinginventory.Exceptions.EntityNotFoundException;
import com.diamon.manufacturinginventory.Exceptions.InsufficientQuantity;
import com.diamon.manufacturinginventory.Mapper.ProductMapper;
import com.diamon.manufacturinginventory.Repository.ProductRecipeRepository;
import com.diamon.manufacturinginventory.Repository.ProductRepository;
import com.diamon.manufacturinginventory.Repository.RawMaterialsRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {


    private final ProductRepository productRepository;
    private final RawMaterialsRepository rawMaterialsRepository;
    private final ProductRecipeRepository productRecipeRepository;

    public ProductService(
            ProductRepository productRepository,
            RawMaterialsRepository rawMaterialsRepository,
            ProductRecipeRepository productRecipeRepository
    ) {
        this.productRepository = productRepository;
        this.rawMaterialsRepository = rawMaterialsRepository;
        this.productRecipeRepository = productRecipeRepository;
    }

    public Product findById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    public Product save(ProductRequest product) {
        Product entity = ProductMapper.toEntity(product);
        return productRepository.save(entity);
    }

    public void deleteById(UUID id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        productRepository.deleteById(id);
    }

    public void update(UUID id, ProductRequest product) {
        Product existingProduct = productRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        existingProduct.setCode(product.code());
        existingProduct.setName(product.name());
        existingProduct.setPrice(product.price());

        productRepository.save(existingProduct);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }
    @Transactional
    public Product createProductWithRecipe(ProductRequest productRequest, List<ProductRecipeRequest> recipeRequests) {
        Product product = ProductMapper.toEntity(productRequest);
        product = productRepository.save(product);

        for (ProductRecipeRequest recipeRequest : recipeRequests) {
            RawMaterials rawMaterial = rawMaterialsRepository.findById(UUID.fromString(recipeRequest.rawMaterialId()))
                .orElseThrow(() -> new EntityNotFoundException("Raw material not found"));
            if (rawMaterial.getStockQuantity() < recipeRequest.quantity()) {
                throw new InsufficientQuantity("Insufficient stock for raw material: " + rawMaterial.getName());
            }
            rawMaterial.setStockQuantity(rawMaterial.getStockQuantity() - recipeRequest.quantity());
            rawMaterialsRepository.save(rawMaterial);

            ProductRecipes recipe = new ProductRecipes();
            Product recipeProduct = product;
            String prodIdStr = recipeRequest.productId();
            if (prodIdStr != null && !prodIdStr.isBlank()) {
                try {
                    UUID recipeProductId = UUID.fromString(prodIdStr);
                    recipeProduct = productRepository.findById(recipeProductId)
                        .orElseThrow(() -> new EntityNotFoundException("Product not found for recipe"));
                } catch (Exception e) {
                    throw new EntityNotFoundException("Invalid product ID format");
                }
            }
            recipe.setProduct(recipeProduct);
            recipe.setRawProduct(rawMaterial);
            recipe.setRequiredQuantity(recipeRequest.quantity());
            productRecipeRepository.save(recipe);
        }

        return product;
    }
}

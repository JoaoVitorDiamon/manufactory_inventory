package com.diamon.manufacturinginventory.Services;

import com.diamon.manufacturinginventory.DTOs.ProductRecipe.ProductRecipeRequest;
import com.diamon.manufacturinginventory.DTOs.ProductRecipe.ProductRecipeResponse;
import com.diamon.manufacturinginventory.Entity.ProductRecipes;
import com.diamon.manufacturinginventory.Entity.Product;
import com.diamon.manufacturinginventory.Entity.RawMaterials;
import com.diamon.manufacturinginventory.Exceptions.EntityNotFoundException;
import com.diamon.manufacturinginventory.Repository.ProductRecipeRepository;
import com.diamon.manufacturinginventory.Repository.ProductRepository;
import com.diamon.manufacturinginventory.Repository.RawMaterialsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductRecipeServiceTest {

    @Mock
    private ProductRecipeRepository productRecipeRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private RawMaterialsRepository rawMaterialsRepository;

    @InjectMocks
    private ProductRecipeService productRecipeService;

    private ProductRecipes recipe;
    private Product product;
    private RawMaterials rawMaterial;
    private UUID recipeId;
    private UUID productId;
    private UUID rawMaterialId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        recipeId = UUID.randomUUID();
        productId = UUID.randomUUID();
        rawMaterialId = UUID.randomUUID();
        product = mock(Product.class);
        when(product.getName()).thenReturn("Test Product");
        rawMaterial = mock(RawMaterials.class);
        when(rawMaterial.getName()).thenReturn("Test Raw Material");
        recipe = new ProductRecipes();
        recipe.setProduct(product);
        recipe.setRawProduct(rawMaterial);
        recipe.setRequiredQuantity(5);
        // Use reflection or setter for id if needed
    }

    @Test
    void createRecipe_shouldSaveAndReturnRecipe() {
        ProductRecipeRequest request = new ProductRecipeRequest(productId, rawMaterialId, 5);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(rawMaterialsRepository.findById(rawMaterialId)).thenReturn(Optional.of(rawMaterial));
        when(productRecipeRepository.save(any(ProductRecipes.class))).thenReturn(recipe);
        ProductRecipes saved = productRecipeService.createRecipe(request);
        assertNotNull(saved);
        assertEquals(product, saved.getProduct());
        assertEquals(rawMaterial, saved.getRawProduct());
        assertEquals(5, saved.getRequiredQuantity());
    }

    @Test
    void createRecipe_shouldThrowExceptionWhenProductNotFound() {
        ProductRecipeRequest request = new ProductRecipeRequest(productId, rawMaterialId, 5);
        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> productRecipeService.createRecipe(request));
    }

    @Test
    void createRecipe_shouldThrowExceptionWhenRawMaterialNotFound() {
        ProductRecipeRequest request = new ProductRecipeRequest(productId, rawMaterialId, 5);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(rawMaterialsRepository.findById(rawMaterialId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> productRecipeService.createRecipe(request));
    }

    @Test
    void getRecipeById_shouldReturnRecipeWhenFound() {
        when(productRecipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
        ProductRecipes found = productRecipeService.getRecipeById(recipeId);
        assertEquals(recipe, found);
    }

    @Test
    void getRecipeById_shouldThrowExceptionWhenNotFound() {
        when(productRecipeRepository.findById(recipeId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> productRecipeService.getRecipeById(recipeId));
    }

    @Test
    void getAllRecipes_shouldReturnRecipeList() {
        when(productRecipeRepository.findAll()).thenReturn(List.of(recipe));
        List<ProductRecipes> recipes = productRecipeService.getAllRecipes();
        assertNotNull(recipes);
        assertFalse(recipes.isEmpty());
    }

    @Test
    void updateRecipe_shouldUpdateAndReturnRecipe() {
        ProductRecipeRequest request = new ProductRecipeRequest(productId, rawMaterialId, 10);
        when(productRecipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(rawMaterialsRepository.findById(rawMaterialId)).thenReturn(Optional.of(rawMaterial));
        when(productRecipeRepository.save(any(ProductRecipes.class))).thenReturn(recipe);
        ProductRecipes updated = productRecipeService.updateRecipe(recipeId, request);
        assertNotNull(updated);
        assertEquals(product, updated.getProduct());
        assertEquals(rawMaterial, updated.getRawProduct());
        assertEquals(10, updated.getRequiredQuantity());
    }

    @Test
    void updateRecipe_shouldThrowExceptionWhenRecipeNotFound() {
        ProductRecipeRequest request = new ProductRecipeRequest(productId, rawMaterialId, 10);
        when(productRecipeRepository.findById(recipeId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> productRecipeService.updateRecipe(recipeId, request));
    }

    @Test
    void getAllRecipeDetails_shouldReturnRecipeDetails() {
        when(productRecipeRepository.findAll()).thenReturn(List.of(recipe));
        List<ProductRecipeResponse> details = productRecipeService.getAllRecipeDetails();
        assertNotNull(details);
        assertEquals(1, details.size());
        ProductRecipeResponse.RawMaterialInfo info = details.get(0).rawMaterials().get(0);
        assertEquals("Test Raw Material", info.rawMaterialName());
        assertEquals(5, info.requiredQuantity());
    }
}


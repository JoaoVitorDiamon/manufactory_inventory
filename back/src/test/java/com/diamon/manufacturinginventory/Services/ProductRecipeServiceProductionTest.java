package com.diamon.manufacturinginventory.Services;

import com.diamon.manufacturinginventory.DTOs.ProductRecipe.ProductProductionSuggestion;
import com.diamon.manufacturinginventory.Entity.Product;
import com.diamon.manufacturinginventory.Entity.ProductRecipes;
import com.diamon.manufacturinginventory.Repository.ProductRecipeRepository;
import com.diamon.manufacturinginventory.Repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductRecipeServiceProductionTest {

    @Mock
    private ProductRecipeRepository productRecipeRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductRecipeService productRecipeService;

    private Product product;
    private ProductRecipes recipe;
    private UUID productId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productId = UUID.randomUUID();
        product = mock(Product.class);
        when(product.getId()).thenReturn(productId);
        when(product.getName()).thenReturn("Test Product");
        when(product.getPrice()).thenReturn(new BigDecimal("100.00"));
        recipe = mock(ProductRecipes.class);
        when(recipe.getProduct()).thenReturn(product);
        when(recipe.getRequiredQuantity()).thenReturn(2);
        when(recipe.getRawProduct()).thenReturn(mock(com.diamon.manufacturinginventory.Entity.RawMaterials.class));
        when(recipe.getRawProduct().getStockQuantity()).thenReturn(10);
    }

    @Test
    void getProductionSuggestions_shouldReturnCorrectSuggestions() {
        when(productRepository.findAll()).thenReturn(List.of(product));
        when(productRecipeRepository.findAll()).thenReturn(List.of(recipe));
        List<ProductProductionSuggestion> suggestions = productRecipeService.getProductionSuggestions();
        assertNotNull(suggestions);
        assertEquals(1, suggestions.size());
        ProductProductionSuggestion suggestion = suggestions.get(0);
        assertEquals("Test Product", suggestion.productName());
        assertEquals(5, suggestion.maxQuantity());
        assertEquals(100.00, suggestion.productValue());
        assertEquals(500.00, suggestion.totalValue());
    }

    @Test
    void getProductionSuggestions_shouldReturnEmptyWhenNoRecipes() {
        when(productRepository.findAll()).thenReturn(List.of(product));
        when(productRecipeRepository.findAll()).thenReturn(List.of());
        List<ProductProductionSuggestion> suggestions = productRecipeService.getProductionSuggestions();
        assertTrue(suggestions.isEmpty());
    }
}


package com.diamon.manufacturinginventory.Services;

import com.diamon.manufacturinginventory.DTOs.Product.ProductRequest;
import com.diamon.manufacturinginventory.Entity.Product;
import com.diamon.manufacturinginventory.Entity.ProductRecipes;
import com.diamon.manufacturinginventory.Entity.RawMaterials;
import com.diamon.manufacturinginventory.Exceptions.EntityNotFoundException;
import com.diamon.manufacturinginventory.Repository.ProductRepository;
import com.diamon.manufacturinginventory.Repository.ProductRecipeRepository;
import com.diamon.manufacturinginventory.Repository.RawMaterialsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RawMaterialsRepository rawMaterialsRepository;

    @Mock
    private ProductRecipeRepository productRecipeRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private UUID id;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();
        product = new Product(id, "CODE1", "Test Product", new BigDecimal("10.00"));
    }

    @Test
    void findById_shouldReturnProductWhenFound() {
        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        Product found = productService.findById(id);
        assertEquals(product, found);
    }

    @Test
    void findById_shouldThrowExceptionWhenNotFound() {
        when(productRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> productService.findById(id));
    }

    @Test
    void save_shouldSaveAndReturnProduct() {
        ProductRequest request = new ProductRequest("CODE1", "Test Product", "desc", new BigDecimal("10.00"));
        Product entity = new Product(null, "CODE1", "Test Product", new BigDecimal("10.00"));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        Product saved = productService.save(request);
        assertNotNull(saved);
        assertEquals(product.getCode(), saved.getCode());
    }

    @Test
    void deleteById_shouldDeleteWhenFound() {
        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        doNothing().when(productRepository).deleteById(id);
        assertDoesNotThrow(() -> productService.deleteById(id));
        verify(productRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteById_shouldThrowExceptionWhenNotFound() {
        when(productRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> productService.deleteById(id));
    }

    @Test
    void update_shouldUpdateProductWhenFound() {
        ProductRequest request = new ProductRequest("CODE2", "Updated Product", "desc", new BigDecimal("20.00"));
        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        assertDoesNotThrow(() -> productService.update(id, request));
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void update_shouldThrowExceptionWhenNotFound() {
        ProductRequest request = new ProductRequest("CODE2", "Updated Product", "desc", new BigDecimal("20.00"));
        when(productRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> productService.update(id, request));
    }

    @Test
    void findAll_shouldReturnProductList() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));
        List<Product> products = productService.findAll();
        assertNotNull(products);
        assertFalse(products.isEmpty());
    }

    @Test
    void createProductWithRecipe_shouldCreateProductAndUpdateStock() {
        ProductRequest productRequest = new ProductRequest("CODE1", "Test Product", "desc", new BigDecimal("10.00"));
        UUID rawMaterialId = UUID.randomUUID();
        com.diamon.manufacturinginventory.DTOs.ProductRecipe.ProductRecipeRequest recipeRequest = new com.diamon.manufacturinginventory.DTOs.ProductRecipe.ProductRecipeRequest(id, rawMaterialId, 5);
        RawMaterials rawMaterial = mock(RawMaterials.class);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(rawMaterialsRepository.findById(rawMaterialId)).thenReturn(Optional.of(rawMaterial));
        when(rawMaterial.getStockQuantity()).thenReturn(10);
        when(rawMaterial.getName()).thenReturn("Test Raw Material");
        when(productRecipeRepository.save(any(ProductRecipes.class))).thenReturn(new ProductRecipes());

        Product result = productService.createProductWithRecipe(productRequest, List.of(recipeRequest));
        assertNotNull(result);
        verify(rawMaterial).setStockQuantity(5);
        verify(rawMaterialsRepository).save(rawMaterial);
        verify(productRecipeRepository).save(any(ProductRecipes.class));
    }

    @Test
    void createProductWithRecipe_shouldThrowInsufficientQuantity() {
        ProductRequest productRequest = new ProductRequest("CODE1", "Test Product", "desc", new BigDecimal("10.00"));
        UUID rawMaterialId = UUID.randomUUID();
        com.diamon.manufacturinginventory.DTOs.ProductRecipe.ProductRecipeRequest recipeRequest = new com.diamon.manufacturinginventory.DTOs.ProductRecipe.ProductRecipeRequest(id, rawMaterialId, 15);
        RawMaterials rawMaterial = mock(RawMaterials.class);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(rawMaterialsRepository.findById(rawMaterialId)).thenReturn(Optional.of(rawMaterial));
        when(rawMaterial.getStockQuantity()).thenReturn(10);
        when(rawMaterial.getName()).thenReturn("Test Raw Material");

        assertThrows(com.diamon.manufacturinginventory.Exceptions.InsufficientQuantity.class, () ->
            productService.createProductWithRecipe(productRequest, List.of(recipeRequest)));
    }

    @Test
    void createProductWithRecipe_shouldThrowEntityNotFoundException() {
        ProductRequest productRequest = new ProductRequest("CODE1", "Test Product", "desc", new BigDecimal("10.00"));
        UUID rawMaterialId = UUID.randomUUID();
        com.diamon.manufacturinginventory.DTOs.ProductRecipe.ProductRecipeRequest recipeRequest = new com.diamon.manufacturinginventory.DTOs.ProductRecipe.ProductRecipeRequest(id, rawMaterialId, 5);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(rawMaterialsRepository.findById(rawMaterialId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
            productService.createProductWithRecipe(productRequest, List.of(recipeRequest)));
    }
}

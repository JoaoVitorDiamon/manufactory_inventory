package com.diamon.manufacturinginventory.Controller;

import com.diamon.manufacturinginventory.DTOs.ProductRecipe.ProductProductionSuggestion;
import com.diamon.manufacturinginventory.DTOs.ProductRecipe.ProductRecipeRequest;
import com.diamon.manufacturinginventory.DTOs.ProductRecipe.ProductRecipeResponse;
import com.diamon.manufacturinginventory.Entity.ProductRecipes;
import com.diamon.manufacturinginventory.Services.ProductRecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/product-recipes")
@Tag(name = "Receitas de Produto", description = "Operações de gerenciamento de receitas de produto e sugestões de produção")
public class ProductRecipeController {

    private final ProductRecipeService productRecipeService;

    public ProductRecipeController(ProductRecipeService productRecipeService) {
        this.productRecipeService = productRecipeService;
    }


    @Operation(summary = "Criar receita de produto", description = "Cria uma nova receita para um produto.")
    @PostMapping
    public ResponseEntity<ProductRecipes> createRecipe(@Valid @RequestBody ProductRecipeRequest request) {
        ProductRecipes recipe = productRecipeService.createRecipe(request);
        return ResponseEntity.ok(recipe);
    }


    @Operation(summary = "Buscar receita por ID", description = "Retorna uma receita específica pelo ID.")
    @GetMapping("/{id}")
    public ResponseEntity<ProductRecipes> getRecipeById(@PathVariable UUID id) {
        ProductRecipes recipe = productRecipeService.getRecipeById(id);
        return ResponseEntity.ok(recipe);
    }


    @Operation(summary = "Listar todas as receitas", description = "Retorna todas as receitas de produtos cadastradas.")
    @GetMapping
    public ResponseEntity<List<ProductRecipes>> getAllRecipes() {
        List<ProductRecipes> recipes = productRecipeService.getAllRecipes();
        return ResponseEntity.ok(recipes);
    }


    @Operation(summary = "Atualizar receita de produto", description = "Atualiza uma receita de produto existente.")
    @PutMapping("/{id}")
    public ResponseEntity<ProductRecipes> updateRecipe(@PathVariable UUID id, @Valid @RequestBody ProductRecipeRequest request) {
        ProductRecipes updated = productRecipeService.updateRecipe(id, request);
        return ResponseEntity.ok(updated);
    }


    @Operation(summary = "Listar detalhes das receitas", description = "Retorna detalhes de todas as receitas de produtos.")
    @GetMapping("/details")
    public ResponseEntity<List<ProductRecipeResponse>> getAllRecipeDetails() {
        List<ProductRecipeResponse> details = productRecipeService.getAllRecipeDetails();
        return ResponseEntity.ok(details);
    }


    @Operation(summary = "Sugestões de produção", description = "Retorna sugestões de produção baseadas nas receitas e estoque atual.")
    @GetMapping("/production-suggestions")
    public ResponseEntity<List<ProductProductionSuggestion>> getProductionSuggestions() {
        List<ProductProductionSuggestion> suggestions = productRecipeService.getProductionSuggestions();
        return ResponseEntity.ok(suggestions);
    }
}

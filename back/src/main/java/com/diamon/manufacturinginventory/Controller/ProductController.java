package com.diamon.manufacturinginventory.Controller;


import com.diamon.manufacturinginventory.DTOs.Product.ProductRequest;
import com.diamon.manufacturinginventory.DTOs.Product.ProductWithRecipesRequest;
import com.diamon.manufacturinginventory.Entity.Product;
import com.diamon.manufacturinginventory.Services.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Produtos", description = "Operações de gerenciamento de produtos")
public class ProductController {


    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @Operation(summary = "Criar produto com receita", description = "Cria um novo produto e suas receitas associadas.")
    @PostMapping
    public ResponseEntity<Product> createProductWithRecipe(@Valid @RequestBody ProductWithRecipesRequest request) {
        Product createdProduct = productService.createProductWithRecipe(request.productRequest(), request.recipeRequests());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }


    @Operation(summary = "Listar todos os produtos", description = "Retorna todos os produtos cadastrados.")
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.findAll();
        return ResponseEntity.ok().body(products);
    }


    @Operation(summary = "Atualizar produto", description = "Atualiza os dados de um produto existente.")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable UUID id, @Valid @RequestBody ProductRequest productRequest) {
        productService.update(id, productRequest);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Deletar produto", description = "Remove um produto pelo ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        productService.deleteById(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }
}
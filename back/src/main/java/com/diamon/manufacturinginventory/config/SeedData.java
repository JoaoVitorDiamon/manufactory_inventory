package com.diamon.manufacturinginventory.config;

import com.diamon.manufacturinginventory.Entity.Product;
import com.diamon.manufacturinginventory.Entity.RawMaterials;
import com.diamon.manufacturinginventory.Entity.ProductRecipes;
import com.diamon.manufacturinginventory.Repository.ProductRepository;
import com.diamon.manufacturinginventory.Repository.RawMaterialsRepository;
import com.diamon.manufacturinginventory.Repository.ProductRecipeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class SeedData {
    @Bean
    CommandLineRunner initDatabase(ProductRepository productRepository,
                                   RawMaterialsRepository rawMaterialsRepository,
                                   ProductRecipeRepository productRecipeRepository) {
        return args -> {
            RawMaterials steel = new RawMaterials();
            steel.setCode("RM001");
            steel.setName("Steel");
            steel.setStockQuantity(1000);
            rawMaterialsRepository.save(steel);

            RawMaterials plastic = new RawMaterials();
            plastic.setCode("RM002");
            plastic.setName("Plastic");
            plastic.setStockQuantity(500);
            rawMaterialsRepository.save(plastic);

            RawMaterials aluminum = new RawMaterials();
            aluminum.setCode("RM003");
            aluminum.setName("Aluminum");
            aluminum.setStockQuantity(800);
            rawMaterialsRepository.save(aluminum);

            RawMaterials copper = new RawMaterials();
            copper.setCode("RM004");
            copper.setName("Copper");
            copper.setStockQuantity(300);
            rawMaterialsRepository.save(copper);

            RawMaterials glass = new RawMaterials();
            glass.setCode("RM005");
            glass.setName("Glass");
            glass.setStockQuantity(200);
            rawMaterialsRepository.save(glass);

            Product bike = new Product();
            bike.setCode("P001");
            bike.setName("Bike");
            bike.setPrice(BigDecimal.valueOf(300.00));
            productRepository.save(bike);

            Product bottle = new Product();
            bottle.setCode("P002");
            bottle.setName("Bottle");
            bottle.setPrice(BigDecimal.valueOf(10.00));
            productRepository.save(bottle);

            rawMaterialsRepository.flush();
            productRepository.flush();

            steel = rawMaterialsRepository.findById(steel.getId()).orElseThrow();
            plastic = rawMaterialsRepository.findById(plastic.getId()).orElseThrow();
            aluminum = rawMaterialsRepository.findById(aluminum.getId()).orElseThrow();
            copper = rawMaterialsRepository.findById(copper.getId()).orElseThrow();
            glass = rawMaterialsRepository.findById(glass.getId()).orElseThrow();
            bike = productRepository.findById(bike.getId()).orElseThrow();
            bottle = productRepository.findById(bottle.getId()).orElseThrow();

            ProductRecipes bikeRecipe = new ProductRecipes();
            bikeRecipe.setProduct(bike);
            bikeRecipe.setRawProduct(steel);
            bikeRecipe.setRequiredQuantity(50);
            productRecipeRepository.save(bikeRecipe);

            ProductRecipes bottleRecipe = new ProductRecipes();
            bottleRecipe.setProduct(bottle);
            bottleRecipe.setRawProduct(plastic);
            bottleRecipe.setRequiredQuantity(5);
            productRecipeRepository.save(bottleRecipe);
        };
    }
}

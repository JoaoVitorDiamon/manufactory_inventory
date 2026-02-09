package com.diamon.manufacturinginventory.Repository;

import com.diamon.manufacturinginventory.Entity.ProductRecipes;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ProductRecipeRepository extends JpaRepository<ProductRecipes, UUID> {
}


package com.diamon.manufacturinginventory.Controller;

import com.diamon.manufacturinginventory.DTOs.ProductRecipe.ProductRecipeRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductRecipeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetAllRecipesAndSuggestions() throws Exception {
        mockMvc.perform(get("/product-recipes"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/product-recipes/details"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/product-recipes/production-suggestions"))
                .andExpect(status().isOk());
    }
}

package com.diamon.manufacturinginventory.Controller;

import com.diamon.manufacturinginventory.DTOs.Product.ProductRequest;
import com.diamon.manufacturinginventory.DTOs.Product.ProductWithRecipesRequest;
import com.diamon.manufacturinginventory.Entity.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateAndGetProduct() throws Exception {
        ProductWithRecipesRequest request = new ProductWithRecipesRequest(
            new ProductRequest("Produto Teste", "CODE123", 10.0),
            java.util.Collections.emptyList()
        );

        ResultActions createResult = mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
        createResult.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    void shouldUpdateAndDeleteProduct() throws Exception {
        ProductWithRecipesRequest request = new ProductWithRecipesRequest(
            new ProductRequest("Produto Update", "CODEUPD", 20.0),
            java.util.Collections.emptyList()
        );
        String response = mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();
        Product created = objectMapper.readValue(response, Product.class);

        ProductRequest updateRequest = new ProductRequest("Produto Atualizado", "CODEUPD", 30.0);
        mockMvc.perform(put("/api/products/" + created.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/products/" + created.getId()))
                .andExpect(status().isNoContent());
    }
}

package com.diamon.manufacturinginventory.Controller;

import com.diamon.manufacturinginventory.DTOs.RawMaterials.RawMaterialsRequest;
import com.diamon.manufacturinginventory.Entity.RawMaterials;
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
public class RawMaterialsControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateAndGetRawMaterial() throws Exception {
        RawMaterialsRequest request = new RawMaterialsRequest("Matéria Prima Teste", "RM001", 100);
        String response = mockMvc.perform(post("/api/raw-materials")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        RawMaterials created = objectMapper.readValue(response, RawMaterials.class);

        mockMvc.perform(get("/api/raw-materials"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    void shouldUpdateAndDeleteRawMaterial() throws Exception {
        RawMaterialsRequest request = new RawMaterialsRequest("Matéria Prima Update", "RM002", 200);
        String response = mockMvc.perform(post("/api/raw-materials")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();
        RawMaterials created = objectMapper.readValue(response, RawMaterials.class);

        RawMaterialsRequest updateRequest = new RawMaterialsRequest("Matéria Prima Atualizada", "RM002", 300);
        mockMvc.perform(put("/api/raw-materials/" + created.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/raw-materials/" + created.getId()))
                .andExpect(status().isNoContent());
    }
}

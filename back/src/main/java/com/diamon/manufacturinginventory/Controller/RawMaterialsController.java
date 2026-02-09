package com.diamon.manufacturinginventory.Controller;

import com.diamon.manufacturinginventory.DTOs.RawMaterials.RawMaterialsRequest;
import com.diamon.manufacturinginventory.Entity.RawMaterials;
import com.diamon.manufacturinginventory.Services.RawMaterialsServices;
import jakarta.validation.Valid;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/raw-materials")
@Tag(name = "Matérias-primas", description = "Operações de gerenciamento de matérias-primas")
public class RawMaterialsController {

    private final RawMaterialsServices rawMaterialsServices;

    public RawMaterialsController(RawMaterialsServices rawMaterialsServices) {
        this.rawMaterialsServices = rawMaterialsServices;
    }



    @Operation(summary = "Listar todas as matérias-primas", description = "Retorna todas as matérias-primas cadastradas.")
    @GetMapping
    public ResponseEntity<List<RawMaterials>> getAllRawMaterials() {
        List<RawMaterials> materials = rawMaterialsServices.findAll();
        return ResponseEntity.ok(materials);
    }


    @Operation(summary = "Criar matéria-prima", description = "Cria uma nova matéria-prima.")
    @PostMapping
    public ResponseEntity<RawMaterials> createRawMaterial(@Valid @RequestBody RawMaterialsRequest rawMaterials) {
        RawMaterials createdRawMaterial = rawMaterialsServices.save(rawMaterials);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRawMaterial);
    }


    @Operation(summary = "Buscar matéria-prima por ID", description = "Retorna uma matéria-prima específica pelo ID.")
    @GetMapping("/{id}")
    public ResponseEntity<RawMaterials> getRawMaterialById(@PathVariable String id) {
        RawMaterials rawMaterial = rawMaterialsServices.findById(UUID.fromString(id));
        return ResponseEntity.ok().body(rawMaterial);
    }


    @Operation(summary = "Atualizar matéria-prima", description = "Atualiza os dados de uma matéria-prima existente.")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateRawMaterial(@PathVariable String id, @Valid RawMaterialsRequest rawMaterials) {
        rawMaterialsServices.update(UUID.fromString(id), rawMaterials);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Deletar matéria-prima", description = "Remove uma matéria-prima pelo ID.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRawMaterial(@PathVariable String id) {
        rawMaterialsServices.deleteById(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }
}

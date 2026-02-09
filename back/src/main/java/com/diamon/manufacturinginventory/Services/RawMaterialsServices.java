package com.diamon.manufacturinginventory.Services;


import com.diamon.manufacturinginventory.DTOs.RawMaterials.RawMaterialsRequest;
import com.diamon.manufacturinginventory.Entity.RawMaterials;
import com.diamon.manufacturinginventory.Exceptions.EntityNotFoundException;
import com.diamon.manufacturinginventory.Mapper.RawMaterialsMapper;
import com.diamon.manufacturinginventory.Repository.RawMaterialsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RawMaterialsServices {

    private final RawMaterialsRepository rawMaterialsRepository;

    public RawMaterialsServices(RawMaterialsRepository rawMaterialsRepository) {
        this.rawMaterialsRepository = rawMaterialsRepository;
    }

    public RawMaterials findById(UUID id) {
        return rawMaterialsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Raw material not found"));
    }

    public RawMaterials save(RawMaterialsRequest rawMaterial) {
        RawMaterials entity = RawMaterialsMapper.toEntity(rawMaterial);
        return rawMaterialsRepository.save(entity);
    }

    public void deleteById(UUID id) {
        RawMaterials rawMaterial = rawMaterialsRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Raw material not found"));
        rawMaterialsRepository.deleteById(id);
    }

    public void update(UUID id, RawMaterialsRequest rawMaterial) {
        RawMaterials existingRawMaterial = rawMaterialsRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Raw material not found"));

        existingRawMaterial.setName(rawMaterial.name());
        existingRawMaterial.setCode(rawMaterial.code());
        existingRawMaterial.setStockQuantity(rawMaterial.stockQuantity());

        rawMaterialsRepository.save(existingRawMaterial);
    }

    public List<RawMaterials> findAll () {
     return rawMaterialsRepository.findAll();
    }

}

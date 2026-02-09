package com.diamon.manufacturinginventory.Mapper;

import com.diamon.manufacturinginventory.DTOs.RawMaterials.RawMaterialsRequest;
import com.diamon.manufacturinginventory.Entity.RawMaterials;

import java.math.BigDecimal;

public class RawMaterialsMapper {

    public static RawMaterials toEntity(RawMaterialsRequest rawMaterialsRequest) {
        return new RawMaterials(null, rawMaterialsRequest.code(), rawMaterialsRequest.name(), rawMaterialsRequest.stockQuantity());
    }
}

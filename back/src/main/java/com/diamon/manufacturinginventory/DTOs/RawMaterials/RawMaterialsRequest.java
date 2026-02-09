package com.diamon.manufacturinginventory.DTOs.RawMaterials;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record RawMaterialsRequest(
    @NotBlank(message = "The code must not be blank!") String code,
    @NotBlank(message = "The name must not be blank!") String name,
    @PositiveOrZero(message = "The stock quantity must be zero or positive!") int stockQuantity
) {
}

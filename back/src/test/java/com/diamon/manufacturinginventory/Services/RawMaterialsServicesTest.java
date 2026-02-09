package com.diamon.manufacturinginventory.Services;

import com.diamon.manufacturinginventory.DTOs.RawMaterials.RawMaterialsRequest;
import com.diamon.manufacturinginventory.Entity.RawMaterials;
import com.diamon.manufacturinginventory.Exceptions.EntityNotFoundException;
import com.diamon.manufacturinginventory.Repository.RawMaterialsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RawMaterialsServicesTest {

    @Mock
    private RawMaterialsRepository rawMaterialsRepository;

    @InjectMocks
    private RawMaterialsServices rawMaterialsServices;

    private RawMaterials rawMaterial;
    private UUID id;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        id = UUID.randomUUID();
        rawMaterial = new RawMaterials(null, "CODE1", "Test Raw Material", 10);
    }

    @Test
    void findById_shouldReturnRawMaterialWhenFound() {
        when(rawMaterialsRepository.findById(id)).thenReturn(Optional.of(rawMaterial));
        RawMaterials found = rawMaterialsServices.findById(id);
        assertEquals(rawMaterial, found);
    }

    @Test
    void findById_shouldThrowExceptionWhenNotFound() {
        when(rawMaterialsRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> rawMaterialsServices.findById(id));
    }

    @Test
    void save_shouldSaveAndReturnRawMaterial() {
        RawMaterialsRequest request = new RawMaterialsRequest("CODE1", "Test Raw Material", 100);
        when(rawMaterialsRepository.save(any(RawMaterials.class))).thenReturn(rawMaterial);
        RawMaterials saved = rawMaterialsServices.save(request);
        assertNotNull(saved);
        assertEquals(rawMaterial.getCode(), saved.getCode());
    }

    @Test
    void deleteById_shouldDeleteWhenFound() {
        when(rawMaterialsRepository.findById(id)).thenReturn(Optional.of(rawMaterial));
        doNothing().when(rawMaterialsRepository).deleteById(id);
        assertDoesNotThrow(() -> rawMaterialsServices.deleteById(id));
        verify(rawMaterialsRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteById_shouldThrowExceptionWhenNotFound() {
        when(rawMaterialsRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> rawMaterialsServices.deleteById(id));
    }

    @Test
    void update_shouldUpdateRawMaterialWhenFound() {
        RawMaterialsRequest request = new RawMaterialsRequest("CODE2", "Updated Raw Material", 200);
        when(rawMaterialsRepository.findById(id)).thenReturn(Optional.of(rawMaterial));
        when(rawMaterialsRepository.save(any(RawMaterials.class))).thenReturn(rawMaterial);
        assertDoesNotThrow(() -> rawMaterialsServices.update(id, request));
        verify(rawMaterialsRepository, times(1)).save(any(RawMaterials.class));
    }

    @Test
    void update_shouldThrowExceptionWhenNotFound() {
        RawMaterialsRequest request = new RawMaterialsRequest("CODE2", "Updated Raw Material", 200);
        when(rawMaterialsRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> rawMaterialsServices.update(id, request));
    }
}


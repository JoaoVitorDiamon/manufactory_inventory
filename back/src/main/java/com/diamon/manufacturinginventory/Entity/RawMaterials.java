package com.diamon.manufacturinginventory.Entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "raw_materials")
public class RawMaterials {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "stock_quantity")
    private int stockQuantity;


    public RawMaterials() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public RawMaterials(UUID id, String code, String name, int stockQuantity) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.stockQuantity = stockQuantity;
    }
}

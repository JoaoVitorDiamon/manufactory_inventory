package com.diamon.manufacturinginventory.Entity;


import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "product_recipes")
public class ProductRecipes {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "required_quantity")
    private int requiredQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_product_recipes_product_id", foreignKeyDefinition = "FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE"))
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "raw_product_id", nullable = false, foreignKey = @ForeignKey(name = "fk_product_recipes_raw_product_id", foreignKeyDefinition = "FOREIGN KEY (raw_product_id) REFERENCES raw_materials(id) ON DELETE CASCADE"))
    private RawMaterials rawProduct;

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setRawProduct(RawMaterials rawProduct) {
        this.rawProduct = rawProduct;
    }

    public void setRequiredQuantity(int requiredQuantity) {
        this.requiredQuantity = requiredQuantity;
    }

    public UUID getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public RawMaterials getRawProduct() {
        return rawProduct;
    }

    public int getRequiredQuantity() {
        return requiredQuantity;
    }
}

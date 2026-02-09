package com.diamon.manufacturinginventory.Repository;

import com.diamon.manufacturinginventory.Entity.RawMaterials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RawMaterialsRepository extends JpaRepository<RawMaterials, UUID> {
}

package com.cayi.mes.repository;

import com.cayi.mes.entity.BillOfMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillOfMaterialRepository extends JpaRepository<BillOfMaterial, Long> {
    List<BillOfMaterial> findByParentItemId(Long parentItemId);
    List<BillOfMaterial> findByComponentItemId(Long componentItemId);
    boolean existsByParentItemIdAndComponentItemId(Long parentItemId, Long componentItemId);
}
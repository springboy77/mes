package com.cayi.mes.controller;

import com.cayi.mes.entity.BillOfMaterial;
import com.cayi.mes.entity.Item;
import com.cayi.mes.repository.BillOfMaterialRepository;
import com.cayi.mes.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/boms")
public class BOMController {
    @Autowired
    private BillOfMaterialRepository bomRepository;
    @Autowired
    private ItemRepository itemRepository;

    @GetMapping
    public List<BillOfMaterial> getAllBOMs() {
        return bomRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillOfMaterial> getBOMById(@PathVariable Long id) {
        Optional<BillOfMaterial> bom = bomRepository.findById(id);
        return bom.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/parent/{parentItemId}")
    public List<BillOfMaterial> getBOMsByParentItemId(@PathVariable Long parentItemId) {
        return bomRepository.findByParentItemId(parentItemId);
    }

    @GetMapping("/component/{componentItemId}")
    public List<BillOfMaterial> getBOMsByComponentItemId(@PathVariable Long componentItemId) {
        return bomRepository.findByComponentItemId(componentItemId);
    }

    @PostMapping
    public ResponseEntity<?> createBOM(@RequestBody BOMRequest request) {
        try {
            Optional<Item> parentItem = itemRepository.findById(request.getParentItemId());
            Optional<Item> componentItem = itemRepository.findById(request.getComponentItemId());

            if (parentItem.isEmpty() || componentItem.isEmpty()) {
                return ResponseEntity.badRequest().body("父项或组件物料不存在");
            }
            if (bomRepository.existsByParentItemIdAndComponentItemId(
                    request.getParentItemId(), request.getComponentItemId())) {
                return ResponseEntity.badRequest().body("BOM记录已存在");
            }

            BillOfMaterial bom = new BillOfMaterial();
            bom.setParentItem(parentItem.get());
            bom.setComponentItem(componentItem.get());
            bom.setQtyRequired(request.getQtyRequired());
            bom.setOperationStep(request.getOperationStep());
            bom.setDescription(request.getDescription());

            BillOfMaterial savedBOM = bomRepository.save(bom);
            return ResponseEntity.ok(savedBOM);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("创建失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBOM(@PathVariable Long id, @RequestBody BOMRequest request) {
        try {
            Optional<BillOfMaterial> optionalBOM = bomRepository.findById(id);
            if (optionalBOM.isEmpty()) return ResponseEntity.notFound().build();

            BillOfMaterial bom = optionalBOM.get();
            bom.setQtyRequired(request.getQtyRequired());
            bom.setOperationStep(request.getOperationStep());
            bom.setDescription(request.getDescription());

            return ResponseEntity.ok(bomRepository.save(bom));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("更新失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBOM(@PathVariable Long id) {
        if (bomRepository.existsById(id)) {
            bomRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    public static class BOMRequest {
        private Long parentItemId;
        private Long componentItemId;
        private BigDecimal qtyRequired;
        private Integer operationStep;
        private String description;

        public Long getParentItemId() { return parentItemId; }
        public void setParentItemId(Long parentItemId) { this.parentItemId = parentItemId; }
        public Long getComponentItemId() { return componentItemId; }
        public void setComponentItemId(Long componentItemId) { this.componentItemId = componentItemId; }
        public BigDecimal getQtyRequired() { return qtyRequired; }
        public void setQtyRequired(BigDecimal qtyRequired) { this.qtyRequired = qtyRequired; }
        public Integer getOperationStep() { return operationStep; }
        public void setOperationStep(Integer operationStep) { this.operationStep = operationStep; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}
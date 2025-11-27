package com.cayi.mes.controller;

import com.cayi.mes.entity.Routing;
import com.cayi.mes.entity.Item;
import com.cayi.mes.repository.RoutingRepository;
import com.cayi.mes.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/routings")
public class RoutingController {
    @Autowired
    private RoutingRepository routingRepository;
    @Autowired
    private ItemRepository itemRepository;

    // 获取所有工艺路线 - 修复序列化问题
    @GetMapping
    public ResponseEntity<List<Routing>> getAllRoutings() {
        try {
            List<Routing> routings = routingRepository.findAll();

            // 手动初始化关联对象，避免延迟加载问题
            for (Routing routing : routings) {
                if (routing.getItem() != null) {
                    // 触发延迟加载，确保数据完整
                    routing.getItem().getId();
                    routing.getItem().getName();
                    routing.getItem().getCode();
                }
                // 避免循环引用，清空operations
                routing.setOperations(null);
            }

            return ResponseEntity.ok(routings);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Routing> getRoutingById(@PathVariable Long id) {
        try {
            Optional<Routing> routing = routingRepository.findById(id);
            if (routing.isPresent()) {
                Routing routingObj = routing.get();
                // 手动初始化关联对象
                if (routingObj.getItem() != null) {
                    routingObj.getItem().getId();
                    routingObj.getItem().getName();
                }
                routingObj.setOperations(null); // 避免循环引用
                return ResponseEntity.ok(routingObj);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<Routing>> getRoutingsByItemId(@PathVariable Long itemId) {
        try {
            List<Routing> routings = routingRepository.findByItemId(itemId);
            for (Routing routing : routings) {
                if (routing.getItem() != null) {
                    routing.getItem().getId();
                    routing.getItem().getName();
                }
                routing.setOperations(null);
            }
            return ResponseEntity.ok(routings);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping
    public ResponseEntity<?> createRouting(@RequestBody RoutingRequest request) {
        try {
            Optional<Item> item = itemRepository.findById(request.getItemId());
            if (item.isEmpty()) {
                return ResponseEntity.badRequest().body("物料不存在");
            }
            Routing routing = new Routing();
            routing.setItem(item.get());
            routing.setVersion(request.getVersion());
            routing.setIsDefault(request.getIsDefault());
            routing.setDescription(request.getDescription());

            Routing savedRouting = routingRepository.save(routing);
            // 返回简化的响应
            return ResponseEntity.ok(new SimpleRoutingResponse(
                    savedRouting.getId(),
                    savedRouting.getVersion(),
                    savedRouting.getDescription()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("创建失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRouting(@PathVariable Long id, @RequestBody RoutingRequest request) {
        try {
            Optional<Routing> optionalRouting = routingRepository.findById(id);
            if (optionalRouting.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Routing routing = optionalRouting.get();
            routing.setVersion(request.getVersion());
            routing.setIsDefault(request.getIsDefault());
            routing.setDescription(request.getDescription());

            Routing updatedRouting = routingRepository.save(routing);
            return ResponseEntity.ok(new SimpleRoutingResponse(
                    updatedRouting.getId(),
                    updatedRouting.getVersion(),
                    updatedRouting.getDescription()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("更新失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRouting(@PathVariable Long id) {
        try {
            if (routingRepository.existsById(id)) {
                routingRepository.deleteById(id);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/item/{itemId}/default")
    public ResponseEntity<Routing> getDefaultRoutingByItemId(@PathVariable Long itemId) {
        try {
            Optional<Routing> routing = routingRepository.findByItemIdAndIsDefaultTrue(itemId);
            if (routing.isPresent()) {
                Routing routingObj = routing.get();
                if (routingObj.getItem() != null) {
                    routingObj.getItem().getId();
                    routingObj.getItem().getName();
                }
                routingObj.setOperations(null);
                return ResponseEntity.ok(routingObj);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    // 简化的响应DTO
    public static class SimpleRoutingResponse {
        private Long id;
        private String version;
        private String description;

        public SimpleRoutingResponse(Long id, String version, String description) {
            this.id = id;
            this.version = version;
            this.description = description;
        }

        public Long getId() { return id; }
        public String getVersion() { return version; }
        public String getDescription() { return description; }
    }

    public static class RoutingRequest {
        private Long itemId;
        private String version = "1.0";
        private Boolean isDefault = true;
        private String description;

        public Long getItemId() { return itemId; }
        public void setItemId(Long itemId) { this.itemId = itemId; }
        public String getVersion() { return version; }
        public void setVersion(String version) { this.version = version; }
        public Boolean getIsDefault() { return isDefault; }
        public void setIsDefault(Boolean isDefault) { this.isDefault = isDefault; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}
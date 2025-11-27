package com.cayi.mes.controller;

import com.cayi.mes.entity.SalesOrder;
import com.cayi.mes.entity.Item;
import com.cayi.mes.repository.SalesOrderRepository;
import com.cayi.mes.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/sales-orders")
public class SalesOrderController {
    @Autowired
    private SalesOrderRepository salesOrderRepository;
    @Autowired
    private ItemRepository itemRepository;

    @GetMapping
    public List<SalesOrder> getAllSalesOrders() {
        return salesOrderRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesOrder> getSalesOrderById(@PathVariable Long id) {
        Optional<SalesOrder> salesOrder = salesOrderRepository.findById(id);
        return salesOrder.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/order-number/{orderNumber}")
    public ResponseEntity<SalesOrder> getSalesOrderByOrderNumber(@PathVariable String orderNumber) {
        Optional<SalesOrder> salesOrder = salesOrderRepository.findByOrderNumber(orderNumber);
        return salesOrder.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createSalesOrder(@RequestBody SalesOrderRequest request) {
        try {
            // 验证订单号唯一性
            if (salesOrderRepository.existsByOrderNumber(request.getOrderNumber())) {
                return ResponseEntity.badRequest().body("订单号已存在");
            }

            // 验证物料是否存在
            Optional<Item> item = itemRepository.findById(request.getItemId());
            if (item.isEmpty()) {
                return ResponseEntity.badRequest().body("物料不存在");
            }

            SalesOrder salesOrder = new SalesOrder();
            salesOrder.setOrderNumber(request.getOrderNumber());
            salesOrder.setItem(item.get());
            salesOrder.setQty(request.getQty());
            salesOrder.setDueDate(request.getDueDate());
            salesOrder.setCustomerName(request.getCustomerName());
            salesOrder.setDescription(request.getDescription());
            salesOrder.setStatus("ENTERED");

            SalesOrder savedOrder = salesOrderRepository.save(salesOrder);
            return ResponseEntity.ok(savedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("创建失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSalesOrder(@PathVariable Long id, @RequestBody SalesOrderRequest request) {
        try {
            Optional<SalesOrder> optionalSalesOrder = salesOrderRepository.findById(id);
            if (optionalSalesOrder.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            SalesOrder salesOrder = optionalSalesOrder.get();
            salesOrder.setQty(request.getQty());
            salesOrder.setDueDate(request.getDueDate());
            salesOrder.setCustomerName(request.getCustomerName());
            salesOrder.setDescription(request.getDescription());
            salesOrder.setStatus(request.getStatus());

            return ResponseEntity.ok(salesOrderRepository.save(salesOrder));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("更新失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateSalesOrderStatus(@PathVariable Long id, @RequestBody StatusUpdateRequest request) {
        try {
            Optional<SalesOrder> optionalSalesOrder = salesOrderRepository.findById(id);
            if (optionalSalesOrder.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            SalesOrder salesOrder = optionalSalesOrder.get();
            salesOrder.setStatus(request.getStatus());

            return ResponseEntity.ok(salesOrderRepository.save(salesOrder));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("状态更新失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalesOrder(@PathVariable Long id) {
        if (salesOrderRepository.existsById(id)) {
            salesOrderRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/status/{status}")
    public List<SalesOrder> getSalesOrdersByStatus(@PathVariable String status) {
        return salesOrderRepository.findByStatus(status);
    }

    // 请求DTO类
    public static class SalesOrderRequest {
        private String orderNumber;
        private Long itemId;
        private java.math.BigDecimal qty;
        private LocalDateTime dueDate;
        private String customerName;
        private String description;
        private String status;

        public String getOrderNumber() { return orderNumber; }
        public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
        public Long getItemId() { return itemId; }
        public void setItemId(Long itemId) { this.itemId = itemId; }
        public java.math.BigDecimal getQty() { return qty; }
        public void setQty(java.math.BigDecimal qty) { this.qty = qty; }
        public LocalDateTime getDueDate() { return dueDate; }
        public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    public static class StatusUpdateRequest {
        private String status;

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}
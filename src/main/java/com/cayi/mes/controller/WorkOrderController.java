package com.cayi.mes.controller;

import com.cayi.mes.entity.WorkOrder;
import com.cayi.mes.entity.Item;
import com.cayi.mes.entity.SalesOrder;
import com.cayi.mes.entity.Routing;
import com.cayi.mes.repository.WorkOrderRepository;
import com.cayi.mes.repository.ItemRepository;
import com.cayi.mes.repository.SalesOrderRepository;
import com.cayi.mes.repository.RoutingRepository;
import com.cayi.mes.dto.WorkOrderDTO;
import com.cayi.mes.dto.DTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/work-orders")
public class WorkOrderController {
    @Autowired
    private WorkOrderRepository workOrderRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private SalesOrderRepository salesOrderRepository;
    @Autowired
    private RoutingRepository routingRepository;

    @GetMapping
    public List<WorkOrderDTO> getAllWorkOrders() {
        List<WorkOrder> workOrders = workOrderRepository.findAll();
        return workOrders.stream()
                .map(DTOConverter::toWorkOrderDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkOrderDTO> getWorkOrderById(@PathVariable Long id) {
        Optional<WorkOrder> workOrder = workOrderRepository.findById(id);
        return workOrder.map(wo -> ResponseEntity.ok(DTOConverter.toWorkOrderDTO(wo)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/status/{status}")
    public List<WorkOrderDTO> getWorkOrdersByStatus(@PathVariable WorkOrder.WorkOrderStatus status) {
        List<WorkOrder> workOrders = workOrderRepository.findByStatus(status);
        return workOrders.stream()
                .map(DTOConverter::toWorkOrderDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/pending-scheduling")
    public List<WorkOrderDTO> getPendingSchedulingWorkOrders() {
        List<WorkOrder> workOrders = workOrderRepository.findByStatusOrderByPriorityAscDueDateAsc(WorkOrder.WorkOrderStatus.PENDING);
        return workOrders.stream()
                .map(DTOConverter::toWorkOrderDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<?> createWorkOrder(@RequestBody WorkOrderRequest request) {
        try {
            // 验证工单号唯一性
            if (workOrderRepository.existsByCode(request.getCode())) {
                return ResponseEntity.badRequest().body("工单号已存在");
            }

            // 验证物料是否存在
            Optional<Item> item = itemRepository.findById(request.getItemId());
            if (item.isEmpty()) {
                return ResponseEntity.badRequest().body("物料不存在");
            }

            WorkOrder workOrder = new WorkOrder();
            workOrder.setCode(request.getCode());
            workOrder.setItem(item.get());
            workOrder.setQtyToProduce(request.getQtyToProduce());
            workOrder.setDueDate(request.getDueDate());
            workOrder.setPriority(request.getPriority());
            workOrder.setDescription(request.getDescription());

            // 设置销售订单（如果提供）
            if (request.getSalesOrderId() != null) {
                Optional<SalesOrder> salesOrder = salesOrderRepository.findById(request.getSalesOrderId());
                salesOrder.ifPresent(workOrder::setSalesOrder);
            }

            // 设置工艺路线（如果提供）
            if (request.getRoutingId() != null) {
                Optional<Routing> routing = routingRepository.findById(request.getRoutingId());
                routing.ifPresent(workOrder::setRouting);
            }

            WorkOrder savedWorkOrder = workOrderRepository.save(workOrder);
            return ResponseEntity.ok(DTOConverter.toWorkOrderDTO(savedWorkOrder));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("创建失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateWorkOrder(@PathVariable Long id, @RequestBody WorkOrderRequest request) {
        try {
            Optional<WorkOrder> optionalWorkOrder = workOrderRepository.findById(id);
            if (optionalWorkOrder.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            WorkOrder workOrder = optionalWorkOrder.get();
            workOrder.setQtyToProduce(request.getQtyToProduce());
            workOrder.setDueDate(request.getDueDate());
            workOrder.setPriority(request.getPriority());
            workOrder.setDescription(request.getDescription());

            // 更新工艺路线
            if (request.getRoutingId() != null) {
                Optional<Routing> routing = routingRepository.findById(request.getRoutingId());
                routing.ifPresent(workOrder::setRouting);
            }

            WorkOrder updatedWorkOrder = workOrderRepository.save(workOrder);
            return ResponseEntity.ok(DTOConverter.toWorkOrderDTO(updatedWorkOrder));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("更新失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateWorkOrderStatus(@PathVariable Long id, @RequestBody WorkOrderStatusRequest request) {
        try {
            Optional<WorkOrder> optionalWorkOrder = workOrderRepository.findById(id);
            if (optionalWorkOrder.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            WorkOrder workOrder = optionalWorkOrder.get();
            workOrder.setStatus(request.getStatus());

            // 根据状态更新实际时间
            if (request.getStatus() == WorkOrder.WorkOrderStatus.IN_PROGRESS && workOrder.getActualStart() == null) {
                workOrder.setActualStart(LocalDateTime.now());
            } else if (request.getStatus() == WorkOrder.WorkOrderStatus.COMPLETED && workOrder.getActualEnd() == null) {
                workOrder.setActualEnd(LocalDateTime.now());
            }

            WorkOrder updatedWorkOrder = workOrderRepository.save(workOrder);
            return ResponseEntity.ok(DTOConverter.toWorkOrderDTO(updatedWorkOrder));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("状态更新失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/schedule")
    public ResponseEntity<?> scheduleWorkOrder(@PathVariable Long id, @RequestBody ScheduleRequest request) {
        try {
            Optional<WorkOrder> optionalWorkOrder = workOrderRepository.findById(id);
            if (optionalWorkOrder.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            WorkOrder workOrder = optionalWorkOrder.get();
            workOrder.setPlannedStart(request.getPlannedStart());
            workOrder.setPlannedEnd(request.getPlannedEnd());
            workOrder.setStatus(WorkOrder.WorkOrderStatus.SCHEDULED);

            WorkOrder updatedWorkOrder = workOrderRepository.save(workOrder);
            return ResponseEntity.ok(DTOConverter.toWorkOrderDTO(updatedWorkOrder));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("排程失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkOrder(@PathVariable Long id) {
        if (workOrderRepository.existsById(id)) {
            workOrderRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // 请求DTO类（保持不变）
    public static class WorkOrderRequest {
        private String code;
        private Long salesOrderId;
        private Long itemId;
        private java.math.BigDecimal qtyToProduce;
        private LocalDateTime dueDate;
        private Long routingId;
        private Integer priority = 5;
        private String description;

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public Long getSalesOrderId() { return salesOrderId; }
        public void setSalesOrderId(Long salesOrderId) { this.salesOrderId = salesOrderId; }
        public Long getItemId() { return itemId; }
        public void setItemId(Long itemId) { this.itemId = itemId; }
        public java.math.BigDecimal getQtyToProduce() { return qtyToProduce; }
        public void setQtyToProduce(java.math.BigDecimal qtyToProduce) { this.qtyToProduce = qtyToProduce; }
        public LocalDateTime getDueDate() { return dueDate; }
        public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
        public Long getRoutingId() { return routingId; }
        public void setRoutingId(Long routingId) { this.routingId = routingId; }
        public Integer getPriority() { return priority; }
        public void setPriority(Integer priority) { this.priority = priority; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    public static class WorkOrderStatusRequest {
        private WorkOrder.WorkOrderStatus status;

        public WorkOrder.WorkOrderStatus getStatus() { return status; }
        public void setStatus(WorkOrder.WorkOrderStatus status) { this.status = status; }
    }

    public static class ScheduleRequest {
        private LocalDateTime plannedStart;
        private LocalDateTime plannedEnd;

        public LocalDateTime getPlannedStart() { return plannedStart; }
        public void setPlannedStart(LocalDateTime plannedStart) { this.plannedStart = plannedStart; }
        public LocalDateTime getPlannedEnd() { return plannedEnd; }
        public void setPlannedEnd(LocalDateTime plannedEnd) { this.plannedEnd = plannedEnd; }
    }
}
package com.cayi.mes.dto;

import com.cayi.mes.entity.WorkOrder;
import java.time.LocalDateTime;
import java.math.BigDecimal;

public class WorkOrderDTO {
    private Long id;
    private String code;
    private Long salesOrderId;
    private String salesOrderNumber;
    private Long itemId;
    private String itemName;
    private String itemCode;
    private BigDecimal qtyToProduce;
    private WorkOrder.WorkOrderStatus status;
    private LocalDateTime dueDate;
    private LocalDateTime plannedStart;
    private LocalDateTime plannedEnd;
    private LocalDateTime actualStart;
    private LocalDateTime actualEnd;
    private Long routingId;
    private String routingVersion;
    private Integer priority;
    private String description;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public WorkOrderDTO() {}

    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public Long getSalesOrderId() { return salesOrderId; }
    public void setSalesOrderId(Long salesOrderId) { this.salesOrderId = salesOrderId; }

    public String getSalesOrderNumber() { return salesOrderNumber; }
    public void setSalesOrderNumber(String salesOrderNumber) { this.salesOrderNumber = salesOrderNumber; }

    public Long getItemId() { return itemId; }
    public void setItemId(Long itemId) { this.itemId = itemId; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getItemCode() { return itemCode; }
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }

    public BigDecimal getQtyToProduce() { return qtyToProduce; }
    public void setQtyToProduce(BigDecimal qtyToProduce) { this.qtyToProduce = qtyToProduce; }

    public WorkOrder.WorkOrderStatus getStatus() { return status; }
    public void setStatus(WorkOrder.WorkOrderStatus status) { this.status = status; }

    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }

    public LocalDateTime getPlannedStart() { return plannedStart; }
    public void setPlannedStart(LocalDateTime plannedStart) { this.plannedStart = plannedStart; }

    public LocalDateTime getPlannedEnd() { return plannedEnd; }
    public void setPlannedEnd(LocalDateTime plannedEnd) { this.plannedEnd = plannedEnd; }

    public LocalDateTime getActualStart() { return actualStart; }
    public void setActualStart(LocalDateTime actualStart) { this.actualStart = actualStart; }

    public LocalDateTime getActualEnd() { return actualEnd; }
    public void setActualEnd(LocalDateTime actualEnd) { this.actualEnd = actualEnd; }

    public Long getRoutingId() { return routingId; }
    public void setRoutingId(Long routingId) { this.routingId = routingId; }

    public String getRoutingVersion() { return routingVersion; }
    public void setRoutingVersion(String routingVersion) { this.routingVersion = routingVersion; }

    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }

    public LocalDateTime getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(LocalDateTime updatedTime) { this.updatedTime = updatedTime; }
}
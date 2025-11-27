package com.cayi.mes.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "work_order")
public class WorkOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String code;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "sales_order_id")
    private SalesOrder salesOrder;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(name = "qty_to_produce", nullable = false, precision = 15, scale = 4)
    private BigDecimal qtyToProduce;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private WorkOrderStatus status = WorkOrderStatus.PENDING;

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Column(name = "planned_start")
    private LocalDateTime plannedStart;

    @Column(name = "planned_end")
    private LocalDateTime plannedEnd;

    @Column(name = "actual_start")
    private LocalDateTime actualStart;

    @Column(name = "actual_end")
    private LocalDateTime actualEnd;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "routing_id")
    private Routing routing;

    private Integer priority = 5; // 1-10, 1为最高优先级

    private String description;

    @Column(name = "created_time")
    private LocalDateTime createdTime = LocalDateTime.now();

    @Column(name = "updated_time")
    private LocalDateTime updatedTime = LocalDateTime.now();

    // 工单状态枚举
    public enum WorkOrderStatus {
        PENDING,        // 待排程
        SCHEDULED,      // 已排程
        IN_PROGRESS,    // 生产中
        COMPLETED,      // 已完成
        CANCELLED       // 已取消
    }

    // 构造函数
    public WorkOrder() {}

    public WorkOrder(String code, Item item, BigDecimal qtyToProduce, LocalDateTime dueDate) {
        this.code = code;
        this.item = item;
        this.qtyToProduce = qtyToProduce;
        this.dueDate = dueDate;
    }

    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public SalesOrder getSalesOrder() { return salesOrder; }
    public void setSalesOrder(SalesOrder salesOrder) { this.salesOrder = salesOrder; }

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }

    public BigDecimal getQtyToProduce() { return qtyToProduce; }
    public void setQtyToProduce(BigDecimal qtyToProduce) { this.qtyToProduce = qtyToProduce; }

    public WorkOrderStatus getStatus() { return status; }
    public void setStatus(WorkOrderStatus status) { this.status = status; }

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

    public Routing getRouting() { return routing; }
    public void setRouting(Routing routing) { this.routing = routing; }

    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }

    public LocalDateTime getUpdatedTime() { return updatedTime; }
    public void setUpdatedTime(LocalDateTime updatedTime) { this.updatedTime = updatedTime; }

    @PreUpdate
    public void preUpdate() {
        this.updatedTime = LocalDateTime.now();
    }

    // 业务方法
    public boolean canStart() {
        return status == WorkOrderStatus.SCHEDULED;
    }

    public boolean canComplete() {
        return status == WorkOrderStatus.IN_PROGRESS;
    }

    public boolean isOverdue() {
        return dueDate != null && LocalDateTime.now().isAfter(dueDate);
    }
}
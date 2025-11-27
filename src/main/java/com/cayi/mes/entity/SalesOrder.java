package com.cayi.mes.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "sales_order")
public class SalesOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", unique = true, nullable = false, length = 100)
    private String orderNumber;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(nullable = false, precision = 15, scale = 4)
    private BigDecimal qty;

    @Column(name = "due_date", nullable = false)
    private LocalDateTime dueDate;

    @Column(length = 50)
    private String status = "ENTERED"; // ENTERED, CONFIRMED, COMPLETED, CANCELLED

    @Column(name = "customer_name")
    private String customerName;

    private String description;

    @Column(name = "created_time")
    private LocalDateTime createdTime = LocalDateTime.now();

    @Column(name = "updated_time")
    private LocalDateTime updatedTime = LocalDateTime.now();

    @JsonIgnore // 避免循环引用
    @OneToMany(mappedBy = "salesOrder")
    private java.util.List<WorkOrder> workOrders;

    // 构造函数
    public SalesOrder() {}

    public SalesOrder(String orderNumber, Item item, BigDecimal qty, LocalDateTime dueDate) {
        this.orderNumber = orderNumber;
        this.item = item;
        this.qty = qty;
        this.dueDate = dueDate;
    }

    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }

    public BigDecimal getQty() { return qty; }
    public void setQty(BigDecimal qty) { this.qty = qty; }

    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

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
}
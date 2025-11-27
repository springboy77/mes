package com.cayi.mes.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "bill_of_material")
public class BillOfMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_item_id", nullable = false)
    private Item parentItem;

    @ManyToOne
    @JoinColumn(name = "component_item_id", nullable = false)
    private Item componentItem;

    @Column(name = "qty_required", nullable = false, precision = 15, scale = 4)
    private BigDecimal qtyRequired;

    private Integer operationStep;
    private String description;

    public BillOfMaterial() {}
    public BillOfMaterial(Item parentItem, Item componentItem, BigDecimal qtyRequired) {
        this.parentItem = parentItem;
        this.componentItem = componentItem;
        this.qtyRequired = qtyRequired;
    }

    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Item getParentItem() { return parentItem; }
    public void setParentItem(Item parentItem) { this.parentItem = parentItem; }
    public Item getComponentItem() { return componentItem; }
    public void setComponentItem(Item componentItem) { this.componentItem = componentItem; }
    public BigDecimal getQtyRequired() { return qtyRequired; }
    public void setQtyRequired(BigDecimal qtyRequired) { this.qtyRequired = qtyRequired; }
    public Integer getOperationStep() { return operationStep; }
    public void setOperationStep(Integer operationStep) { this.operationStep = operationStep; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
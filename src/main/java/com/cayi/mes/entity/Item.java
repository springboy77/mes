package com.cayi.mes.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String code;

    @Column(nullable = false, length = 255)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ItemType type = ItemType.RAW;

    @Column(name = "on_hand_qty", precision = 15, scale = 4)
    private BigDecimal onHandQty = BigDecimal.ZERO;

    private String description;

    @Column(name = "is_active")
    private Boolean active = true;

    public enum ItemType { RAW, SEMI, FINISHED }

    public Item() {}
    public Item(String code, String name, ItemType type) {
        this.code = code;
        this.name = name;
        this.type = type;
    }

    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public ItemType getType() { return type; }
    public void setType(ItemType type) { this.type = type; }
    public BigDecimal getOnHandQty() { return onHandQty; }
    public void setOnHandQty(BigDecimal onHandQty) { this.onHandQty = onHandQty; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
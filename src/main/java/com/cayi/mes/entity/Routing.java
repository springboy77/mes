package com.cayi.mes.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "routing")
public class Routing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(nullable = false, length = 50)
    private String version = "1.0";

    @Column(name = "is_default")
    private Boolean isDefault = true;

    private String description;

    @Column(name = "created_time")
    private LocalDateTime createdTime = LocalDateTime.now();

    @OneToMany(mappedBy = "routing", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Operation> operations = new ArrayList<>();

    public Routing() {}
    public Routing(Item item, String version) {
        this.item = item;
        this.version = version;
    }

    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Item getItem() { return item; }
    public void setItem(Item item) { this.item = item; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public Boolean getIsDefault() { return isDefault; }
    public void setIsDefault(Boolean isDefault) { this.isDefault = isDefault; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreatedTime() { return createdTime; }
    public void setCreatedTime(LocalDateTime createdTime) { this.createdTime = createdTime; }
    public List<Operation> getOperations() { return operations; }
    public void setOperations(List<Operation> operations) { this.operations = operations; }

    public void addOperation(Operation operation) {
        operations.add(operation);
        operation.setRouting(this);
    }
    public void removeOperation(Operation operation) {
        operations.remove(operation);
        operation.setRouting(null);
    }
}
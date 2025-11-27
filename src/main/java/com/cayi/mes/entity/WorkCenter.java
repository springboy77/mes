package com.cayi.mes.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Table(name = "work_center")
public class WorkCenter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String code;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(precision = 5, scale = 4)
    private BigDecimal efficiency = BigDecimal.ONE;

    @Column(name = "available_start")
    private LocalTime availableStart = LocalTime.of(8, 0);

    @Column(name = "available_end")
    private LocalTime availableEnd = LocalTime.of(17, 0);

    @Column(name = "is_active")
    private Boolean active = true;

    public WorkCenter() {}
    public WorkCenter(String code, String name) {
        this.code = code;
        this.name = name;
    }

    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getEfficiency() { return efficiency; }
    public void setEfficiency(BigDecimal efficiency) { this.efficiency = efficiency; }
    public LocalTime getAvailableStart() { return availableStart; }
    public void setAvailableStart(LocalTime availableStart) { this.availableStart = availableStart; }
    public LocalTime getAvailableEnd() { return availableEnd; }
    public void setAvailableEnd(LocalTime availableEnd) { this.availableEnd = availableEnd; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
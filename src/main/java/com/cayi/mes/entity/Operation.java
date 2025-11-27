package com.cayi.mes.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "operation")
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "routing_id", nullable = false)
    private Routing routing;

    @Column(nullable = false)
    private Integer step;

    @Column(nullable = false, length = 255)
    private String name;

    @ManyToOne
    @JoinColumn(name = "work_center_id", nullable = false)
    private WorkCenter workCenter;

    @Column(name = "setup_time")
    private Integer setupTime = 0;

    @Column(name = "process_time", nullable = false)
    private Integer processTime;

    @Column(name = "previous_operation_ids", length = 255)
    private String previousOperationIds;

    private String description;

    public Operation() {}
    public Operation(Routing routing, Integer step, String name, WorkCenter workCenter, Integer processTime) {
        this.routing = routing;
        this.step = step;
        this.name = name;
        this.workCenter = workCenter;
        this.processTime = processTime;
    }

    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Routing getRouting() { return routing; }
    public void setRouting(Routing routing) { this.routing = routing; }
    public Integer getStep() { return step; }
    public void setStep(Integer step) { this.step = step; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public WorkCenter getWorkCenter() { return workCenter; }
    public void setWorkCenter(WorkCenter workCenter) { this.workCenter = workCenter; }
    public Integer getSetupTime() { return setupTime; }
    public void setSetupTime(Integer setupTime) { this.setupTime = setupTime; }
    public Integer getProcessTime() { return processTime; }
    public void setProcessTime(Integer processTime) { this.processTime = processTime; }
    public String getPreviousOperationIds() { return previousOperationIds; }
    public void setPreviousOperationIds(String previousOperationIds) { this.previousOperationIds = previousOperationIds; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getTotalTime() {
        return setupTime + processTime;
    }
}
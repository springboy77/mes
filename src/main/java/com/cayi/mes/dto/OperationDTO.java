package com.cayi.mes.dto;

public class OperationDTO {
    private Long id;
    private Long routingId;
    private String routingVersion;
    private Long workCenterId;
    private String workCenterName;
    private Integer step;
    private String name;
    private Integer setupTime;
    private Integer processTime;
    private String previousOperationIds;
    private String description;

    public OperationDTO() {}

    // Getter和Setter方法
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRoutingId() { return routingId; }
    public void setRoutingId(Long routingId) { this.routingId = routingId; }
    public String getRoutingVersion() { return routingVersion; }
    public void setRoutingVersion(String routingVersion) { this.routingVersion = routingVersion; }
    public Long getWorkCenterId() { return workCenterId; }
    public void setWorkCenterId(Long workCenterId) { this.workCenterId = workCenterId; }
    public String getWorkCenterName() { return workCenterName; }
    public void setWorkCenterName(String workCenterName) { this.workCenterName = workCenterName; }
    public Integer getStep() { return step; }
    public void setStep(Integer step) { this.step = step; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getSetupTime() { return setupTime; }
    public void setSetupTime(Integer setupTime) { this.setupTime = setupTime; }
    public Integer getProcessTime() { return processTime; }
    public void setProcessTime(Integer processTime) { this.processTime = processTime; }
    public String getPreviousOperationIds() { return previousOperationIds; }
    public void setPreviousOperationIds(String previousOperationIds) { this.previousOperationIds = previousOperationIds; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
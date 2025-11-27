package com.cayi.mes.aps.domain;

import java.time.LocalDateTime;

public class ScheduledTask {
    private Long workOrderId;
    private String workOrderCode;
    private Long operationId;
    private String operationName;
    private Long workCenterId;
    private String workCenterName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int duration; // 分钟
    private int setupTime; // 分钟
    private boolean scheduled;
    private String status; // SCHEDULED, CONFLICT, UNSCHEDULABLE

    // 构造函数
    public ScheduledTask() {}

    public ScheduledTask(Long workOrderId, String workOrderCode, Long operationId, String operationName,
                         Long workCenterId, String workCenterName, int duration, int setupTime) {
        this.workOrderId = workOrderId;
        this.workOrderCode = workOrderCode;
        this.operationId = operationId;
        this.operationName = operationName;
        this.workCenterId = workCenterId;
        this.workCenterName = workCenterName;
        this.duration = duration;
        this.setupTime = setupTime;
        this.scheduled = false;
        this.status = "UNSCHEDULABLE";
    }

    // Getter和Setter方法
    public Long getWorkOrderId() { return workOrderId; }
    public void setWorkOrderId(Long workOrderId) { this.workOrderId = workOrderId; }

    public String getWorkOrderCode() { return workOrderCode; }
    public void setWorkOrderCode(String workOrderCode) { this.workOrderCode = workOrderCode; }

    public Long getOperationId() { return operationId; }
    public void setOperationId(Long operationId) { this.operationId = operationId; }

    public String getOperationName() { return operationName; }
    public void setOperationName(String operationName) { this.operationName = operationName; }

    public Long getWorkCenterId() { return workCenterId; }
    public void setWorkCenterId(Long workCenterId) { this.workCenterId = workCenterId; }

    public String getWorkCenterName() { return workCenterName; }
    public void setWorkCenterName(String workCenterName) { this.workCenterName = workCenterName; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public int getSetupTime() { return setupTime; }
    public void setSetupTime(int setupTime) { this.setupTime = setupTime; }

    public boolean isScheduled() { return scheduled; }
    public void setScheduled(boolean scheduled) { this.scheduled = scheduled; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // 业务方法
    public int getTotalTime() {
        return setupTime + duration;
    }

    public boolean hasTimeConflict(ScheduledTask other) {
        if (!this.workCenterId.equals(other.workCenterId)) {
            return false;
        }
        return this.startTime.isBefore(other.endTime) && other.startTime.isBefore(this.endTime);
    }
}
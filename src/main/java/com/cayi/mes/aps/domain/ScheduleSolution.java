package com.cayi.mes.aps.domain;

import java.time.LocalDateTime;
import java.util.List;

public class ScheduleSolution {
    private List<ScheduledTask> scheduledTasks;
    private LocalDateTime scheduleStartTime;
    private LocalDateTime scheduleEndTime;
    private int totalTasks;
    private int scheduledTasksCount;
    private String scoreExplanation;

    // 构造函数
    public ScheduleSolution() {}

    public ScheduleSolution(List<ScheduledTask> scheduledTasks, LocalDateTime scheduleStartTime, LocalDateTime scheduleEndTime) {
        this.scheduledTasks = scheduledTasks;
        this.scheduleStartTime = scheduleStartTime;
        this.scheduleEndTime = scheduleEndTime;
        this.totalTasks = scheduledTasks.size();
        this.scheduledTasksCount = (int) scheduledTasks.stream().filter(ScheduledTask::isScheduled).count();
    }

    // Getter和Setter方法
    public List<ScheduledTask> getScheduledTasks() { return scheduledTasks; }
    public void setScheduledTasks(List<ScheduledTask> scheduledTasks) { this.scheduledTasks = scheduledTasks; }

    public LocalDateTime getScheduleStartTime() { return scheduleStartTime; }
    public void setScheduleStartTime(LocalDateTime scheduleStartTime) { this.scheduleStartTime = scheduleStartTime; }

    public LocalDateTime getScheduleEndTime() { return scheduleEndTime; }
    public void setScheduleEndTime(LocalDateTime scheduleEndTime) { this.scheduleEndTime = scheduleEndTime; }

    public int getTotalTasks() { return totalTasks; }
    public void setTotalTasks(int totalTasks) { this.totalTasks = totalTasks; }

    public int getScheduledTasksCount() { return scheduledTasksCount; }
    public void setScheduledTasksCount(int scheduledTasksCount) { this.scheduledTasksCount = scheduledTasksCount; }

    public String getScoreExplanation() { return scoreExplanation; }
    public void setScoreExplanation(String scoreExplanation) { this.scoreExplanation = scoreExplanation; }
}
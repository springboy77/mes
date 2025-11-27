package com.cayi.mes.aps;

import com.cayi.mes.aps.domain.ScheduleSolution;
import com.cayi.mes.aps.domain.ScheduledTask;
import com.cayi.mes.entity.WorkOrder;
import com.cayi.mes.entity.Operation;
import com.cayi.mes.entity.WorkCenter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Component
public class SimpleScheduler {

    /**
     * 简单排程算法：基于优先级和交期的前向排程
     */
    public ScheduleSolution scheduleWorkOrders(List<WorkOrder> workOrders, LocalDateTime scheduleStart) {
        List<ScheduledTask> scheduledTasks = new ArrayList<>();

        // 按优先级和交期排序
        workOrders.sort((wo1, wo2) -> {
            int priorityCompare = wo1.getPriority().compareTo(wo2.getPriority());
            if (priorityCompare != 0) return priorityCompare;
            return wo1.getDueDate().compareTo(wo2.getDueDate());
        });

        // 为每个工单的每个工序创建排程任务
        for (WorkOrder workOrder : workOrders) {
            if (workOrder.getRouting() == null || workOrder.getRouting().getOperations().isEmpty()) {
                continue;
            }

            // 获取工单的工序（按步骤排序）
            List<Operation> operations = workOrder.getRouting().getOperations().stream()
                    .sorted(Comparator.comparing(Operation::getStep))
                    .toList();

            LocalDateTime currentTime = scheduleStart;

            for (Operation operation : operations) {
                ScheduledTask task = createScheduledTask(workOrder, operation, currentTime);

                if (canScheduleTask(task, scheduledTasks)) {
                    task.setScheduled(true);
                    task.setStatus("SCHEDULED");
                    scheduledTasks.add(task);
                    currentTime = task.getEndTime(); // 下一工序的开始时间
                } else {
                    task.setScheduled(false);
                    task.setStatus("CONFLICT");
                    scheduledTasks.add(task);
                    break; // 如果一个工序无法排程，跳过该工单的后续工序
                }
            }
        }

        LocalDateTime scheduleEnd = calculateScheduleEnd(scheduledTasks);
        ScheduleSolution solution = new ScheduleSolution(scheduledTasks, scheduleStart, scheduleEnd);
        solution.setScoreExplanation(calculateScoreExplanation(scheduledTasks, workOrders));

        return solution;
    }

    private ScheduledTask createScheduledTask(WorkOrder workOrder, Operation operation, LocalDateTime startTime) {
        WorkCenter workCenter = operation.getWorkCenter();
        int totalTime = operation.getTotalTime() * workOrder.getQtyToProduce().intValue();

        // 考虑设备效率
        double efficiency = workCenter.getEfficiency().doubleValue();
        int adjustedTime = (int) (totalTime / efficiency);

        LocalDateTime endTime = startTime.plusMinutes(adjustedTime);

        // 调整到工作时间范围内
        endTime = adjustToWorkingHours(endTime, workCenter);

        ScheduledTask task = new ScheduledTask(
                workOrder.getId(),
                workOrder.getCode(),
                operation.getId(),
                operation.getName(),
                workCenter.getId(),
                workCenter.getName(),
                operation.getProcessTime() * workOrder.getQtyToProduce().intValue(),
                operation.getSetupTime()
        );
        task.setStartTime(startTime);
        task.setEndTime(endTime);

        return task;
    }

    private boolean canScheduleTask(ScheduledTask newTask, List<ScheduledTask> existingTasks) {
        // 检查设备冲突
        for (ScheduledTask existingTask : existingTasks) {
            if (existingTask.isScheduled() && newTask.hasTimeConflict(existingTask)) {
                return false;
            }
        }
        return true;
    }

    private LocalDateTime adjustToWorkingHours(LocalDateTime time, WorkCenter workCenter) {
        LocalTime workStart = workCenter.getAvailableStart();
        LocalTime workEnd = workCenter.getAvailableEnd();
        LocalTime currentTime = time.toLocalTime();

        // 如果在下班时间之后，移到第二天上班时间
        if (currentTime.isAfter(workEnd)) {
            time = time.plusDays(1).with(workStart);
        }
        // 如果在上班时间之前，调整到上班时间
        else if (currentTime.isBefore(workStart)) {
            time = time.with(workStart);
        }

        return time;
    }

    private LocalDateTime calculateScheduleEnd(List<ScheduledTask> tasks) {
        return tasks.stream()
                .filter(ScheduledTask::isScheduled)
                .map(ScheduledTask::getEndTime)
                .max(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());
    }

    private String calculateScoreExplanation(List<ScheduledTask> tasks, List<WorkOrder> workOrders) {
        long scheduledCount = tasks.stream().filter(ScheduledTask::isScheduled).count();
        long totalOperations = tasks.size();
        double scheduleRate = totalOperations > 0 ? (double) scheduledCount / totalOperations * 100 : 0;

        return String.format("排程完成度: %.1f%%, 已排程: %d/%d 个工序", scheduleRate, scheduledCount, totalOperations);
    }
}
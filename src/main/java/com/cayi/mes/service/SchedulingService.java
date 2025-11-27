package com.cayi.mes.service;

import com.cayi.mes.aps.SimpleScheduler;
import com.cayi.mes.aps.domain.ScheduleSolution;
import com.cayi.mes.aps.domain.ScheduledTask;
import com.cayi.mes.entity.WorkOrder;
import com.cayi.mes.repository.WorkOrderRepository;
import com.cayi.mes.repository.WorkCenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SchedulingService {

    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Autowired
    private WorkCenterRepository workCenterRepository;

    @Autowired
    private SimpleScheduler simpleScheduler;

    /**
     * 执行自动排程
     */
    public ScheduleSolution performAutoScheduling(LocalDateTime scheduleStart) {
        // 获取所有待排程的工单
        List<WorkOrder> pendingWorkOrders = workOrderRepository
                .findByStatusOrderByPriorityAscDueDateAsc(WorkOrder.WorkOrderStatus.PENDING);

        if (pendingWorkOrders.isEmpty()) {
            ScheduleSolution emptySolution = new ScheduleSolution();
            emptySolution.setScoreExplanation("没有待排程的工单");
            return emptySolution;
        }

        // 执行排程
        ScheduleSolution solution = simpleScheduler.scheduleWorkOrders(pendingWorkOrders, scheduleStart);

        // 更新工单状态和排程时间
        updateWorkOrderSchedules(solution);

        return solution;
    }

    /**
     * 根据物料可用性检查排程可行性
     */
    public ScheduleSolution performSchedulingWithMaterialCheck(LocalDateTime scheduleStart) {
        // 这里可以添加物料可用性检查逻辑
        // 暂时先执行基本排程
        return performAutoScheduling(scheduleStart);
    }

    /**
     * 更新工单的排程信息
     */
    private void updateWorkOrderSchedules(ScheduleSolution solution) {
        for (ScheduledTask task : solution.getScheduledTasks()) {
            if (task.isScheduled()) {
                workOrderRepository.findById(task.getWorkOrderId()).ifPresent(workOrder -> {
                    // 如果是该工单的第一个工序，设置计划开始时间
                    if (workOrder.getPlannedStart() == null ||
                            task.getStartTime().isBefore(workOrder.getPlannedStart())) {
                        workOrder.setPlannedStart(task.getStartTime());
                    }

                    // 如果是该工单的最后一个工序，设置计划结束时间
                    if (workOrder.getPlannedEnd() == null ||
                            task.getEndTime().isAfter(workOrder.getPlannedEnd())) {
                        workOrder.setPlannedEnd(task.getEndTime());
                    }

                    workOrder.setStatus(WorkOrder.WorkOrderStatus.SCHEDULED);
                    workOrderRepository.save(workOrder);
                });
            }
        }
    }

    /**
     * 获取排程统计信息
     */
    public SchedulingStats getSchedulingStats() {
        long totalWorkOrders = workOrderRepository.count();
        long pendingCount = workOrderRepository.findByStatus(WorkOrder.WorkOrderStatus.PENDING).size();
        long scheduledCount = workOrderRepository.findByStatus(WorkOrder.WorkOrderStatus.SCHEDULED).size();
        long inProgressCount = workOrderRepository.findByStatus(WorkOrder.WorkOrderStatus.IN_PROGRESS).size();
        long completedCount = workOrderRepository.findByStatus(WorkOrder.WorkOrderStatus.COMPLETED).size();

        return new SchedulingStats(totalWorkOrders, pendingCount, scheduledCount, inProgressCount, completedCount);
    }

    // 排程统计类
    public static class SchedulingStats {
        private final long totalWorkOrders;
        private final long pendingCount;
        private final long scheduledCount;
        private final long inProgressCount;
        private final long completedCount;

        public SchedulingStats(long totalWorkOrders, long pendingCount, long scheduledCount,
                               long inProgressCount, long completedCount) {
            this.totalWorkOrders = totalWorkOrders;
            this.pendingCount = pendingCount;
            this.scheduledCount = scheduledCount;
            this.inProgressCount = inProgressCount;
            this.completedCount = completedCount;
        }

        // Getter方法
        public long getTotalWorkOrders() { return totalWorkOrders; }
        public long getPendingCount() { return pendingCount; }
        public long getScheduledCount() { return scheduledCount; }
        public long getInProgressCount() { return inProgressCount; }
        public long getCompletedCount() { return completedCount; }

        public double getScheduleRate() {
            return totalWorkOrders > 0 ? (double) (scheduledCount + inProgressCount + completedCount) / totalWorkOrders * 100 : 0;
        }
    }
}
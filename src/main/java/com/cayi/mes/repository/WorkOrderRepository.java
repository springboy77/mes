package com.cayi.mes.repository;

import com.cayi.mes.entity.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {
    Optional<WorkOrder> findByCode(String code);
    List<WorkOrder> findByStatus(WorkOrder.WorkOrderStatus status);
    List<WorkOrder> findByItemId(Long itemId);
    List<WorkOrder> findBySalesOrderId(Long salesOrderId);
    boolean existsByCode(String code);

    // 查找需要排程的工单（待排程状态）
    List<WorkOrder> findByStatusOrderByPriorityAscDueDateAsc(WorkOrder.WorkOrderStatus status);
}
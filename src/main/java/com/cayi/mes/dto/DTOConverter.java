package com.cayi.mes.dto;

import com.cayi.mes.entity.Operation;
import com.cayi.mes.entity.WorkOrder;
import java.math.BigDecimal;

public class DTOConverter {

    public static OperationDTO toOperationDTO(Operation operation) {
        if (operation == null) return null;

        OperationDTO dto = new OperationDTO();
        dto.setId(operation.getId());
        dto.setStep(operation.getStep());
        dto.setName(operation.getName());
        dto.setSetupTime(operation.getSetupTime());
        dto.setProcessTime(operation.getProcessTime());
        dto.setPreviousOperationIds(operation.getPreviousOperationIds());
        dto.setDescription(operation.getDescription());

        if (operation.getRouting() != null) {
            dto.setRoutingId(operation.getRouting().getId());
            dto.setRoutingVersion(operation.getRouting().getVersion());
        }
        if (operation.getWorkCenter() != null) {
            dto.setWorkCenterId(operation.getWorkCenter().getId());
            dto.setWorkCenterName(operation.getWorkCenter().getName());
        }

        return dto;
    }

    public static WorkOrderDTO toWorkOrderDTO(WorkOrder workOrder) {
        if (workOrder == null) return null;

        WorkOrderDTO dto = new WorkOrderDTO();
        dto.setId(workOrder.getId());
        dto.setCode(workOrder.getCode());
        dto.setQtyToProduce(workOrder.getQtyToProduce());
        dto.setStatus(workOrder.getStatus());
        dto.setDueDate(workOrder.getDueDate());
        dto.setPlannedStart(workOrder.getPlannedStart());
        dto.setPlannedEnd(workOrder.getPlannedEnd());
        dto.setActualStart(workOrder.getActualStart());
        dto.setActualEnd(workOrder.getActualEnd());
        dto.setPriority(workOrder.getPriority());
        dto.setDescription(workOrder.getDescription());
        dto.setCreatedTime(workOrder.getCreatedTime());
        dto.setUpdatedTime(workOrder.getUpdatedTime());

        // 处理销售订单信息
        if (workOrder.getSalesOrder() != null) {
            dto.setSalesOrderId(workOrder.getSalesOrder().getId());
            dto.setSalesOrderNumber(workOrder.getSalesOrder().getOrderNumber());
        }

        // 处理物料信息
        if (workOrder.getItem() != null) {
            dto.setItemId(workOrder.getItem().getId());
            dto.setItemName(workOrder.getItem().getName());
            dto.setItemCode(workOrder.getItem().getCode());
        }

        // 处理工艺路线信息
        if (workOrder.getRouting() != null) {
            dto.setRoutingId(workOrder.getRouting().getId());
            dto.setRoutingVersion(workOrder.getRouting().getVersion());
        }

        return dto;
    }
}
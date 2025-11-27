package com.cayi.mes.controller;

import com.cayi.mes.entity.Operation;
import com.cayi.mes.entity.Routing;
import com.cayi.mes.entity.WorkCenter;
import com.cayi.mes.repository.OperationRepository;
import com.cayi.mes.repository.RoutingRepository;
import com.cayi.mes.repository.WorkCenterRepository;
import com.cayi.mes.dto.OperationDTO;
import com.cayi.mes.dto.DTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/operations")
public class OperationController {
    @Autowired
    private OperationRepository operationRepository;
    @Autowired
    private RoutingRepository routingRepository;
    @Autowired
    private WorkCenterRepository workCenterRepository;

    @GetMapping
    public List<OperationDTO> getAllOperations() {
        List<Operation> operations = operationRepository.findAll();
        return operations.stream()
                .map(DTOConverter::toOperationDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OperationDTO> getOperationById(@PathVariable Long id) {
        Optional<Operation> operation = operationRepository.findById(id);
        return operation.map(op -> ResponseEntity.ok(DTOConverter.toOperationDTO(op)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/routing/{routingId}")
    public List<OperationDTO> getOperationsByRoutingId(@PathVariable Long routingId) {
        List<Operation> operations = operationRepository.findByRoutingIdOrderByStep(routingId);
        return operations.stream()
                .map(DTOConverter::toOperationDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/work-center/{workCenterId}")
    public List<OperationDTO> getOperationsByWorkCenterId(@PathVariable Long workCenterId) {
        List<Operation> operations = operationRepository.findByWorkCenterId(workCenterId);
        return operations.stream()
                .map(DTOConverter::toOperationDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<?> createOperation(@RequestBody OperationRequest request) {
        try {
            if (request.getRoutingId() == null) return ResponseEntity.badRequest().body("工艺路线ID不能为空");
            if (request.getWorkCenterId() == null) return ResponseEntity.badRequest().body("设备ID不能为空");
            if (request.getStep() == null) return ResponseEntity.badRequest().body("工序步骤不能为空");
            if (request.getName() == null || request.getName().trim().isEmpty()) return ResponseEntity.badRequest().body("工序名称不能为空");
            if (request.getProcessTime() == null) return ResponseEntity.badRequest().body("加工时间不能为空");

            Optional<Routing> routing = routingRepository.findById(request.getRoutingId());
            Optional<WorkCenter> workCenter = workCenterRepository.findById(request.getWorkCenterId());

            if (routing.isEmpty()) return ResponseEntity.badRequest().body("工艺路线不存在");
            if (workCenter.isEmpty()) return ResponseEntity.badRequest().body("设备不存在");

            Operation operation = new Operation();
            operation.setRouting(routing.get());
            operation.setWorkCenter(workCenter.get());
            operation.setStep(request.getStep());
            operation.setName(request.getName());
            operation.setSetupTime(request.getSetupTime() != null ? request.getSetupTime() : 0);
            operation.setProcessTime(request.getProcessTime());
            operation.setPreviousOperationIds(request.getPreviousOperationIds());
            operation.setDescription(request.getDescription());

            Operation savedOperation = operationRepository.save(operation);
            return ResponseEntity.ok(DTOConverter.toOperationDTO(savedOperation));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("创建失败: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOperation(@PathVariable Long id, @RequestBody OperationRequest request) {
        try {
            Optional<Operation> optionalOperation = operationRepository.findById(id);
            if (optionalOperation.isEmpty()) return ResponseEntity.notFound().build();

            Operation operation = optionalOperation.get();
            operation.setStep(request.getStep());
            operation.setName(request.getName());
            operation.setSetupTime(request.getSetupTime());
            operation.setProcessTime(request.getProcessTime());
            operation.setPreviousOperationIds(request.getPreviousOperationIds());
            operation.setDescription(request.getDescription());

            if (request.getWorkCenterId() != null) {
                Optional<WorkCenter> workCenter = workCenterRepository.findById(request.getWorkCenterId());
                workCenter.ifPresent(operation::setWorkCenter);
            }

            Operation updatedOperation = operationRepository.save(operation);
            return ResponseEntity.ok(DTOConverter.toOperationDTO(updatedOperation));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("更新失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOperation(@PathVariable Long id) {
        if (operationRepository.existsById(id)) {
            operationRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    public static class OperationRequest {
        private Long routingId;
        private Long workCenterId;
        private Integer step;
        private String name;
        private Integer setupTime = 0;
        private Integer processTime;
        private String previousOperationIds;
        private String description;

        public Long getRoutingId() { return routingId; }
        public void setRoutingId(Long routingId) { this.routingId = routingId; }
        public Long getWorkCenterId() { return workCenterId; }
        public void setWorkCenterId(Long workCenterId) { this.workCenterId = workCenterId; }
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
}
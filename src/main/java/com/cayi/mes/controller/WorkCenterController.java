package com.cayi.mes.controller;

import com.cayi.mes.entity.WorkCenter;
import com.cayi.mes.repository.WorkCenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/work-centers")
public class WorkCenterController {
    @Autowired
    private WorkCenterRepository workCenterRepository;

    @GetMapping
    public List<WorkCenter> getAllWorkCenters() {
        return workCenterRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkCenter> getWorkCenterById(@PathVariable Long id) {
        Optional<WorkCenter> workCenter = workCenterRepository.findById(id);
        return workCenter.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public WorkCenter createWorkCenter(@RequestBody WorkCenter workCenter) {
        return workCenterRepository.save(workCenter);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkCenter> updateWorkCenter(@PathVariable Long id, @RequestBody WorkCenter workCenterDetails) {
        Optional<WorkCenter> optionalWorkCenter = workCenterRepository.findById(id);
        if (optionalWorkCenter.isPresent()) {
            WorkCenter workCenter = optionalWorkCenter.get();
            workCenter.setCode(workCenterDetails.getCode());
            workCenter.setName(workCenterDetails.getName());
            workCenter.setEfficiency(workCenterDetails.getEfficiency());
            workCenter.setAvailableStart(workCenterDetails.getAvailableStart());
            workCenter.setAvailableEnd(workCenterDetails.getAvailableEnd());
            workCenter.setActive(workCenterDetails.getActive());
            return ResponseEntity.ok(workCenterRepository.save(workCenter));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkCenter(@PathVariable Long id) {
        if (workCenterRepository.existsById(id)) {
            workCenterRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/active")
    public List<WorkCenter> getActiveWorkCenters() {
        return workCenterRepository.findByActiveTrue();
    }
}
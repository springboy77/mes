package com.cayi.mes.controller;

import com.cayi.mes.aps.domain.ScheduleSolution;
import com.cayi.mes.service.SchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/scheduling")
public class SchedulingController {

    @Autowired
    private SchedulingService schedulingService;

    /**
     * 执行自动排程
     */
    @PostMapping("/auto-schedule")
    public ResponseEntity<ScheduleSolution> performAutoScheduling(@RequestBody SchedulingRequest request) {
        try {
            LocalDateTime scheduleStart = request.getScheduleStart() != null ?
                    request.getScheduleStart() : LocalDateTime.now();

            ScheduleSolution solution = schedulingService.performAutoScheduling(scheduleStart);
            return ResponseEntity.ok(solution);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 带物料检查的排程
     */
    @PostMapping("/schedule-with-material-check")
    public ResponseEntity<ScheduleSolution> performSchedulingWithMaterialCheck(@RequestBody SchedulingRequest request) {
        try {
            LocalDateTime scheduleStart = request.getScheduleStart() != null ?
                    request.getScheduleStart() : LocalDateTime.now();

            ScheduleSolution solution = schedulingService.performSchedulingWithMaterialCheck(scheduleStart);
            return ResponseEntity.ok(solution);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 获取排程统计信息
     */
    @GetMapping("/stats")
    public ResponseEntity<SchedulingService.SchedulingStats> getSchedulingStats() {
        try {
            SchedulingService.SchedulingStats stats = schedulingService.getSchedulingStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 清除所有排程（重置为待排程状态）
     */
    @PostMapping("/clear-all")
    public ResponseEntity<String> clearAllSchedules() {
        try {
            // 这里可以实现清除排程的逻辑
            // 暂时返回成功消息
            return ResponseEntity.ok("排程清除功能待实现");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("清除失败: " + e.getMessage());
        }
    }

    // 请求DTO类
    public static class SchedulingRequest {
        private LocalDateTime scheduleStart;

        public LocalDateTime getScheduleStart() { return scheduleStart; }
        public void setScheduleStart(LocalDateTime scheduleStart) { this.scheduleStart = scheduleStart; }
    }
}
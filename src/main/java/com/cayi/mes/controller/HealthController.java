package com.cayi.mes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.RuntimeMXBean;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public Map<String, Object> health() {
        Map<String, Object> healthInfo = new HashMap<>();
        healthInfo.put("status", "UP");
        healthInfo.put("timestamp", java.time.LocalDateTime.now().toString());
        healthInfo.put("database", checkDatabaseHealth());
        healthInfo.put("application", "MES System");
        healthInfo.put("version", "1.0.0");
        return healthInfo;
    }

    @GetMapping("/liveness")
    public Map<String, String> liveness() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("component", "Liveness Probe");
        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return response;
    }

    @GetMapping("/readiness")
    public Map<String, String> readiness() {
        Map<String, String> response = new HashMap<>();

        // 检查数据库连接
        boolean dbHealthy = checkDatabaseHealth().equals("HEALTHY");

        if (dbHealthy) {
            response.put("status", "UP");
            response.put("database", "CONNECTED");
            response.put("message", "Application is ready to handle requests");
        } else {
            response.put("status", "DOWN");
            response.put("database", "DISCONNECTED");
            response.put("message", "Application is not ready");
        }

        response.put("timestamp", java.time.LocalDateTime.now().toString());
        return response;
    }

    @GetMapping("/database")
    public Map<String, Object> databaseHealth() {
        Map<String, Object> dbHealth = new HashMap<>();
        try {
            String result = jdbcTemplate.queryForObject("SELECT 1", String.class);
            dbHealth.put("status", "HEALTHY");
            dbHealth.put("response", result);
            dbHealth.put("timestamp", java.time.LocalDateTime.now().toString());
        } catch (Exception e) {
            dbHealth.put("status", "UNHEALTHY");
            dbHealth.put("error", e.getMessage());
            dbHealth.put("timestamp", java.time.LocalDateTime.now().toString());
        }
        return dbHealth;
    }

    @GetMapping("/metrics")
    public Map<String, Object> metrics() {
        Map<String, Object> metrics = new HashMap<>();

        // JVM内存信息
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        metrics.put("jvm.heap.used", memoryBean.getHeapMemoryUsage().getUsed());
        metrics.put("jvm.heap.max", memoryBean.getHeapMemoryUsage().getMax());
        metrics.put("jvm.nonheap.used", memoryBean.getNonHeapMemoryUsage().getUsed());

        // 运行时信息
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        metrics.put("jvm.uptime", runtimeBean.getUptime());
        metrics.put("jvm.starttime", runtimeBean.getStartTime());

        // 系统信息
        Runtime runtime = Runtime.getRuntime();
        metrics.put("system.cpu.cores", runtime.availableProcessors());
        metrics.put("system.memory.free", runtime.freeMemory());
        metrics.put("system.memory.total", runtime.totalMemory());
        metrics.put("system.memory.max", runtime.maxMemory());

        // 数据库连接信息
        try {
            Integer connectionCount = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM information_schema.PROCESSLIST WHERE DB = 'mes_system'",
                    Integer.class
            );
            metrics.put("database.connections", connectionCount);
        } catch (Exception e) {
            metrics.put("database.connections", "N/A");
        }

        metrics.put("timestamp", java.time.LocalDateTime.now().toString());
        return metrics;
    }

    @GetMapping("/info")
    public Map<String, Object> info() {
        Map<String, Object> info = new HashMap<>();
        info.put("application", "MES System with APS");
        info.put("version", "1.0.0");
        info.put("company", "cayi");
        info.put("description", "Manufacturing Execution System with Auto Planning and Scheduling");
        info.put("environment", "Production");
        info.put("timestamp", java.time.LocalDateTime.now().toString());
        return info;
    }

    private String checkDatabaseHealth() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return "HEALTHY";
        } catch (Exception e) {
            return "UNHEALTHY";
        }
    }
}
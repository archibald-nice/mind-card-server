package com.archie.mind_card_server.controller;

import com.archie.mind_card_server.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/health")
public class HealthController {
    
    @Value("${spring.application.name}")
    private String applicationName;
    
    @Value("${server.port}")
    private String serverPort;
    
    /**
     * 基础健康检查
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> health() {
        Map<String, Object> healthInfo = new HashMap<>();
        healthInfo.put("status", "UP");
        healthInfo.put("application", applicationName);
        healthInfo.put("port", serverPort);
        healthInfo.put("timestamp", LocalDateTime.now());
        healthInfo.put("message", "思维卡片服务运行正常");
        
        ApiResponse<Map<String, Object>> response = ApiResponse.success(healthInfo);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 详细健康检查
     */
    @GetMapping("/detailed")
    public ResponseEntity<ApiResponse<Map<String, Object>>> detailedHealth() {
        Map<String, Object> healthInfo = new HashMap<>();
        
        // 基本信息
        healthInfo.put("status", "UP");
        healthInfo.put("application", applicationName);
        healthInfo.put("port", serverPort);
        healthInfo.put("timestamp", LocalDateTime.now());
        
        // 系统信息
        Runtime runtime = Runtime.getRuntime();
        Map<String, Object> systemInfo = new HashMap<>();
        systemInfo.put("availableProcessors", runtime.availableProcessors());
        systemInfo.put("freeMemory", runtime.freeMemory() / 1024 / 1024 + " MB");
        systemInfo.put("totalMemory", runtime.totalMemory() / 1024 / 1024 + " MB");
        systemInfo.put("maxMemory", runtime.maxMemory() / 1024 / 1024 + " MB");
        
        healthInfo.put("system", systemInfo);
        
        // Java信息
        Map<String, Object> javaInfo = new HashMap<>();
        javaInfo.put("version", System.getProperty("java.version"));
        javaInfo.put("vendor", System.getProperty("java.vendor"));
        javaInfo.put("home", System.getProperty("java.home"));
        
        healthInfo.put("java", javaInfo);
        
        ApiResponse<Map<String, Object>> response = ApiResponse.success(healthInfo);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 版本信息
     */
    @GetMapping("/version")
    public ResponseEntity<ApiResponse<Map<String, String>>> version() {
        Map<String, String> versionInfo = new HashMap<>();
        versionInfo.put("application", applicationName);
        versionInfo.put("version", "1.0.0");
        versionInfo.put("buildTime", LocalDateTime.now().toString());
        versionInfo.put("description", "思维卡片管理系统");
        
        ApiResponse<Map<String, String>> response = ApiResponse.success(versionInfo);
        return ResponseEntity.ok(response);
    }
}

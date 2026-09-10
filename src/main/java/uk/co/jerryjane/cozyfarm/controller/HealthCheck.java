package uk.co.jerryjane.cozyfarm.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/api/health")
public class HealthCheck {

    @GetMapping
    public ResponseEntity<Map<String,String>> health() {
        return ResponseEntity.ok().body(Map.of("status", "ok"));
    }

}

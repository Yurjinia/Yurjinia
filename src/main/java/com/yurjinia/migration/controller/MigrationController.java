package com.yurjinia.migration.controller;

import com.yurjinia.migration.service.MigrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/data")
@RequiredArgsConstructor
public class MigrationController {

    private final MigrationService migrationService;

    @PostMapping("/createInitialData")
    public ResponseEntity<Void> createInitialData() {
        migrationService.createInitialData();
        return ResponseEntity.ok().build();
    }

}

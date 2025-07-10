package com.senolkacar.sqltrainer.controller;

import com.senolkacar.sqltrainer.dto.DatabaseDTO;
import com.senolkacar.sqltrainer.service.DatabaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/database")
public class DatabaseController {

    private final DatabaseService databaseService;

    @Autowired
    public DatabaseController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @PreAuthorize("hasRole('TEACHER')")
    @GetMapping
    public ResponseEntity<List<DatabaseDTO>> getAll() {
        List<DatabaseDTO> databases = databaseService.getAllDatabases();
        return ResponseEntity.ok(databases);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/gettables")
    public ResponseEntity<List<String>> getTables(@RequestBody DatabaseDTO databaseDTO) {
        List<String> tables = databaseService.getTables(databaseDTO.getName());
        return ResponseEntity.ok(tables);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/getcolumns")
    public ResponseEntity<List<String>> getColumns(@RequestBody DatabaseDTO databaseDTO) {
        List<String> columns = databaseService.getColumns(databaseDTO.getName());
        return ResponseEntity.ok(columns);
    }
}
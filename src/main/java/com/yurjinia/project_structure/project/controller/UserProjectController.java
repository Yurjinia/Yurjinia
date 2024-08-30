package com.yurjinia.project_structure.project.controller;


import com.yurjinia.project_structure.project.dto.CreateProjectRequest;
import com.yurjinia.project_structure.project.dto.ProjectDTO;
import com.yurjinia.project_structure.project.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/{userEmail}/projects")
public class UserProjectController {

    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getUserProjects(@PathVariable String userEmail) {
        List<ProjectDTO> projects = projectService.getUserProjects(userEmail);
        return ResponseEntity.ok(projects);
    }

    @PostMapping
    public ResponseEntity<Void> createProject(@PathVariable String userEmail,
                                              @Valid @RequestBody CreateProjectRequest createProjectRequest) {
        projectService.createProject(userEmail, createProjectRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}

package com.yurjinia.user.controller;

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
@RequestMapping("/api/v1/users/{userEmail}")
public class UserController {

    private final ProjectService projectService;

    @GetMapping("/projects")
    public ResponseEntity<List<ProjectDTO>> getUserProjects(@PathVariable String userEmail) {
        List<ProjectDTO> projects = projectService.getUserProjects(userEmail);
        return ResponseEntity.ok(projects);
    }

    @PostMapping("/projects")
    public ResponseEntity<Void> createProject(@PathVariable String userEmail,
                                              @Valid @RequestBody CreateProjectRequest createProjectRequest) {
        projectService.createProject(userEmail, createProjectRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}

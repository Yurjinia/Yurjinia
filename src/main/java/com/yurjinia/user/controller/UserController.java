package com.yurjinia.user.controller;

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
@RequestMapping("/api/v1/users")
public class UserController {

    private final ProjectService projectService;

    @GetMapping("/{userEmail}/projects")
    public ResponseEntity<List<ProjectDTO>> getUserProjects(@PathVariable String userEmail) {
        List<ProjectDTO> projects = projectService.getUserProjects(userEmail);
        return ResponseEntity.ok(projects);
    }

    @PostMapping("/{userEmail}/projects")
    public ResponseEntity<Void> createProject(@PathVariable String userEmail,
                                              @Valid @RequestBody ProjectDTO projectDTO) {
        projectService.createProject(userEmail, projectDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}

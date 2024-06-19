package com.yurjinia.project_structure.project.controller;

import com.yurjinia.project_structure.project.dto.ProjectDTO;
import com.yurjinia.project_structure.project.dto.ProjectInvitationDTO;
import com.yurjinia.project_structure.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/projects/{userEmail}")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ProjectDTO createProject(@PathVariable String userEmail, @RequestBody ProjectDTO projectDTO) {
        return projectService.createProject(userEmail, projectDTO);
    }

    @PostMapping("{projectName}/invite")
    public void inviteUserToTheProject(@PathVariable String projectName,
                                       @RequestBody ProjectInvitationDTO projectInvitationDTO) {
        projectService.inviteUserToTheProject(projectName, projectInvitationDTO);
    }

    @GetMapping("/confirm")
    public String confirmInvite(@RequestParam("token") String token) {
        projectService.confirmInvite(token);

        return "User added to the project";
    }

}

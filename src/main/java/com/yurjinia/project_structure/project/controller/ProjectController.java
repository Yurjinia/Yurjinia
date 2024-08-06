package com.yurjinia.project_structure.project.controller;

import com.yurjinia.project_structure.project.dto.ProjectDTO;
import com.yurjinia.project_structure.project.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/{userEmail}")
    public ResponseEntity<Void> createProject(@PathVariable String userEmail, @Valid @RequestBody ProjectDTO projectDTO) {
        projectService.createProject(userEmail, projectDTO);

        return ResponseEntity.noContent().build();
    }

    /* ToDo: Refer to next JIRA with having more clarification about the reasons of
        why the code was commented, and when it's going to be uncommented:
        https://pashka1clash.atlassian.net/browse/YUR-114

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

     */
}

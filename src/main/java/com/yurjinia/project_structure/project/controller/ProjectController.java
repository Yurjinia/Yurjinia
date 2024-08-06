package com.yurjinia.project_structure.project.controller;

import com.yurjinia.project_structure.project.dto.InviteToProjectRequest;
import com.yurjinia.project_structure.project.dto.ProjectDTO;
import com.yurjinia.project_structure.project.dto.UpdateProjectRequest;
import com.yurjinia.project_structure.project.service.ProjectService;
import com.yurjinia.user.dto.UserDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/{projectCode}/users")
    public ResponseEntity<List<UserDTO>> getProjectUsers(@PathVariable String projectCode) {
        List<UserDTO> users = projectService.getProjectUsers(projectCode);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{projectCode}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable String projectCode,
                                                    @Valid @RequestBody UpdateProjectRequest updateProjectRequest) {
        ProjectDTO updatedProject = projectService.updateProject(projectCode, updateProjectRequest);
        return ResponseEntity.ok(updatedProject);
    }

    @PostMapping("/{projectCode}/invite")
    public ResponseEntity<Void> inviteUsers(@PathVariable String projectCode,
                                            @Valid @RequestBody InviteToProjectRequest inviteToProjectRequest) {
        projectService.inviteUsers(projectCode, inviteToProjectRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{projectCode}")
    public ResponseEntity<Void> deleteProject(@PathVariable String projectCode) {
        projectService.deleteProject(projectCode);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{projectCode}/users/{userEmail}")
    public ResponseEntity<Void> deleteUserFromProject(@PathVariable String projectCode, @PathVariable String userEmail) {
        projectService.deleteUserFromProject(projectCode, userEmail);
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

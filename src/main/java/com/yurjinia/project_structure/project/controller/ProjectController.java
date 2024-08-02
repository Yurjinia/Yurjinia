package com.yurjinia.project_structure.project.controller;

import com.yurjinia.project_structure.project.dto.ProjectDTO;
import com.yurjinia.project_structure.project.service.ProjectService;
import com.yurjinia.user.dto.UserDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/{projectCode}/users")
    public ResponseEntity<List<UserDTO>> getProjectUsers(@PathVariable String projectCode) {
        List<UserDTO> users = projectService.getProjectUsers(projectCode);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/{projectCode}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable String projectCode, @Valid @RequestBody ProjectDTO projectDTO) {
        ProjectDTO updatedProject = projectService.
                updateProject(projectCode, projectDTO);
        return new ResponseEntity<>(updatedProject, HttpStatus.OK);
    }

//    @DeleteMapping("/{projectCode}")
//    public ResponseEntity<Void> deleteProject(@PathVariable String projectCode) {
//        projectService.deleteProject(projectCode);
//        return ResponseEntity.noContent().build(); // Повертає статус 204 No Content
//    }

/*

    @PutMapping("/projects/{projectCode}")
    public ResponseEntity<ProjectDTO> updateProject(
            @PathVariable String projectCode,
            @RequestBody @Valid UpdateProjectRequest updateProjectRequest) {
        ProjectDTO updatedProject = projectService.updateProject(projectCode, updateProjectRequest);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/projects/{projectCode}")
    public ResponseEntity<Void> deleteProject(@PathVariable String projectCode) {
        projectService.deleteProject(projectCode);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/projects/{projectCode}/users/{userEmail}")
    public ResponseEntity<Void> removeUserFromProject(
            @PathVariable String projectCode,
            @PathVariable String userEmail) {
        projectService.removeUserFromProject(projectCode, userEmail);
        return ResponseEntity.noContent().build();
    }
*/
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

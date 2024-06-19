package com.yurjinia.project_structure.project.service;

import com.yurjinia.common.emailSender.EmailSender;
import com.yurjinia.common.exception.CommonException;
import com.yurjinia.common.exception.ErrorCode;
import com.yurjinia.project_structure.project.confirmationToken.entity.ConfirmationTokenEntity;
import com.yurjinia.project_structure.project.confirmationToken.service.ConfirmationTokenService;
import com.yurjinia.project_structure.project.dto.ProjectDTO;
import com.yurjinia.project_structure.project.dto.ProjectInvitationDTO;
import com.yurjinia.project_structure.project.entity.ProjectEntity;
import com.yurjinia.project_structure.project.repository.ProjectRepository;
import com.yurjinia.project_structure.project.service.mapper.ProjectMapper;
import com.yurjinia.user.entity.UserEntity;
import com.yurjinia.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final EmailSender emailSender;
    private final UserService userService;
    private final ProjectMapper projectMapper;
    private final ProjectRepository projectRepository;
    private final ConfirmationTokenService confirmationTokenService;

    @Transactional
    public ProjectDTO createProject(String userEmail, ProjectDTO projectDTO) {
        validateIfProjectExists(projectDTO);
        userService.validateIfUsersExists(projectDTO.getUsers().stream().toList());

        ProjectEntity projectEntity = projectMapper.toEntity(projectDTO);
        UserEntity userEntityList = userService.getByEmail(userEmail);
        projectEntity.setUsers(List.of(userEntityList));
        projectRepository.save(projectEntity);

        userService.addProject(projectEntity);

        inviteUsers(projectDTO);

        return projectDTO;
    }

    private void inviteUsers(ProjectDTO projectDTO) {
        Set<String> users = projectDTO.getUsers();
        String projectName = projectDTO.getName();

        users.forEach(user -> {
            inviteUserToTheProject(projectName, ProjectInvitationDTO.builder().email(user).build());
        });
    }

    public void inviteUserToTheProject(String projectName, ProjectInvitationDTO projectInvitationDTO) {
        validateIfProjectNotExists(projectName);
        validateIfUserExistsInProject(projectInvitationDTO.getEmail(), projectName);

        String token = confirmationTokenService.createToken(projectInvitationDTO.getEmail(), projectName);
        String link = "http://localhost:9000/api/v1/projects/confirm?token=" + token;//ToDo: Resolve the security breach (MVP 1.2)
        emailSender.send(projectInvitationDTO.getEmail(), buildInvitationMessage(link));
    }

    @Transactional
    public void addUserToProject(String email, String projectName) {
        ProjectEntity projectEntity = projectRepository.findProjectEntityByName(projectName)
                .orElseThrow(() -> new CommonException(ErrorCode.PROJECT_NOT_FOUND, HttpStatus.NOT_FOUND));
        UserEntity userEntity = userService.getByEmail(email);

        projectEntity.getUsers().add(userEntity);
        userEntity.getProjects().add(projectEntity);

        projectRepository.save(projectEntity);
        userService.save(userEntity);
    }

    @Transactional
    public void confirmInvite(String token) {
        ConfirmationTokenEntity confirmationToken = confirmationTokenService.getToken(token);

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Token expired");
        }

        addUserToProject(confirmationToken.getUserEmail(), confirmationToken.getProjectName());
        confirmationTokenService.deleteToken(token);
    }

    private void validateIfProjectExists(ProjectDTO projectDTO) {
        if (projectRepository.existsByName(projectDTO.getName())) {
            throw new CommonException(ErrorCode.PROJECT_ALREADY_EXISTS, HttpStatus.CONFLICT, List.of("Project by name " + projectDTO.getName() + " already exists"));
        }
    }

    private void validateIfProjectNotExists(String projectName) {
        if (!projectRepository.existsByName(projectName)) {
            throw new CommonException(ErrorCode.PROJECT_NOT_FOUND, HttpStatus.NOT_FOUND, List.of("Project by name " + projectName + " already exists"));
        }
    }

    private void validateIfUserExistsInProject(String email, String projectName) {
        ProjectEntity projectEntity = projectRepository.findProjectEntityByName(projectName).
                orElseThrow(() -> new CommonException(ErrorCode.PROJECT_NOT_FOUND, HttpStatus.NOT_FOUND));

        List<String> emails = projectEntity.getUsers().stream().map(UserEntity::getEmail).toList();
        if (emails.contains(email)) {
            throw new CommonException(ErrorCode.USER_ALREADY_IN_PROJECT, HttpStatus.CONFLICT);
        }

    }

    private String buildInvitationMessage(String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Join to the project</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Someone wants you to join the project. Please click on the below link to join it: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Join Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

}

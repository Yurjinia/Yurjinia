package com.yurjinia.platform.common.emailSender.service;

import com.yurjinia.platform.common.emailSender.EmailSender;
import com.yurjinia.platform.common.exception.CommonException;
import com.yurjinia.platform.common.exception.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.yurjinia.platform.common.emailSender.constants.EmailSenderConstants.*;

@Service
@RequiredArgsConstructor
public class EmailService implements EmailSender {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String emailOfSender;

    @Override
    @Async
    public void send(String to, String email) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");

            mimeMessageHelper.setText(email, true);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject("Confirm your email");
            mimeMessageHelper.setFrom(emailOfSender);

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new IllegalStateException("Failed to send email", e);
        }
    }

    public String buildInvitationMessage(String link) {
        return loadHtmlTemplate(link, INVITATION_TEMPLATE);
    }

    public String buildForgotPasswordSuccessMessage(String link) {
        return loadHtmlTemplate(link, FORGOT_PASSWORD_SUCCESS_TEMPLATE);
    }

    public String buildForgotPasswordMessage(String link) {
        return loadHtmlTemplate(link, FORGOT_PASSWORD_TEMPLATE);
    }

    public String buildConfirmationEmailMessage(String link) {
        return loadHtmlTemplate(link, CONFIRMATION_EMAIL_TEMPLATE);
    }

    private String loadHtmlTemplate(String link, String fileTemplate) {
        String template;

        try {
            URL resource = getClass().getClassLoader().getResource(fileTemplate);

            if (resource != null) {
                template = new String(Files.readAllBytes(Paths.get(resource.toURI())));
            } else {
                throw new CommonException(ErrorCode.TEMPLATE_FILE_NOT_FOUND, HttpStatus.NOT_FOUND);
            }

        } catch (IOException | URISyntaxException e) {
            throw new CommonException(ErrorCode.BUILD_EMAIL_ERROR, HttpStatus.NOT_IMPLEMENTED);
        }

        return template.replace("${link}", link);
    }

}

package com.global.library.util.mail;

import com.global.library.api.utils.IEmailSendler;
import com.global.library.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class EmailSendler implements IEmailSendler {

    private static final String EMAIL_ADDRESS = "L3v1strauss@gmail.com";

    private final JavaMailSender emailSender;

    @Autowired
    public EmailSendler(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    @Override
    public void sendSimpleMessage(
            String to) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("L3v1strauss@gmail.com");
        message.setTo(to);
        message.setSubject("Babe pass me hookah");
        message.setText("You wanna hookah?");
        emailSender.send(message);

    }

    @Override
    public void sendMessageWithActivationInstruction(User user, String subject) {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("userId", user.getId());
        templateModel.put("firstName", user.getFirstName());
        templateModel.put("lastName", user.getLastName());
        templateModel.put("adminEmail", EMAIL_ADDRESS);
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);
        String htmlBody = springTemplateEngine.process("mailMessageTemplate.html", thymeleafContext);

        try {
            sendHtmlMessage(user.getEmail(), subject, htmlBody);
        } catch (MessagingException e) {
            log.error("Failed to send message. Error message: {}", e.getMessage());
        }
    }

    @Override
    public void sendMessageWithNewPassword(User user, String subject, String newPassword) {
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("password", newPassword);
        templateModel.put("firstName", user.getFirstName());
        templateModel.put("lastName", user.getLastName());
        templateModel.put("adminEmail", EMAIL_ADDRESS);
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(templateModel);
        String htmlBody = springTemplateEngine.process("mailMessageNewPassword.html", thymeleafContext);
        try {
            sendHtmlMessage(user.getEmail(), subject, htmlBody);
        } catch (MessagingException e) {
            log.error("Failed to send message. Error message: {}", e.getMessage());
        }
    }

    private void sendHtmlMessage(String to, String subject, String htmlBody) throws MessagingException {

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        helper.setFrom(EMAIL_ADDRESS);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
//        helper.addInline("logo", resourceFile);
        emailSender.send(message);
    }
}

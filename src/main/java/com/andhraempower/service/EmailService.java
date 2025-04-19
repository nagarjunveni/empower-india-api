package com.andhraempower.service;

import com.andhraempower.entity.User;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDate;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendEmail(User user, String randomPassword) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(user.getEmail());
            helper.setSubject("Team Empower Andhra: Password Reset Request");

            Context context = new Context();
            context.setVariable("userFirstName", user.getFirstName());
            context.setVariable("userPassword", randomPassword);
            context.setVariable("year", LocalDate.now().getYear());

            String emailBody = templateEngine.process("passwordRecoveryTemplate", context);

            helper.setText(emailBody, true);

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

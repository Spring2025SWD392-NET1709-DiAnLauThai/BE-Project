package com.be.back_end.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
public class EmailService implements IEmailService{

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendOtpEmail(String to, String name, String otp, String token) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("otp", otp);
        context.setVariable("token", token);

        String htmlContent = templateEngine.process("otp-verification", context);

        helper.setTo(to);
        helper.setSubject("Your OTP Code");
        helper.setText(htmlContent, true);

        try {
            mailSender.send(message);
            System.out.println("Email sent successfully.");
        } catch (Exception e) {
            e.printStackTrace(); // Print the full error
            System.out.println("Error sending email: " + e.getMessage());
            throw new MessagingException("Error sending OTP email", e); // Rethrow the exception
        }

    }

    @Async
    @Override
    public void sendAssignmentEmail(String to, String name, String bookingCode) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("bookingCode", bookingCode);
        String htmlContent = templateEngine.process("task-assignment", context);
        helper.setTo(to);
        helper.setSubject("Task Assignment Notification");
        helper.setText(htmlContent, true);
        try {
            mailSender.send(message);
            System.out.println("Assignment email sent successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new MessagingException("Error sending assignment email", e);
        }
    }
    @Async
    @Override
    public void sendDesignerEmail(String to, String designerName) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        Context context = new Context();
        context.setVariable("designerName", designerName);
        String htmlContent = templateEngine.process("task-assignment", context);
        helper.setTo(to);
        helper.setSubject("New Modification Request");
        helper.setText(htmlContent, true);
        try {
            mailSender.send(message);
            System.out.println("Designer announcement email sent successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new MessagingException("Error sending designer announcement email", e);
        }
    }


    @Override
    public void sendPasswordEmail(String to, String name, String password) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("password", password);

        String htmlContent = templateEngine.process("password-notification", context);

        helper.setTo(to);
        helper.setSubject("Your Account Password");
        helper.setText(htmlContent, true);

        try {
            mailSender.send(message);
            System.out.println("Password email sent successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error sending password email: " + e.getMessage());
            throw new MessagingException("Error sending password email", e);
        }
    }


}

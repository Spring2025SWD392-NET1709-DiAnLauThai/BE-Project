package com.be.back_end.service.EmailService;

import jakarta.mail.MessagingException;

public interface IEmailService {
     void sendOtpEmail(String to, String name, String otp, String token) throws MessagingException;
}

package com.be.back_end.service.EmailService;

import com.be.back_end.dto.EmailDTO;

import jakarta.mail.MessagingException;

public interface IEmailService {
     void sendOtpEmail(String to, String name, String otp, String token) throws MessagingException;
     void sendPasswordEmail(String to, String name, String password) throws MessagingException;
     void sendAssignmentEmail(String to, String name, String bookingCode) throws MessagingException;
}

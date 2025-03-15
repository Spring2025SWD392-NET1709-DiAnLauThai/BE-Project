package com.be.back_end.service.EmailService;

import jakarta.mail.MessagingException;

public interface IEmailService {
     void sendOtpEmail(String to, String name, String otp, String token) throws MessagingException;
     void sendPasswordEmail(String to, String name, String password) throws MessagingException;
     void sendAssignmentEmail(String to, String name, String bookingCode) throws MessagingException;
     void sendDesignerEmail(String to, String designerName, String bookingCode, String oldDescription, String newDescription) throws MessagingException ;
     boolean sendCustomerCompleteEmail(String to, String customerName, String bookingCode);
}

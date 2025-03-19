package com.be.back_end.service.EmailService;

import jakarta.mail.MessagingException;

public interface IEmailService {

     void sendPasswordEmail(String to, String name, String password) throws MessagingException;
     void sendAssignmentEmail(String to, String name, String bookingCode) throws MessagingException;
     void sendDesignerEmail(String to, String designerName, String bookingCode, String oldDescription, String newDescription) throws MessagingException ;
     void sendCustomerCompleteEmail(String to, String customerName, String bookingCode);
}

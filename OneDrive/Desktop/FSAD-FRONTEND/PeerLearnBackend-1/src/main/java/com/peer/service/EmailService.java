package com.peer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(String toEmail, String fullName, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setFrom(fromEmail);
        message.setSubject("PeerLearn Password Reset");
        message.setText("Hello " + fullName + ",\n\n"
                + "We received a request to reset your password.\n"
                + "Use this link to set a new password:\n"
                + resetLink + "\n\n"
                + "This link expires in 30 minutes.\n"
                + "If you did not request this, ignore this email.");

        mailSender.send(message);
    }

    public void sendLoginOtpEmail(String toEmail, String fullName, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setFrom(fromEmail);
        message.setSubject("PeerLearn Login OTP");
        message.setText("Hello " + fullName + ",\n\n"
                + "Your login OTP is: " + otp + "\n"
                + "This OTP expires in 5 minutes.\n\n"
                + "If you did not request this, ignore this email.");

        mailSender.send(message);
    }
}
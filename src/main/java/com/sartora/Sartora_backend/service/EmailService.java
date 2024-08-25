package com.sartora.Sartora_backend.service;

import com.sartora.Sartora_backend.entity.LocalUser;
import com.sartora.Sartora_backend.entity.VerificationToken;
import com.sartora.Sartora_backend.exception.EmailFailureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service

public class EmailService {



    @Value("${email.from}")
    private String fromAddress;

    @Value("${app.frontend.url}")
    private String frontendUrl;


    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    private SimpleMailMessage createSimpleMailMessage() {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(fromAddress);
        return simpleMailMessage;

    }

    public void sendVerificationEmail(VerificationToken verificationToken) throws EmailFailureException {
        SimpleMailMessage simpleMailMessage = createSimpleMailMessage();
        simpleMailMessage.setTo(verificationToken.getUser().getEmail());
        simpleMailMessage.setSubject("verify  email to activate your account");
        simpleMailMessage.setText("Please follow the link below to verify your email to activate your account.\n" + frontendUrl +
                "/verify?token=" + verificationToken.getToken());
        try {
            javaMailSender.send(simpleMailMessage);

        }catch (MailException ex) {
            throw new EmailFailureException();
        }


    }
    public void  sendPasswordResetEmail(LocalUser user, String token) throws EmailFailureException {
        SimpleMailMessage simpleMailMessage = createSimpleMailMessage();
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("Your Password Reset link");
        simpleMailMessage.setText("You requested a password reset on our website," +
                "please find the link below to reset your password .\n" + frontendUrl + "/reset?token=" + token);
        try {
            javaMailSender.send(simpleMailMessage);
        }catch (MailException ex) {
            throw new EmailFailureException();
        }

    }
}

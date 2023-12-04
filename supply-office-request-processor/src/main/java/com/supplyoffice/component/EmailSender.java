package com.supplyoffice.component;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class EmailSender {

    @Autowired
    JavaMailSender javaMailSender;
    @Value("${supply.office.address}")
    private String supplyOfficeAddress;

    public void sendEmailWithAttachment(byte[] data) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            String fileName = "Supply requests " + LocalDate.now().toString();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(supplyOfficeAddress);
            helper.setSubject(fileName);
            ByteArrayResource file = new ByteArrayResource(data);
            helper.addAttachment(fileName + ".xls", file);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}

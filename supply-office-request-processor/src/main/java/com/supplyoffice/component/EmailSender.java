package com.supplyoffice.component;

import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class EmailSender {

    @Autowired
    JavaMailSender javaMailSender;
    @Value("${supply.office.address}")
    private String supplyOfficeAddress;
    @Value("${spring.client.sender}")
    private String senderMail;

    static Logger LOG = LoggerFactory.getLogger(EmailSender.class);

    public void sendEmailWithAttachment(String file)
            throws IOException, MessagingException, jakarta.mail.MessagingException {
        if (file != null && !file.isEmpty() && !file.isBlank()) {
            LOG.debug("Received data for mail creation.");
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(senderMail);
            helper.setTo(supplyOfficeAddress);
            LOG.debug("Recipient address is set to {}.", supplyOfficeAddress);
            helper.setSubject("New supply requests");
            helper.setText("Requests ready!");
            FileSystemResource resource = new FileSystemResource(new File(file));
            LOG.debug("Created file for attachment.");
            helper.addAttachment(resource.getFilename(), resource);
            LOG.debug("File attached.");
            javaMailSender.send(message);
            LOG.debug("Mail sent.");
        } else {
            LOG.debug("No data received.");
        }
    }
}

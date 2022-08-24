package com.nvt.service.services.impl;

import com.nvt.service.models.EmailDetails;
import com.nvt.service.services.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailSender;
    @Override
    public String sendHtmlEmail(EmailDetails details) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();

        boolean multipart = true;

        MimeMessageHelper helper = new MimeMessageHelper(message, multipart);

        helper.setTo(details.getSubject());
        helper.setSubject("Test email with attachments");
        List listFiles = details.getFiles();
        for (int i = 0; i < listFiles.size(); i++) {
            FileSystemResource file = new FileSystemResource(new File((String) listFiles.get(i)));
            helper.addAttachment("Readme", file);
        }
        helper.setText("Hello, Im testing email with attachments!");
        emailSender.send(message);
        return "Email send done!";
    }

    @Override
    public String sendMailWithAttachment(EmailDetails details) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();

        boolean multipart = true;

        MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");

        String htmlMsg = "<h3>Im testing send a HTML email</h3>"
                +"<img src='http://www.apache.org/images/asf_logo_wide.gif'>";

        message.setContent(htmlMsg, "text/html");

        helper.setTo(details.getRecipient());

        helper.setSubject("Test send HTML email");


        this.emailSender.send(message);
        return null;
    }
}

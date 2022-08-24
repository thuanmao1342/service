package com.nvt.service.services;

import com.nvt.service.models.EmailDetails;

import javax.mail.MessagingException;

public interface EmailService {

    String sendHtmlEmail(EmailDetails details) throws MessagingException;

    String sendMailWithAttachment(EmailDetails details) throws MessagingException;
}

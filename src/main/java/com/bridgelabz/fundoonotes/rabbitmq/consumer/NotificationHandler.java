package com.bridgelabz.fundoonotes.rabbitmq.consumer;


import com.bridgelabz.fundoonotes.dto.EmailTemplateDTO;
import com.bridgelabz.fundoonotes.utils.IMailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;

@Service
public class NotificationHandler {

    @Autowired
    IMailService mailService;

    @RabbitListener(queues = "collaboratorQueue")
    public void handleQueue(EmailTemplateDTO emailTemplate) throws MessagingException {
        mailService.sendMail(emailTemplate.message, emailTemplate.subject, emailTemplate.email);
        System.out.println("Email Sent Successfully To " + emailTemplate.email);
    }
}
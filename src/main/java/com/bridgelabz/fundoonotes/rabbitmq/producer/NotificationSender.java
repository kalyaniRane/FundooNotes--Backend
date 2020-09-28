package com.bridgelabz.fundoonotes.rabbitmq.producer;

import com.bridgelabz.fundoonotes.dto.EmailTemplateDTO;
import com.bridgelabz.fundoonotes.properties.FileProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NotificationSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    FileProperties fileProperties;

    public void addToQueue(EmailTemplateDTO emailTemplate) {
        rabbitTemplate.convertAndSend("collaborator", "routine", emailTemplate);
    }
}
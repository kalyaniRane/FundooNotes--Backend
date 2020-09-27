package com.bridgelabz.fundoonotes.configuration;


import com.bridgelabz.fundoonotes.properties.FileProperties;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigureRabbitMq {

    @Autowired
    FileProperties fileProperties;

    @Bean
    Queue createQueue() {
        return new Queue("collaboratorQueue", true, false, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("collaborator");
    }

    @Bean
    Binding binding(Queue q, TopicExchange exchange) {
        return BindingBuilder.bind(q).to(exchange).with("routine");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

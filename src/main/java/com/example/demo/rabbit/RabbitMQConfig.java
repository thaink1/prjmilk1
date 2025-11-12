package com.example.demo.rabbit;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String INVENTORY_EXCHANGE = "inventoryExchange";

    @Bean
    public Queue inventoryHistoryQueue() {
        return new Queue("inventoryHistoryQueue", true);
    }

    @Bean
    public TopicExchange inventoryExchange() {
        return new TopicExchange(INVENTORY_EXCHANGE);
    }

    @Bean
    public Binding inventoryHistoryBinding(Queue inventoryHistoryQueue, TopicExchange inventoryExchange) {
        return BindingBuilder.bind(inventoryHistoryQueue)
                .to(inventoryExchange)
                .with("inventory.history");
    }

    // 👇 Converter giúp Rabbit tự động (de)serialize JSON
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
}

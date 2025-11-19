package com.ms.ticketlock.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String RESERVATION_EXPIRATION_QUEUE = "reservation.expiration.queue";
    public static final String RESERVATION_EXPIRATION_DLX = "reservation.expiration.dlx";
    public static final String RESERVATION_DELAY_QUEUE = "reservation.delay.queue";

    private static final int EXPIRATION_DELAY_MS = 10000; // 10 minutos

    @Bean
    public Queue expirationQueue() {
        return new Queue(RESERVATION_EXPIRATION_QUEUE, true);
    }

    @Bean
    public DirectExchange expirationExchange() {
        return new DirectExchange(RESERVATION_EXPIRATION_DLX);
    }

    @Bean
    public Binding expirationBinding() {
        return BindingBuilder
                .bind(expirationQueue())
                .to(expirationExchange())
                .with(RESERVATION_EXPIRATION_QUEUE);
    }

    @Bean
    public Queue delayQueue() {
        return QueueBuilder.durable(RESERVATION_DELAY_QUEUE)
                .withArgument("x-dead-letter-exchange", RESERVATION_EXPIRATION_DLX)
                .withArgument("x-dead-letter-routing-key", RESERVATION_EXPIRATION_QUEUE)
                .withArgument("x-message-ttl", EXPIRATION_DELAY_MS)
                .build();
    }
}
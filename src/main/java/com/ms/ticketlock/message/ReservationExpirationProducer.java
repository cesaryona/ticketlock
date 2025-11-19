package com.ms.ticketlock.message;

import com.ms.ticketlock.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationExpirationProducer {

    private final RabbitTemplate rabbitTemplate;

    public void scheduleExpiration(UUID reservationId) {
        log.info("sending message - reservationId [{}]", reservationId);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.RESERVATION_DELAY_QUEUE,
                reservationId.toString()
        );
    }
}
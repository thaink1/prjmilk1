package com.example.demo.service;

import com.example.demo.dto.InventoryHistoryMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryHistoryEmailService {
    private final MailService mailService;

    @RabbitListener(queues = "inventoryHistoryQueue")
    public void handleInventoryChange(InventoryHistoryMessage message) {
        log.info("Received inventory change message: {}", message);
        mailService.sendInventoryHistoryEmail(message);
    }
}

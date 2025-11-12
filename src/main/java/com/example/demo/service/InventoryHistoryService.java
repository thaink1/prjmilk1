package com.example.demo.service;

import com.example.demo.rabbit.RabbitMQConfig;
import com.example.demo.dto.InventoryHistoryMessage;
import com.example.demo.model.Inventory;
import com.example.demo.repo.InventoryRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryHistoryService {

    private final InventoryRepo inventoryHistoryRepository;
    private final RabbitTemplate rabbitTemplate;

    public Inventory createHistory(Inventory history) {
        Inventory saved = inventoryHistoryRepository.save(history);


        InventoryHistoryMessage message = new InventoryHistoryMessage(
                saved.getHistoryId(),
                saved.getProductId(),
                saved.getChangeQuantity(),
                saved.getDate(),
                saved.getReason(),
                "inventory.manager@example.com"
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.INVENTORY_EXCHANGE,
                "inventory.history",
                message
        );



        log.info("Sent inventory history message for productId={} reason={}",
                saved.getProductId(), saved.getReason());
        return saved;
    }
}

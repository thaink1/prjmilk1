package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryHistoryMessage {
    private Long historyId;
    private Long productId;
    private int changeQuantity;
    private LocalDateTime date;
    private String reason;
    private String email;
}

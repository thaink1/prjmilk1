package com.example.demo.dto;

import java.time.LocalDateTime;

public interface ProductImageIF {
    Long getImageId();
    String getUrl();
    Boolean getIsPrimary();
    LocalDateTime getCreatedAt();
}


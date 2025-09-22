package com.example.demo.dto;

import lombok.*;
import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class ImageResponse {
    private Long imageId;
    private String url;
    private Boolean isPrimary;
    private LocalDateTime createdAt;
}

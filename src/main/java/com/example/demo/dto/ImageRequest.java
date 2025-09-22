package com.example.demo.dto;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
public class ImageRequest {
    private String url;
    private Boolean isPrimary;
}

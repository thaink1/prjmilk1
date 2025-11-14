package com.example.demo.dto;

import com.example.demo.model.Account_User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private Account_User user;
}

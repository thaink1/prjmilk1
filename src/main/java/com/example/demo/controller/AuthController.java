package com.example.demo.controller;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.BaseResponse;
import com.example.demo.dto.GoogleLoginRequest;
import com.example.demo.dto.GoogleUserInfo;
import com.example.demo.model.Account_User;
import com.example.demo.repo.AccountRepo;
import com.example.demo.service.GoogleService;
import com.example.demo.service.JwtService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private final GoogleService googleService;
    private final AccountRepo accountRepo;
    private final JwtService jwtService;

    @PostMapping("/google")
    public BaseResponse<AuthResponse> loginWithGoogle(@RequestBody GoogleLoginRequest request) {

        GoogleUserInfo googleUser = googleService.verifyToken(request.getIdToken());

        Account_User user = accountRepo.findByUsername(googleUser.getEmail())
                .orElseGet(() -> {
                    Account_User newUser = new Account_User();
                    newUser.setUsername(googleUser.getEmail());
                    newUser.setPassword("GOOGLE_USER");
                    newUser.setRole("USER");
                    return accountRepo.save(newUser);
                });

        String token = jwtService.generateToken(user);

        AuthResponse data = new AuthResponse(token, user);

        BaseResponse<AuthResponse> response = new BaseResponse<>();
        response.setBody(data);
        response.setRequestId(MDC.get("requestId"));
        response.setResponseTime(LocalDateTime.now());

        return response;
    }

}

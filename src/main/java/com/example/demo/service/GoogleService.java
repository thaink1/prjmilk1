package com.example.demo.service;

import com.example.demo.dto.GoogleUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import java.util.Collections;

@Service
@Slf4j
public class GoogleService {

    @Value("${google.clientId}")
    private String clientId;

    public GoogleUserInfo verifyToken(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    new GsonFactory()
            ).setAudience(Collections.singletonList(clientId)).build();

            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                return new GoogleUserInfo(
                        payload.getEmail(),
                        (String) payload.get("name"),
                        (String) payload.get("picture")
                );
            }

            throw new RuntimeException("Invalid Google ID token");

        } catch (Exception e) {
            log.error("Error verifying Google token", e);
            throw new RuntimeException("Google verification failed");
        }
    }
}

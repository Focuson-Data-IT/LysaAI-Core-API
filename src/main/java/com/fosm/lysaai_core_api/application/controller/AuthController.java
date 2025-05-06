package com.fosm.lysaai_core_api.application.controller;

import com.fosm.lysaai_core_api.application.dto.auth.LoginRequestDto;
import com.fosm.lysaai_core_api.application.dto.auth.LoginResponseDto;
import com.fosm.lysaai_core_api.application.dto.auth.RefreshRequestDto;
import com.fosm.lysaai_core_api.application.dto.generic.GenericDto;
import com.fosm.lysaai_core_api.application.dto.user.UserRequestDto;
import com.fosm.lysaai_core_api.domain.port.in.AuthService;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        String accessToken = authService.login(request.getUsername(), request.getPassword());
        String refreshToken = authService.refreshToken(accessToken);
        return ResponseEntity.ok(LoginResponseDto
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build());
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestBody RefreshRequestDto request) {
        String newAccessToken = authService.refreshToken(request.getRefreshToken());
        Map<String, String> response = new HashMap<>();
        response.put("accessToken", newAccessToken);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        authService.logout(token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequestDto request) {
        try {
            authService.register(request.getName(), request.getUsername(), request.getEmail(), request.getPassword());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    GenericDto.builder()
                            .data(null)
                            .code(HttpStatusCode.valueOf(400))
                            .status("Bad Request")
                            .error(e.getMessage())
                            .build()
            );
        }
        return ResponseEntity.ok().build();
    }
}
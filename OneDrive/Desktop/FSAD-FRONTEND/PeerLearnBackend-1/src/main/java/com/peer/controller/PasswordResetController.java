package com.peer.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.peer.dto.ForgotPasswordRequest;
import com.peer.dto.ResetPasswordRequest;
import com.peer.service.PasswordResetService;

@RestController
@RequestMapping("/api/auth")
public class PasswordResetController {


    private final PasswordResetService passwordResetService;

    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        passwordResetService.createResetRequest(request.getEmail());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Password reset link sent to your email.");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        passwordResetService.resetPassword(request.getToken(), request.getNewPassword());

        Map<String, String> response = new HashMap<>();
        response.put("message", "Password updated successfully.");
        return ResponseEntity.ok(response);
    }
}
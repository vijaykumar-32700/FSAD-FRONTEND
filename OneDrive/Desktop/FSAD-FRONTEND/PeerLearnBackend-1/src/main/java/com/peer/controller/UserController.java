package com.peer.controller;

import com.peer.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(Authentication authentication) {
        return ResponseEntity.ok(userService.getProfile(authentication.getName()));
    }

    @GetMapping("/students")
    public ResponseEntity<List<Map<String, Object>>> students(
            @RequestParam(value = "section", required = false) String section
    ) {
        return ResponseEntity.ok(userService.getStudents(section));
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<Map<String, Object>> studentById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getStudentById(id));
    }
}
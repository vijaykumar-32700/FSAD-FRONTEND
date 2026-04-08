package com.peer.controller;

import com.peer.dto.AssignmentRequest;
import com.peer.dto.AssignmentResponse;
import com.peer.service.AssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @PostMapping
    public ResponseEntity<AssignmentResponse> create(
            @Valid @RequestBody AssignmentRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(assignmentService.createAssignment(request, authentication.getName()));
    }

    @GetMapping("/teacher")
    public ResponseEntity<List<AssignmentResponse>> teacherAssignments(Authentication authentication) {
        return ResponseEntity.ok(assignmentService.getTeacherAssignments(authentication.getName()));
    }

    @GetMapping("/student")
    public ResponseEntity<List<AssignmentResponse>> studentAssignments(Authentication authentication) {
        return ResponseEntity.ok(assignmentService.getStudentAssignments(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssignmentResponse> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(assignmentService.getAssignment(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        assignmentService.deleteAssignment(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
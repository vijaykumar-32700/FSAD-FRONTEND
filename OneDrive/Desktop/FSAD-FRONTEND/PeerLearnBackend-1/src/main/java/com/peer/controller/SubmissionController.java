package com.peer.controller;

import com.peer.dto.SubmissionResponse;
import com.peer.service.SubmissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionController {

    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<SubmissionResponse> submit(
            @RequestParam("assignmentId") Long assignmentId,
            @RequestParam("submissionText") String submissionText,
            @RequestParam(value = "file", required = false) MultipartFile file,
            Authentication authentication
    ) throws IOException {
        return ResponseEntity.ok(
                submissionService.submit(assignmentId, submissionText, file, authentication.getName())
        );
    }

    @GetMapping("/student")
    public ResponseEntity<List<SubmissionResponse>> mySubmissions(Authentication authentication) {
        return ResponseEntity.ok(submissionService.getStudentSubmissions(authentication.getName()));
    }

    @GetMapping("/assignment/{assignmentId}")
    public ResponseEntity<List<SubmissionResponse>> byAssignment(
            @PathVariable Long assignmentId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                submissionService.getSubmissionsByAssignment(assignmentId, authentication.getName())
        );
    }

    @GetMapping("/teacher/recent")
    public ResponseEntity<List<SubmissionResponse>> recent(Authentication authentication) {
        return ResponseEntity.ok(submissionService.getRecentSubmissions(authentication.getName()));
    }

    @PatchMapping("/{submissionId}/grade")
    public ResponseEntity<SubmissionResponse> grade(
            @PathVariable Long submissionId,
            @RequestBody GradeRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                submissionService.gradeSubmission(
                        submissionId,
                        request.score(),
                        request.feedback(),
                        authentication.getName()
                )
        );
    }

    @GetMapping("/teacher/pending-count")
    public ResponseEntity<Long> pendingCount(Authentication authentication) {
        return ResponseEntity.ok(submissionService.getPendingCount(authentication.getName()));
    }

    public record GradeRequest(Integer score, String feedback) {}
}
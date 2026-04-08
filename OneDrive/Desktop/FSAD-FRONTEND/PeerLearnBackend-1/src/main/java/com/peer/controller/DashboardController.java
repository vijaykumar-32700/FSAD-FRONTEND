package com.peer.controller;

import com.peer.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final AssignmentService assignmentService;
    private final SubmissionService submissionService;
    private final PeerReviewService peerReviewService;
    private final NotificationService notificationService;
    private final UserService userService;

    public DashboardController(
            AssignmentService assignmentService,
            SubmissionService submissionService,
            PeerReviewService peerReviewService,
            NotificationService notificationService,
            UserService userService
    ) {
        this.assignmentService = assignmentService;
        this.submissionService = submissionService;
        this.peerReviewService = peerReviewService;
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @GetMapping("/student")
    public ResponseEntity<Map<String, Object>> student(Authentication authentication) {
        String email = authentication.getName();

        Map<String, Object> data = new HashMap<>();
        data.put("assignments", assignmentService.getStudentAssignments(email).size());
        data.put("submissions", submissionService.getStudentSubmissions(email).size());

        long pendingReviews = peerReviewService.getReviewsForReviewer(email).stream()
                .filter(r -> "PENDING".equalsIgnoreCase(r.getStatus()))
                .count();
        data.put("pendingReviews", pendingReviews);

        data.put("unreadNotifications", notificationService.getUnreadCount(email));
        return ResponseEntity.ok(data);
    }

    @GetMapping("/teacher")
    public ResponseEntity<Map<String, Object>> teacher(Authentication authentication) {
        String email = authentication.getName();

        Map<String, Object> data = new HashMap<>();
        data.put("assignments", assignmentService.getTeacherAssignments(email).size());
        data.put("students", userService.getAllStudents().size());
        data.put("pendingToGrade", submissionService.getPendingCount(email));
        data.put("reviews", peerReviewService.getReviewsForReviewer(email).size());

        return ResponseEntity.ok(data);
    }
}
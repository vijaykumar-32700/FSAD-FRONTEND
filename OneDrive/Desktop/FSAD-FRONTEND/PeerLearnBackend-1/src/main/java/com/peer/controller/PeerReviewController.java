package com.peer.controller;

import com.peer.dto.PeerReviewRequest;
import com.peer.dto.PeerReviewResponse;
import com.peer.service.PeerReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class PeerReviewController {

    private final PeerReviewService peerReviewService;

    public PeerReviewController(PeerReviewService peerReviewService) {
        this.peerReviewService = peerReviewService;
    }

    @GetMapping("/mine")
    public ResponseEntity<List<PeerReviewResponse>> myReviews(Authentication authentication) {
        return ResponseEntity.ok(peerReviewService.getReviewsForReviewer(authentication.getName()));
    }

    @PostMapping("/assign")
    public ResponseEntity<PeerReviewResponse> assign(
            @RequestBody AssignReviewRequest request
    ) {
        return ResponseEntity.ok(
                peerReviewService.assignReview(request.submissionId(), request.reviewerId())
        );
    }

    @PostMapping("/{reviewId}/submit")
    public ResponseEntity<PeerReviewResponse> submit(
            @PathVariable Long reviewId,
            @RequestBody PeerReviewRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                peerReviewService.submitReview(reviewId, request, authentication.getName())
        );
    }

    @GetMapping("/submission/{submissionId}")
    public ResponseEntity<List<PeerReviewResponse>> bySubmission(@PathVariable Long submissionId) {
        return ResponseEntity.ok(peerReviewService.getReviewsForSubmission(submissionId));
    }

    @GetMapping("/average")
    public ResponseEntity<Double> myAverage(Authentication authentication) {
        return ResponseEntity.ok(peerReviewService.getAveragePeerScore(authentication.getName()));
    }

    public record AssignReviewRequest(Long submissionId, Long reviewerId) {}
}
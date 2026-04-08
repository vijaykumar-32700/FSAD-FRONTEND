package com.peer.service;

import com.peer.dto.PeerReviewRequest;
import com.peer.dto.PeerReviewResponse;
import com.peer.entity.Notification;
import com.peer.entity.PeerReview;
import com.peer.entity.Submission;
import com.peer.entity.User;
import com.peer.repository.NotificationRepository;
import com.peer.repository.PeerReviewRepository;
import com.peer.repository.SubmissionRepository;
import com.peer.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PeerReviewService {

    private final PeerReviewRepository peerReviewRepository;
    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;

    public PeerReviewService(
            PeerReviewRepository peerReviewRepository,
            SubmissionRepository submissionRepository,
            UserRepository userRepository,
            NotificationRepository notificationRepository
    ) {
        this.peerReviewRepository = peerReviewRepository;
        this.submissionRepository = submissionRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
    }

    // Assign to ALL students in the same section (except submitter)
    @Transactional
    public void autoGeneratePeerReviews(Submission submission) {
        if (submission == null || submission.getAssignment() == null || submission.getStudent() == null) {
            return;
        }

        String section = submission.getAssignment().getSection();
        if (section == null || section.isBlank()) {
            section = submission.getStudent().getSection();
        }
        if (section == null || section.isBlank()) {
            return;
        }

        List<User> classmates = userRepository.findByRoleAndSection(User.Role.STUDENT, section);

        for (User reviewer : classmates) {
            if (reviewer.getId().equals(submission.getStudent().getId())) {
                continue;
            }

            if (peerReviewRepository.existsBySubmissionAndReviewer(submission, reviewer)) {
                continue;
            }

            PeerReview review = PeerReview.builder()
                    .submission(submission)
                    .reviewer(reviewer)
                    .assignment(submission.getAssignment())
                    .dueDate(submission.getAssignment().getDueDate())
                    .submittedByStudentId(submission.getStudent().getId())
                    .maxScore(submission.getAssignment().getPoints())
                    .status(PeerReview.Status.PENDING)
                    .build();

            peerReviewRepository.save(review);

            Notification assignNotification = Notification.builder()
                    .user(reviewer)
                    .title("New Peer Review Assigned")
                    .message("You have a new peer review for: " + submission.getAssignment().getTitle())
                    .type(Notification.NotificationType.REVIEW)
                    .build();
            notificationRepository.save(assignNotification);
        }
    }

    public List<PeerReviewResponse> getReviewsForReviewer(String reviewerEmail) {
        User reviewer = userRepository.findByEmail(reviewerEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return peerReviewRepository.findByReviewerOrderByCreatedAtDesc(reviewer)
                .stream()
                .map(PeerReviewResponse::new)
                .collect(Collectors.toList());
    }

    public PeerReviewResponse assignReview(Long submissionId, Long reviewerId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        User reviewer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new RuntimeException("Reviewer not found"));

        if (reviewer.getRole() != User.Role.STUDENT) {
            throw new RuntimeException("Only students can be assigned as peer reviewers");
        }

        if (peerReviewRepository.existsBySubmissionAndReviewer(submission, reviewer)) {
            throw new RuntimeException("Reviewer already assigned to this submission");
        }

        PeerReview review = PeerReview.builder()
                .submission(submission)
                .reviewer(reviewer)
                .assignment(submission.getAssignment())
                .dueDate(submission.getAssignment().getDueDate())
                .submittedByStudentId(submission.getStudent().getId())
                .maxScore(submission.getAssignment().getPoints())
                .status(PeerReview.Status.PENDING)
                .build();

        PeerReview saved = peerReviewRepository.save(review);

        Notification notification = Notification.builder()
                .user(reviewer)
                .title("New Peer Review Assigned")
                .message("You have a new peer review for: " + submission.getAssignment().getTitle())
                .type(Notification.NotificationType.REVIEW)
                .build();
        notificationRepository.save(notification);

        return new PeerReviewResponse(saved);
    }

    @Transactional
    public PeerReviewResponse submitReview(Long reviewId, PeerReviewRequest request, String reviewerEmail) {
        PeerReview review = peerReviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Peer review not found"));

        if (review.getReviewer() == null || !review.getReviewer().getEmail().equals(reviewerEmail)) {
            throw new RuntimeException("Unauthorized");
        }

        if (review.getStatus() == PeerReview.Status.COMPLETED) {
            throw new RuntimeException("Review already submitted");
        }

        if (request.getScore() == null || request.getScore() < 0) {
            throw new RuntimeException("Invalid score");
        }

        if (review.getMaxScore() != null && request.getScore() > review.getMaxScore()) {
            throw new RuntimeException("Score cannot exceed max score");
        }

        review.setScore(request.getScore());
        review.setFeedbackText(request.getFeedbackText());
        review.setStatus(PeerReview.Status.COMPLETED);
        review.setCompletedAt(LocalDateTime.now());
        PeerReview saved = peerReviewRepository.save(review);

        Submission submission = review.getSubmission();

        List<PeerReview> completed = peerReviewRepository.findBySubmission(submission).stream()
                .filter(r -> r.getStatus() == PeerReview.Status.COMPLETED && r.getScore() != null)
                .collect(Collectors.toList());

        double avg = completed.stream()
                .mapToInt(PeerReview::getScore)
                .average()
                .orElse(0.0);

        submission.setAveragePeerScore(Math.round(avg * 10.0) / 10.0);
        submission.setPeerReviewCount(completed.size());

        if (submission.getTeacherScore() == null) {
            submission.setStatus(Submission.Status.PEER_REVIEWED);
        }

        submissionRepository.save(submission);

        Notification resultNotification = Notification.builder()
                .user(submission.getStudent())
                .title("Peer Review Received")
                .message("A peer reviewed your submission for: " + submission.getAssignment().getTitle())
                .type(Notification.NotificationType.REVIEW)
                .build();
        notificationRepository.save(resultNotification);

        return new PeerReviewResponse(saved);
    }

    public List<PeerReviewResponse> getReviewsForSubmission(Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        return peerReviewRepository.findBySubmission(submission)
                .stream()
                .map(PeerReviewResponse::new)
                .collect(Collectors.toList());
    }

    public Double getAveragePeerScore(String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Double avg = peerReviewRepository.findAveragePeerScoreByStudent(student);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0;
    }
}
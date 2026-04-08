package com.peer.service;

import com.peer.dto.SubmissionResponse;
import com.peer.entity.Assignment;
import com.peer.entity.Notification;
import com.peer.entity.Submission;
import com.peer.entity.User;
import com.peer.repository.AssignmentRepository;
import com.peer.repository.NotificationRepository;
import com.peer.repository.SubmissionRepository;
import com.peer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final PeerReviewService peerReviewService;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    public SubmissionService(
            SubmissionRepository submissionRepository,
            AssignmentRepository assignmentRepository,
            UserRepository userRepository,
            NotificationRepository notificationRepository,
            PeerReviewService peerReviewService
    ) {
        this.submissionRepository = submissionRepository;
        this.assignmentRepository = assignmentRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.peerReviewService = peerReviewService;
    }

    public SubmissionResponse submit(Long assignmentId, String submissionText, MultipartFile file, String studentEmail) throws IOException {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (student.getRole() != User.Role.STUDENT) {
            throw new RuntimeException("Only students can submit assignments");
        }

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        if (submissionRepository.findByAssignmentAndStudent(assignment, student).isPresent()) {
            throw new RuntimeException("Already submitted");
        }

        String fileName = null;
        String filePath = null;

        if (file != null && !file.isEmpty()) {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(uploadPath);

            String originalFileName = file.getOriginalFilename() != null
                    ? Paths.get(file.getOriginalFilename()).getFileName().toString()
                    : "upload";

            String storedFileName = System.currentTimeMillis() + "_" + originalFileName;
            Path target = uploadPath.resolve(storedFileName).normalize().toAbsolutePath();
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            fileName = originalFileName;
            filePath = "/uploads/" + storedFileName;
        }

        Submission submission = Submission.builder()
                .assignment(assignment)
                .student(student)
                .submissionText(submissionText)
                .fileName(fileName)
                .filePath(filePath)
                .status(Submission.Status.PENDING_REVIEW)
                .build();

        Submission saved = submissionRepository.save(submission);

        Notification notification = Notification.builder()
                .user(assignment.getTeacher())
                .title("New Submission")
                .message(student.getFullName() + " submitted " + assignment.getTitle())
                .type(Notification.NotificationType.SYSTEM)
                .build();
        notificationRepository.save(notification);

        peerReviewService.autoGeneratePeerReviews(saved);

        return new SubmissionResponse(saved);
    }

    public List<SubmissionResponse> getStudentSubmissions(String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return submissionRepository.findByStudent(student)
                .stream()
                .map(SubmissionResponse::new)
                .collect(Collectors.toList());
    }

    public List<SubmissionResponse> getSubmissionsByAssignment(Long assignmentId, String teacherEmail) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        if (assignment.getTeacher() == null || !assignment.getTeacher().getEmail().equals(teacherEmail)) {
            throw new RuntimeException("Unauthorized");
        }

        return submissionRepository.findByAssignmentOrderBySubmittedAtDesc(assignment)
                .stream()
                .map(SubmissionResponse::new)
                .collect(Collectors.toList());
    }

    public List<SubmissionResponse> getRecentSubmissions(String teacherEmail) {
        User teacher = userRepository.findByEmail(teacherEmail)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        return submissionRepository.findRecentByTeacher(teacher)
                .stream()
                .map(SubmissionResponse::new)
                .collect(Collectors.toList());
    }

    public SubmissionResponse gradeSubmission(Long submissionId, Integer score, String feedback, String teacherEmail) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        if (submission.getAssignment() == null
                || submission.getAssignment().getTeacher() == null
                || !submission.getAssignment().getTeacher().getEmail().equals(teacherEmail)) {
            throw new RuntimeException("Unauthorized");
        }

        if (score == null || score < 0) {
            throw new RuntimeException("Invalid score");
        }

        Integer max = submission.getAssignment().getPoints();
        if (max != null && score > max) {
            throw new RuntimeException("Score cannot exceed assignment points");
        }

        submission.setTeacherScore(score);
        submission.setTeacherFeedback(feedback);
        submission.setStatus(Submission.Status.FULLY_GRADED);
        submission.setGradedAt(LocalDateTime.now());
        Submission saved = submissionRepository.save(submission);

        Notification notification = Notification.builder()
                .user(submission.getStudent())
                .title("New Grade Posted")
                .message("Your submission for '" + submission.getAssignment().getTitle()
                        + "' has been graded: " + score + "/" + submission.getAssignment().getPoints())
                .type(Notification.NotificationType.GRADE)
                .build();
        notificationRepository.save(notification);

        return new SubmissionResponse(saved);
    }

    public long getPendingCount(String teacherEmail) {
        User teacher = userRepository.findByEmail(teacherEmail)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        return submissionRepository.countPendingByTeacher(teacher);
    }
}
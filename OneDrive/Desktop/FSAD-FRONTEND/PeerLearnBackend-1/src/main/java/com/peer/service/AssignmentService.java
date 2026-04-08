package com.peer.service;

import com.peer.dto.AssignmentRequest;
import com.peer.dto.AssignmentResponse;
import com.peer.entity.Assignment;
import com.peer.entity.User;
import com.peer.repository.AssignmentRepository;
import com.peer.repository.SubmissionRepository;
import com.peer.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final SubmissionRepository submissionRepository;
    private final UserRepository userRepository;

    public AssignmentService(
            AssignmentRepository assignmentRepository,
            SubmissionRepository submissionRepository,
            UserRepository userRepository
    ) {
        this.assignmentRepository = assignmentRepository;
        this.submissionRepository = submissionRepository;
        this.userRepository = userRepository;
    }

    public AssignmentResponse createAssignment(AssignmentRequest request, String teacherEmail) {
        User teacher = userRepository.findByEmail(teacherEmail)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        if (teacher.getRole() != User.Role.TEACHER) {
            throw new RuntimeException("Only teachers can create assignments");
        }

        Assignment assignment = Assignment.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .teacher(teacher)
                .section(request.getSection())
                .dueDate(request.getDueDate())
                .points(request.getPoints())
                .peerReviewsRequired(request.getPeerReviewsRequired())
                .isGroup(request.isGroup())
                .groupLimit(request.getGroupLimit())
                .build();

        Assignment saved = assignmentRepository.save(assignment);
        return toResponse(saved);
    }

    public List<AssignmentResponse> getTeacherAssignments(String teacherEmail) {
        User teacher = userRepository.findByEmail(teacherEmail)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        return assignmentRepository.findByTeacherOrderByCreatedAtDesc(teacher)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<AssignmentResponse> getStudentAssignments(String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        String section = student.getSection();
        return assignmentRepository.findBySectionOrderByDueDateAsc(section)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public AssignmentResponse getAssignment(Long id) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
        return toResponse(assignment);
    }

    public void deleteAssignment(Long id, String teacherEmail) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        if (assignment.getTeacher() == null || !assignment.getTeacher().getEmail().equals(teacherEmail)) {
            throw new RuntimeException("Unauthorized");
        }

        assignmentRepository.delete(assignment);
    }

    private AssignmentResponse toResponse(Assignment a) {
        AssignmentResponse response = new AssignmentResponse(a);
        long count = submissionRepository.findByAssignment(a).size();
        response.setSubmissionCount((int) count);
        return response;
    }
}
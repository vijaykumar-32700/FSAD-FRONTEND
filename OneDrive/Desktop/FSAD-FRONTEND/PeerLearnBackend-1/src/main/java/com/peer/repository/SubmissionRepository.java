package com.peer.repository;

import com.peer.entity.Assignment;
import com.peer.entity.Submission;
import com.peer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByStudent(User student);
    List<Submission> findByAssignment(Assignment assignment);
    Optional<Submission> findByAssignmentAndStudent(Assignment assignment, User student);
    List<Submission> findByAssignmentOrderBySubmittedAtDesc(Assignment assignment);

    @Query("SELECT s FROM Submission s WHERE s.assignment.teacher = :teacher ORDER BY s.submittedAt DESC")
    List<Submission> findRecentByTeacher(User teacher);

    @Query("SELECT COUNT(s) FROM Submission s WHERE s.assignment.teacher = :teacher AND s.teacherScore IS NULL")
    long countPendingByTeacher(User teacher);
}
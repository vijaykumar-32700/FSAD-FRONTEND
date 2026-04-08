package com.peer.repository;

import com.peer.entity.Assignment;
import com.peer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByTeacher(User teacher);
    List<Assignment> findBySection(String section);
    List<Assignment> findByTeacherOrderByCreatedAtDesc(User teacher);
    List<Assignment> findBySectionOrderByDueDateAsc(String section);
}
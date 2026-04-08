package com.peer.repository;

import com.peer.entity.Assignment;
import com.peer.entity.GroupMember;
import com.peer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    List<GroupMember> findByAssignment(Assignment assignment);
    boolean existsByAssignmentAndStudent(Assignment assignment, User student);
    long countByAssignment(Assignment assignment);
}
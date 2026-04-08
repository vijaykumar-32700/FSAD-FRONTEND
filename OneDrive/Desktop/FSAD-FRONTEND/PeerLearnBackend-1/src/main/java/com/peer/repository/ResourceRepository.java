package com.peer.repository;

import com.peer.entity.Resource;
import com.peer.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findAllByOrderByCreatedAtDesc();
    List<Resource> findByType(String type);
    List<Resource> findByTeacher(User teacher);
}
package com.peer.service;

import com.peer.dto.ResourceRequest;
import com.peer.dto.ResourceResponse;
import com.peer.entity.Resource;
import com.peer.entity.User;
import com.peer.repository.ResourceRepository;
import com.peer.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final UserRepository userRepository;

    public ResourceService(ResourceRepository resourceRepository, UserRepository userRepository) {
        this.resourceRepository = resourceRepository;
        this.userRepository = userRepository;
    }

    public List<ResourceResponse> getAllResources() {
        return resourceRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(ResourceResponse::new)
                .collect(Collectors.toList());
    }

    public List<ResourceResponse> getByType(String type) {
        if (type == null || type.isBlank() || "All Resources".equalsIgnoreCase(type)) {
            return getAllResources();
        }

        return resourceRepository.findByType(type)
                .stream()
                .map(ResourceResponse::new)
                .collect(Collectors.toList());
    }

    public ResourceResponse createResource(ResourceRequest request, String teacherEmail) {
        User teacher = userRepository.findByEmail(teacherEmail)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        if (teacher.getRole() != User.Role.TEACHER) {
            throw new RuntimeException("Only teachers can add resources");
        }

        Resource resource = Resource.builder()
                .teacher(teacher)
                .title(request.getTitle())
                .description(request.getDescription())
                .type(request.getType())
                .format(request.getFormat())
                .link(request.getLink())
                .downloadCount(0)
                .build();

        Resource saved = resourceRepository.save(resource);
        return new ResourceResponse(saved);
    }

    public void deleteResource(Long id, String teacherEmail) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found"));

        if (resource.getTeacher() == null || !resource.getTeacher().getEmail().equals(teacherEmail)) {
            throw new RuntimeException("Unauthorized");
        }

        resourceRepository.delete(resource);
    }

    public void incrementDownload(Long id) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found"));

        Integer count = resource.getDownloadCount() == null ? 0 : resource.getDownloadCount();
        resource.setDownloadCount(count + 1);
        resourceRepository.save(resource);
    }
}
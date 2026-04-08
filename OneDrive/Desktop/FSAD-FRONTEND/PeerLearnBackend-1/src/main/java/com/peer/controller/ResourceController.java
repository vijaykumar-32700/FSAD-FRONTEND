package com.peer.controller;

import com.peer.dto.ResourceRequest;
import com.peer.dto.ResourceResponse;
import com.peer.service.ResourceService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    private final ResourceService resourceService;
    private final Path uploadRoot = Paths.get("uploads", "resources");

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping
    public ResponseEntity<List<ResourceResponse>> all(
            @RequestParam(value = "type", required = false) String type
    ) {
        if (type == null || type.isBlank()) {
            return ResponseEntity.ok(resourceService.getAllResources());
        }
        return ResponseEntity.ok(resourceService.getByType(type));
    }

    // Existing JSON endpoint kept as-is
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResourceResponse> create(
            @RequestBody ResourceRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(resourceService.createResource(request, authentication.getName()));
    }

    // New endpoint for file upload (multipart/form-data)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResourceResponse> createWithFile(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("type") String type,
            @RequestParam(value = "format", required = false) String format,
            @RequestParam(value = "link", required = false) String link,
            @RequestParam(value = "file", required = false) MultipartFile file,
            Authentication authentication
    ) {
        try {
            boolean hasFile = file != null && !file.isEmpty();
            boolean hasLink = link != null && !link.isBlank();

            if (!hasFile && !hasLink) {
                return ResponseEntity.badRequest().build();
            }

            String resolvedLink = link;
            String resolvedFormat = (format == null || format.isBlank()) ? "FILE" : format;

            if (hasFile) {
                Files.createDirectories(uploadRoot);

                String originalName = StringUtils.cleanPath(
                        file.getOriginalFilename() == null ? "file" : file.getOriginalFilename()
                );

                String ext = "";
                int dotIndex = originalName.lastIndexOf('.');
                if (dotIndex >= 0 && dotIndex < originalName.length() - 1) {
                    ext = originalName.substring(dotIndex + 1);
                }

                String storedName = UUID.randomUUID() + (ext.isBlank() ? "" : "." + ext);
                Path targetPath = uploadRoot.resolve(storedName);
                Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                resolvedLink = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/uploads/resources/")
                        .path(storedName)
                        .toUriString();

                if (format == null || format.isBlank()) {
                    resolvedFormat = ext.isBlank() ? "FILE" : ext.toUpperCase();
                }
            }

            ResourceRequest request = new ResourceRequest();
            request.setTitle(title);
            request.setDescription(description);
            request.setType(type);
            request.setFormat(resolvedFormat);
            request.setLink(resolvedLink);

            return ResponseEntity.ok(resourceService.createResource(request, authentication.getName()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication authentication
    ) {
        resourceService.deleteResource(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/download")
    public ResponseEntity<Void> incrementDownload(@PathVariable Long id) {
        resourceService.incrementDownload(id);
        return ResponseEntity.ok().build();
    }
}
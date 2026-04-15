package com.example.product_service.service.storage;

import com.example.product_service.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;


    @Value("${app.base-url}")
    private String baseUrl;

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/png",
            "image/jpeg",
            "image/jpg",
            "image/webp"
    );

    public String storeFile(MultipartFile file) {

        validateFile(file);

        try {
            String originalName = StringUtils.cleanPath(
                    Objects.requireNonNull(file.getOriginalFilename())
            );

            String extension = originalName.substring(originalName.lastIndexOf("."));

            String baseName = originalName
                    .substring(0, originalName.lastIndexOf("."))
                    .replaceAll("[^a-zA-Z0-9]", "_");

            String fileName = UUID.randomUUID() + "_" + baseName + extension;

            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);


            return baseUrl + "/uploads/products/" + fileName;

        } catch (IOException e) {
            throw new BadRequestException("Failed to store file " + e.getMessage());
        }
    }

    public void deleteFile(String imageUrl) {

        if (imageUrl == null || imageUrl.isBlank()) {
            return;
        }

        try {

            String relativePath = imageUrl
                    .replace(baseUrl, "")     // remove http://localhost:8082
                    .replaceFirst("^/", "");  // remove starting slash

            Path filePath = Paths.get(relativePath);

            Files.deleteIfExists(filePath);

        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file " + imageUrl, e);
        }
    }

    // File validation
    private void validateFile(MultipartFile file) {

        if (file.isEmpty()) {
            throw new BadRequestException("File is empty");
        }

        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new BadRequestException("Only image files are allowed");
        }

        if (file.getSize() > 5 * 1024 * 1024) { // 5MB
            throw new BadRequestException("File size must be less than 5MB");
        }
    }
}
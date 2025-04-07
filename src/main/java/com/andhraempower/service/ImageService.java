package com.andhraempower.service;

import com.andhraempower.entity.Image;
import com.andhraempower.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    // Upload a single image
    public Image uploadImage(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getOriginalFilename());
        image.setType(file.getContentType());
        image.setData(encodeToBase64(file)); // Convert file to Base64
        image.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        return imageRepository.save(image);
    }

    // Convert image file to Base64
    private String encodeToBase64(MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        return Base64.getEncoder().encodeToString(bytes); // Encode to Base64
    }

    // Upload multiple images
    public List<Image> uploadImages(List<MultipartFile> files) throws IOException {
        for (MultipartFile file : files) {
            uploadImage(file);
        }
        return imageRepository.findByDeletedFalse(); // Retrieve all non-deleted images
    }

    // Get image by ID (ignores deleted images)
    public Optional<Image> getImage(Long id) {
        return imageRepository.findById(id).filter(image -> !image.getDeleted());
    }

    // Get all images (only non-deleted)
    public List<Image> getAllImages() {
        return imageRepository.findByDeletedFalse();
    }

    // Soft delete image by ID
    public Optional<Image> softDeleteImage(Long id) {
        Optional<Image> imageOpt = imageRepository.findById(id);
        if (imageOpt.isPresent()) {
            Image image = imageOpt.get();
            image.setDeleted(true); // Mark as deleted
            imageRepository.save(image);
            return Optional.of(image);
        }
        return Optional.empty();
    }
}
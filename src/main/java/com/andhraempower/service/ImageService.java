package com.andhraempower.service;

import com.andhraempower.entity.GalleryImage;
import com.andhraempower.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    // Upload a single image
    public GalleryImage uploadImage(MultipartFile file, String eventType, String description) throws IOException {
        GalleryImage image = new GalleryImage();
        image.setEventType(eventType);
        image.setDescription(description);
        image.setImage(file.getBytes()); // Convert file to byte array

        return imageRepository.save(image);
    }

    // Upload multiple images
    public List<GalleryImage> uploadImages(List<MultipartFile> files, String eventType, String description) throws IOException {
        for (MultipartFile file : files) {
            uploadImage(file, eventType, description);
        }
        return imageRepository.findByDeletedFalse(); // Retrieve non-deleted images
    }

    // Get image by ID (ignores deleted images)
    public Optional<GalleryImage> getImage(Long id) {
        return imageRepository.findById(id).filter(image -> !image.getDeleted());
    }

    // Get all images (only non-deleted)
    public List<GalleryImage> getAllImages() {
        return imageRepository.findByDeletedFalse();
    }

    // Soft delete image by ID
    public Optional<GalleryImage> softDeleteImage(Long id) {
        Optional<GalleryImage> imageOpt = imageRepository.findById(id);
        if (imageOpt.isPresent()) {
            GalleryImage image = imageOpt.get();
            image.setDeleted(true); // Mark as deleted
            imageRepository.save(image);
            return Optional.of(image);
        }
        return Optional.empty();
    }
}
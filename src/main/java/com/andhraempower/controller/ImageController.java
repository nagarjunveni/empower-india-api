package com.andhraempower.controller;

import com.andhraempower.entity.Image;
import com.andhraempower.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    // Upload a single image
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            imageService.uploadImage(file);
            return new ResponseEntity<>("Image uploaded successfully", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error uploading image", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Upload multiple images
    @PostMapping("/uploadMultiple")
    public ResponseEntity<String> uploadMultipleImages(@RequestParam("files") List<MultipartFile> files) {
        try {
            imageService.uploadImages(files);
            return new ResponseEntity<>("Images uploaded successfully", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Error uploading images", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get a single image by ID
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {
        Optional<Image> imageOpt = imageService.getImage(id);
        if (imageOpt.isPresent()) {
            Image image = imageOpt.get();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, image.getType())
                    .body(image.getData());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Get all images (non-deleted)
    @GetMapping("/")
    public ResponseEntity<List<Image>> getAllImages() {
        List<Image> images = imageService.getAllImages();
        return ResponseEntity.ok(images);
    }

    // Soft delete an image
    @DeleteMapping("/{id}")
    public ResponseEntity<String> softDeleteImage(@PathVariable Long id) {
        Optional<Image> imageOpt = imageService.softDeleteImage(id);
        if (imageOpt.isPresent()) {
            return new ResponseEntity<>("Image soft deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Image not found", HttpStatus.NOT_FOUND);
        }
    }
}
package com.andhraempower.repository;

import com.andhraempower.entity.GalleryImage;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ImageRepository extends JpaRepository<GalleryImage, Long> {
    List<GalleryImage> findByDeletedFalse(); // Find non-deleted images
}
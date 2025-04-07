package com.andhraempower.repository;

import com.andhraempower.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByDeletedFalse(); // Find non-deleted images
}
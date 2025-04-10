package com.andhraempower.service;

import com.andhraempower.dto.GalleryDto;
import com.andhraempower.repository.ProjectStatusTrackingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ImageService {

    @Autowired
    private ProjectStatusTrackingRepository projectStatusTrackingRepository;


    // Get all images (only non-deleted)
    public Page<GalleryDto> getAllImages(Long districtId, Long mandalId, Long villageId, Pageable pageable) {
        Page<GalleryDto> dtos = projectStatusTrackingRepository.getGalleryImages(districtId, mandalId, villageId, pageable);
        return dtos;
    }


}
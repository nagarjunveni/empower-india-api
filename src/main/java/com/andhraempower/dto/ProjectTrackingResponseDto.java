package com.andhraempower.dto;

import com.andhraempower.entity.ProjectStatusTracking;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjectTrackingResponseDto extends AbstractProjectTrackingDto{

    private byte[] statusImage;
    private boolean publishToGallery;

    public ProjectTrackingResponseDto(ProjectStatusTracking projectStatusTracking) {
        super(projectStatusTracking);
        this.statusImage = projectStatusTracking.getImage();
        this.publishToGallery = projectStatusTracking.getPublishGallery();
    }
}

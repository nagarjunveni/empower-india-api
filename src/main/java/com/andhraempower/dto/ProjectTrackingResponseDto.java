package com.andhraempower.dto;

import com.andhraempower.entity.ProjectStatusTracking;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjectTrackingResponseDto extends AbstractProjectTrackingDto{

    private byte[] statusImage;

    public ProjectTrackingResponseDto(ProjectStatusTracking projectStatusTracking) {
        super(projectStatusTracking);
        this.statusImage = projectStatusTracking.getImage();
    }
}

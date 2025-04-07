package com.andhraempower.dto;

import com.andhraempower.entity.ProjectStatusTracking;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AbstractProjectTrackingDto {
    private Long id;
    private String status;
    private String createdBy;
    private LocalDateTime createdDate;

    public AbstractProjectTrackingDto(ProjectStatusTracking projectStatusTracking) {
        setId(projectStatusTracking.getId());
        setStatus(projectStatusTracking.getStatus());
        setCreatedBy(projectStatusTracking.getCreatedBy());
        setCreatedDate(projectStatusTracking.getCreatedDate());
    }

}

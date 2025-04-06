package com.andhraempower.dto;

import com.andhraempower.entity.ProjectStatusTracking;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjectTrackingResponseDto {

    private Long id;

    private String status;

    private String createdBy;

    private LocalDateTime createdDate;

    private byte[] profilePhoto;

    public ProjectTrackingResponseDto(ProjectStatusTracking projectStatusTracking) {
        this.id = projectStatusTracking.getId();
        this.status = projectStatusTracking.getStatus();
        this.createdBy = projectStatusTracking.getCreatedBy();
        this.createdDate = projectStatusTracking.getCreatedDate();
    }
}

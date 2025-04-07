package com.andhraempower.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjectTrackingRequestDto extends AbstractProjectTrackingDto {
    private long projectId;

}

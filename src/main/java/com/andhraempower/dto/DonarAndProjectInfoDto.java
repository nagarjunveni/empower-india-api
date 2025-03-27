package com.andhraempower.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonarAndProjectInfoDto {
    private DonarDto donarInfo;
    private List<ProjectInfoDto> projectsInfo;

}

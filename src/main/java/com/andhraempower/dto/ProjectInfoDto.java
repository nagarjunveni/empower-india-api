package com.andhraempower.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectInfoDto {
    private Long donarId;
    private String memoryOf;
    private String modeOfPayment;
    private Double amount;
    private Long projectId;
    private String projectCategory;
    private String projectType;
    private String location;
    private Long villageId;
    private String villageName;
    private Long mandalId;
    private String mandalName;
    private Long districtId;
    private String districtName;
}

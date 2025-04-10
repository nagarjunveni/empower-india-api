package com.andhraempower.dto;

import lombok.Data;

@Data
public class GalleryDto {
    private Long districtId;
    private String districtName;
    private Long mandalId;
    private String mandalName;
    private Long villageId;
    private String villageName;
    private Long projectId;
    private Long projectStatusTrackingId;
    private byte[] statusImage;

    public  GalleryDto(Long districtId, String districtName, Long mandalId, String mandalName, Long villageId, String villageName,
                                   Long projectId, Long projectStatusTrackingId, byte[] statusImage){
        this.districtId = districtId;
        this.villageName = villageName;
        this.villageId = villageId;
        this.mandalName = mandalName;
        this.mandalId = mandalId;
        this.districtName = districtName;
        this.projectId = projectId;
        this.projectStatusTrackingId = projectStatusTrackingId;
        this.statusImage = statusImage;
    }
}

package com.andhraempower.dto;

import lombok.Data;

@Data
public class DistrictDto {

    private String districtName;
    private Long totalCount;
    private Long completed;
    private Long inprogress;
    private Long waitingForSponsor;

    public DistrictDto(String districtName, Long totalCount, Long completed, Long inprogress, Long waitingForSponsor) {
        this.districtName = districtName;
        this.completed = completed;
        this.totalCount = totalCount;
        this.inprogress = inprogress;
        this.waitingForSponsor = waitingForSponsor;
    }

}

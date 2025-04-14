package com.andhraempower.dto;

import lombok.Data;

@Data
public class DistrictDto {

    private String districtName;
    private Long totalCount;
    private Long completed;


    public DistrictDto(String districtName, Long totalCount, Long completed) {
        this.districtName = districtName;
        this.completed = completed;
        this.totalCount = totalCount;
    }

}

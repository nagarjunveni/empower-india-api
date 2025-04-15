package com.andhraempower.dto;

import lombok.Data;

@Data
public class DashBoardStatisticsDTO {
    private Long donorCount;
    private Double villageProjectDonorAmount;
    private Long villageCount;
    private Long totalPopulation;

    public DashBoardStatisticsDTO(Long donorCount, Double villageProjectDonorAmount, Long villageCount, Long totalPopulation) {
        this.donorCount = donorCount;
        this.villageProjectDonorAmount = villageProjectDonorAmount;
        this.villageCount = villageCount;
        this.totalPopulation = totalPopulation;
    }

}
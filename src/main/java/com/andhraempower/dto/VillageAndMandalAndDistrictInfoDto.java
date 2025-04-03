package com.andhraempower.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@ToString
public class VillageAndMandalAndDistrictInfoDto {
    @NonNull
    private Integer districtId;
    private String districtName;
    @NonNull
    private Integer mandalId;
    private String mandalName;
    @NonNull
    private Integer villageId;
    private String villageName;

    private Integer villageProjectId;
    private Long inProgress;
    private Long completed;
    private Long waitingSponsers;
    private Long approvals;
    private Integer villageDemographicsId;
    private Integer totalPopulation;
    private Integer scMalePopulation;
    private Integer scFemalePopulation;
    private Integer bcMalePopulation;
    private Integer bcFemalePopulation;
    private Integer ocMalePopulation;
    private Integer ocFemalePopulation;
    private Integer otherMalePopulation;
    private Integer otherFemalePopulation;


    public VillageAndMandalAndDistrictInfoDto(Integer districtId,String districtName, Integer mandalId,String mandalName,Integer villageId,String villageName,Integer villageProjectId,
                                              Long inProgress,Long completed, Long waitingSponsers,Long approvals,Integer villageDemographicsId,Integer totalPopulation,
                                              Integer scMalePopulation,Integer scFemalePopulation,Integer bcMalePopulation,Integer bcFemalePopulation,Integer ocMalePopulation,
                                              Integer ocFemalePopulation,Integer otherMalePopulation,Integer otherFemalePopulation
        ){
        this.districtId = districtId;
        this.mandalId = mandalId;
        this.villageId = villageId;
        this.districtName = districtName;
        this.mandalName = mandalName;
        this.villageName = villageName;
        this.villageProjectId = villageProjectId;
        this.inProgress = inProgress;
        this.completed = completed;
        this.waitingSponsers = waitingSponsers;
        this.approvals = approvals;
        this.villageDemographicsId = villageDemographicsId;
        this.totalPopulation = totalPopulation;
        this.scMalePopulation = scMalePopulation;
        this.scFemalePopulation = scFemalePopulation;
        this.bcMalePopulation = bcMalePopulation;
        this.bcFemalePopulation = bcFemalePopulation;
        this.ocMalePopulation = ocMalePopulation;
        this.ocFemalePopulation = ocFemalePopulation;
        this.otherMalePopulation = otherMalePopulation;
        this.otherFemalePopulation = otherFemalePopulation;



    }
}

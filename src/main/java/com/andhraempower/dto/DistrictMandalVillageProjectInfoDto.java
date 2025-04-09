package com.andhraempower.dto;

import lombok.Data;

@Data
public class DistrictMandalVillageProjectInfoDto {

    private String districtName;
    private Long districtId;
    private String mandalName;
    private Long mandalId;
    private String villageName;
    private Long villageId;
    private Long villageProjectId;
    private Long inprogress;
    private Long completed;
    private Long waitingSponsers;
    private Long hold;
    private Long draft;
    private Long waitingApprovals;
    private Long villageDemographicsId;
    private Integer totalPopulation;
    private Integer scMalePopulation;
    private Integer scFemalePopulation;
    private Integer stMalePopulation;
    private Integer stFemalePopulation;
    private Integer bcMalePopulation;
    private Integer bcFemalePopulation;
    private Integer ocMalePopulation;
    private Integer ocFemalePopulation;
    private Integer otherMalePopulation;
    private Integer otherFemalePopulation;

    public DistrictMandalVillageProjectInfoDto(Long districtId, String districtName, Long mandalId, String mandalName, Long villageId, String villageName,
                                               Long villageProjectId, Long inprogress, Long completed, Long waitingSponsers, Long hold
                            , Long villageDemographicsId, Integer totalPopulation, Integer scMalePopulation, Integer scFemalePopulation
                            , Integer stMalePopulation, Integer stFemalePopulation, Integer bcMalePopulation, Integer bcFemalePopulation, Integer ocMalePopulation,
                                               Integer ocFemalePopulation, Integer otherMalePopulation, Integer otherFemalePopulation
    ) {
        this.districtName = districtName;
        this.districtId = districtId;
        this.mandalId = mandalId;
        this.mandalName = mandalName;
        this.villageId = villageId;
        this.villageName = villageName;
        this.villageProjectId = villageProjectId;
        this.inprogress = inprogress;
        this.completed = completed;
        this.waitingSponsers = waitingSponsers;
        this.hold = hold;
        this.villageDemographicsId = villageDemographicsId;
        this.totalPopulation = totalPopulation;
        this.scMalePopulation = scMalePopulation;
        this.scFemalePopulation = scFemalePopulation;
        this.stMalePopulation = stMalePopulation;
        this.stFemalePopulation = stFemalePopulation;
        this.bcMalePopulation = bcMalePopulation;
        this.bcFemalePopulation = bcFemalePopulation;
        this.ocMalePopulation = ocMalePopulation;
        this.ocFemalePopulation = ocFemalePopulation;
        this.otherMalePopulation = otherMalePopulation;
        this.otherFemalePopulation = otherFemalePopulation;
    }

    public DistrictMandalVillageProjectInfoDto(Long districtId, String districtName, Long mandalId, String mandalName, Long villageId, String villageName,
                                               Long villageProjectId, Long inprogress, Long completed, Long waitingSponsers, Long hold, Long draft, Long waitingApprovals
            , Long villageDemographicsId, Integer totalPopulation, Integer scMalePopulation, Integer scFemalePopulation
            , Integer stMalePopulation, Integer stFemalePopulation, Integer bcMalePopulation, Integer bcFemalePopulation, Integer ocMalePopulation,
                                               Integer ocFemalePopulation, Integer otherMalePopulation, Integer otherFemalePopulation
    ) {
        this.districtName = districtName;
        this.districtId = districtId;
        this.mandalId = mandalId;
        this.mandalName = mandalName;
        this.villageId = villageId;
        this.villageName = villageName;
        this.villageProjectId = villageProjectId;
        this.inprogress = inprogress;
        this.completed = completed;
        this.waitingSponsers = waitingSponsers;
        this.hold = hold;
        this.draft = draft;
        this.waitingApprovals = waitingApprovals;
        this.villageDemographicsId = villageDemographicsId;
        this.totalPopulation = totalPopulation;
        this.scMalePopulation = scMalePopulation;
        this.scFemalePopulation = scFemalePopulation;
        this.stMalePopulation = stMalePopulation;
        this.stFemalePopulation = stFemalePopulation;
        this.bcMalePopulation = bcMalePopulation;
        this.bcFemalePopulation = bcFemalePopulation;
        this.ocMalePopulation = ocMalePopulation;
        this.ocFemalePopulation = ocFemalePopulation;
        this.otherMalePopulation = otherMalePopulation;
        this.otherFemalePopulation = otherFemalePopulation;
    }
}

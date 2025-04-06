package com.andhraempower.dto;

import com.andhraempower.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VillageDemographicsDTO {

    private Long id;

    private Integer villageId;

    private List<PopulationVillage> populations = new ArrayList<>();

    private List<UnemployeeYouthVillage> unEmployedYouthVillage = new ArrayList<>();

    private List<EmployeeYouthVillage> employedYouthVillage = new ArrayList<>();

    private List<OccupationVillage> occupations = new ArrayList<>();

    private List<LandUtilizationVillage> landUtilizationVillage = new ArrayList<>();

    private List<CultivationCropsVillage> cultivationCropsVillage = new ArrayList<>();
    private List<LiveStockVillage> liveStockVillages = new ArrayList<>();

    private List<InstitutionsVillage> institutionsVillages = new ArrayList<>();

    private Integer noOfHouses;
    private Integer totalPopulation;
    private Integer adultMalePopulation;
    private Integer adultFemalePopulation;
    private Integer childMalePopulation;
    private Integer childFemalePopulation;
    private Integer scMale;
    private Integer scFemale;
    private Integer stMale;
    private Integer stFemale;
    private Integer bcMale;
    private Integer bcFemale;
    private Integer ocMale;
    private Integer ocFemale;
    private Integer otherMale;
    private Integer otherFemale;
    private Float area;
    private Double latitude;
    private Double longitude;
    private Integer pinCode;
    private String boundariesVillage;
    private Integer above60Male;
    private Integer above60Female;
    private String geographicalArea;

    private List<ProjectResponseDto> projectResponseList = new ArrayList<>();

    // Conversion method from Entity to DTO
    public static VillageDemographicsDTO fromEntity(VillageDemographics entity) {
        VillageDemographicsDTO dto = new VillageDemographicsDTO();

        dto.setId(entity.getId());
        dto.setVillageId(entity.getVillageId());

        dto.setPopulations(new ArrayList<>(entity.getPopulations()));
        dto.setUnEmployedYouthVillage(new ArrayList<>(entity.getUnEmployedYouthVillage()));
        dto.setEmployedYouthVillage(new ArrayList<>(entity.getEmployedYouthVillage()));
        dto.setOccupations(new ArrayList<>(entity.getOccupations()));
        dto.setLandUtilizationVillage(new ArrayList<>(entity.getLandUtilizationVillage()));
        dto.setCultivationCropsVillage(new ArrayList<>(entity.getCultivationCropsVillage()));
        dto.setLiveStockVillages(new ArrayList<>(entity.getLiveStockVillages()));
        dto.setInstitutionsVillages(new ArrayList<>(entity.getInstitutionsVillages()));

        dto.setNoOfHouses(entity.getNoOfHouses());
        dto.setTotalPopulation(entity.getTotalPopulation());
        dto.setAdultMalePopulation(entity.getAdultMalePopulation());
        dto.setAdultFemalePopulation(entity.getAdultFemalePopulation());
        dto.setChildMalePopulation(entity.getChildMalePopulation());
        dto.setChildFemalePopulation(entity.getChildFemalePopulation());
        dto.setScMale(entity.getScMale());
        dto.setScFemale(entity.getScFemale());
        dto.setStMale(entity.getStMale());
        dto.setStFemale(entity.getStFemale());
        dto.setBcMale(entity.getBcMale());
        dto.setBcFemale(entity.getBcFemale());
        dto.setOcMale(entity.getOcMale());
        dto.setOcFemale(entity.getOcFemale());
        dto.setOtherMale(entity.getOtherMale());
        dto.setOtherFemale(entity.getOtherFemale());
        dto.setArea(entity.getArea());
        dto.setLatitude(entity.getLatitude());
        dto.setLongitude(entity.getLongitude());
        dto.setPinCode(entity.getPinCode());
        dto.setBoundariesVillage(entity.getBoundariesVillage());
        dto.setAbove60Male(entity.getAbove60Male());
        dto.setAbove60Female(entity.getAbove60Female());
        dto.setGeographicalArea(entity.getGeographicalArea());

        return dto;
    }

}

package com.andhraempower.service;

import com.andhraempower.dto.ProjectResponseDto;
import com.andhraempower.dto.VillageDemographicsDTO;
import com.andhraempower.entity.*;
import com.andhraempower.repository.VillageDetailsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class VillageDetailsService {

    @Autowired
    private VillageDetailsRepository villageDetailsRepository;

    @Autowired
    private ProjectService projectService;

    public VillageDemographics getVillageDetails(Integer villageId) {
        return villageDetailsRepository.findByVillageId(villageId);
    }

    public VillageDemographics addVillageDetails(VillageDemographics villageDemographics) {

        return villageDetailsRepository.save(villageDemographics);
    }


    public VillageDemographics updateVillageDetails(VillageDemographics updatedVillage) {

        Optional<VillageDemographics> entityOptional = villageDetailsRepository.findById(updatedVillage.getId());

        VillageDemographics existingVillage = entityOptional.orElse(null);

        if (existingVillage == null) {
            throw new EntityNotFoundException("Village not found with id: " + updatedVillage.getId());
        }
        if (updatedVillage.getNoOfHouses() != null) {
            existingVillage.setNoOfHouses(updatedVillage.getNoOfHouses());
        }

        if (updatedVillage.getTotalPopulation() != null) {
            existingVillage.setTotalPopulation(updatedVillage.getTotalPopulation());
        }

        if (updatedVillage.getAdultMalePopulation() != null) {
            existingVillage.setAdultMalePopulation(updatedVillage.getAdultMalePopulation());
        }
        if (updatedVillage.getAdultFemalePopulation() != null) {
            existingVillage.setAdultFemalePopulation(updatedVillage.getAdultFemalePopulation());
        }
        if (updatedVillage.getChildMalePopulation() != null) {
            existingVillage.setChildMalePopulation(updatedVillage.getChildMalePopulation());
        }
        if (updatedVillage.getChildFemalePopulation() != null) {
            existingVillage.setChildFemalePopulation(updatedVillage.getChildFemalePopulation());
        }

        if (updatedVillage.getScMale() != null) {
            existingVillage.setScMale(updatedVillage.getScMale());
        }
        if (updatedVillage.getScFemale() != null) {
            existingVillage.setScFemale(updatedVillage.getScFemale());
        }

        if (updatedVillage.getStMale() != null) {
            existingVillage.setStMale(updatedVillage.getStMale());
        }

        if (updatedVillage.getStFemale() != null) {
            existingVillage.setStFemale(updatedVillage.getStFemale());
        }

        if (updatedVillage.getBcMale() != null) {
            existingVillage.setBcMale(updatedVillage.getBcMale());
        }

        if (updatedVillage.getBcFemale() != null) {
            existingVillage.setBcFemale(updatedVillage.getBcFemale());
        }


        if (updatedVillage.getOcMale() != null) {
            existingVillage.setOcMale(updatedVillage.getOcMale());
        }

        if (updatedVillage.getOcFemale() != null) {
            existingVillage.setOcFemale(updatedVillage.getOcFemale());
        }


        if (updatedVillage.getOtherMale() != null) {
            existingVillage.setOtherMale(updatedVillage.getOtherMale());
        }

        if (updatedVillage.getOtherFemale() != null) {
            existingVillage.setOtherFemale(updatedVillage.getOtherFemale());
        }

        if (updatedVillage.getArea() != null) {
            existingVillage.setArea(updatedVillage.getArea());
        }

        if (updatedVillage.getLatitude() != null) {
            existingVillage.setLatitude(updatedVillage.getLatitude());
        }

        if (updatedVillage.getLongitude() != null) {
            existingVillage.setLongitude(updatedVillage.getLongitude());
        }

        if (updatedVillage.getPinCode() != null) {
            existingVillage.setPinCode(updatedVillage.getPinCode());
        }

        if (updatedVillage.getAbove60Male() != null) {
            existingVillage.setAbove60Male(updatedVillage.getAbove60Male());
        }

        if (updatedVillage.getAbove60Female() != null) {
            existingVillage.setAbove60Female(updatedVillage.getAbove60Female());
        }

        if (updatedVillage.getGeographicalArea() != null) {
            existingVillage.setGeographicalArea(updatedVillage.getGeographicalArea());
        }

        if (updatedVillage.getBoundariesVillage() != null) {
            existingVillage.setBoundariesVillage(updatedVillage.getBoundariesVillage());
        }

        if (updatedVillage.getPopulations() != null && !updatedVillage.getPopulations().isEmpty()) {
            for (PopulationVillage updatedPopulation : updatedVillage.getPopulations()) {
                if (updatedPopulation.getId() != null) {
                    Optional<PopulationVillage> existingPopulationOptional = existingVillage.getPopulations().stream()
                            .filter(existingPopulation -> existingPopulation.getId().equals(updatedPopulation.getId()))
                            .findFirst();
                    if (existingPopulationOptional.isPresent()) {
                        existingVillage.getPopulations().remove(existingPopulationOptional.get());
                        existingVillage.getPopulations().add(updatedPopulation);
                    }
                } else {
                    existingVillage.getPopulations().add(updatedPopulation);
                }
            }
        }

        if (updatedVillage.getUnEmployedYouthVillage() != null && !updatedVillage.getUnEmployedYouthVillage().isEmpty()) {
            for (UnemployeeYouthVillage updatedYouth : updatedVillage.getUnEmployedYouthVillage()) {
                if (updatedYouth.getId() != null) {
                    Optional<UnemployeeYouthVillage> existingYouthOptional = existingVillage.getUnEmployedYouthVillage().stream()
                            .filter(existingYouth -> existingYouth.getId().equals(updatedYouth.getId()))
                            .findFirst();
                    if (existingYouthOptional.isPresent()) {
                        existingVillage.getUnEmployedYouthVillage().remove(existingYouthOptional.get());
                        existingVillage.getUnEmployedYouthVillage().add(updatedYouth);
                    }
                } else {
                    existingVillage.getUnEmployedYouthVillage().add(updatedYouth);
                }
            }
        }

        if (updatedVillage.getEmployedYouthVillage() != null && !updatedVillage.getEmployedYouthVillage().isEmpty()) {
            for (EmployeeYouthVillage updatedYouth : updatedVillage.getEmployedYouthVillage()) {
                if (updatedYouth.getId() != null) {
                    Optional<EmployeeYouthVillage> existingYouthOptional = existingVillage.getEmployedYouthVillage().stream()
                            .filter(existingYouth -> existingYouth.getId().equals(updatedYouth.getId()))
                            .findFirst();
                    if (existingYouthOptional.isPresent()) {
                        existingVillage.getEmployedYouthVillage().remove(existingYouthOptional.get());
                        existingVillage.getEmployedYouthVillage().add(updatedYouth);
                    }
                } else {
                    existingVillage.getEmployedYouthVillage().add(updatedYouth);
                }
            }
        }

        if (updatedVillage.getOccupations() != null && !updatedVillage.getOccupations().isEmpty()) {
            for (OccupationVillage updatedOccupation : updatedVillage.getOccupations()) {
                if (updatedOccupation.getId() != null) {
                    Optional<OccupationVillage> existingOccupationOptional = existingVillage.getOccupations().stream()
                            .filter(existingOccupation -> existingOccupation.getId().equals(updatedOccupation.getId()))
                            .findFirst();
                    if (existingOccupationOptional.isPresent()) {
                        existingVillage.getOccupations().remove(existingOccupationOptional.get());
                        existingVillage.getOccupations().add(updatedOccupation);
                    }
                } else {
                    existingVillage.getOccupations().add(updatedOccupation);
                }
            }
        }

        if (updatedVillage.getLandUtilizationVillage() != null && !updatedVillage.getLandUtilizationVillage().isEmpty()) {
            for (LandUtilizationVillage updatedLand : updatedVillage.getLandUtilizationVillage()) {
                if (updatedLand.getId() != null) {
                    Optional<LandUtilizationVillage> existingLandOptional = existingVillage.getLandUtilizationVillage().stream()
                            .filter(existingLand -> existingLand.getId().equals(updatedLand.getId()))
                            .findFirst();
                    if (existingLandOptional.isPresent()) {
                        existingVillage.getLandUtilizationVillage().remove(existingLandOptional.get());
                        existingVillage.getLandUtilizationVillage().add(updatedLand);
                    }
                } else {
                    existingVillage.getLandUtilizationVillage().add(updatedLand);
                }
            }
        }

        if (updatedVillage.getCultivationCropsVillage() != null && !updatedVillage.getCultivationCropsVillage().isEmpty()) {
            for (CultivationCropsVillage updatedCrop : updatedVillage.getCultivationCropsVillage()) {
                if (updatedCrop.getId() != null) {
                    Optional<CultivationCropsVillage> existingCropOptional = existingVillage.getCultivationCropsVillage().stream()
                            .filter(existingCrop -> existingCrop.getId().equals(updatedCrop.getId()))
                            .findFirst();
                    if (existingCropOptional.isPresent()) {
                        existingVillage.getCultivationCropsVillage().remove(existingCropOptional.get());
                        existingVillage.getCultivationCropsVillage().add(updatedCrop);
                    }
                } else {
                    existingVillage.getCultivationCropsVillage().add(updatedCrop);
                }
            }
        }

        if (updatedVillage.getLiveStockVillages() != null && !updatedVillage.getLiveStockVillages().isEmpty()) {
            for (LiveStockVillage updatedLiveStock : updatedVillage.getLiveStockVillages()) {
                if (updatedLiveStock.getId() != null) {
                    Optional<LiveStockVillage> existingLiveStockOptional = existingVillage.getLiveStockVillages().stream()
                            .filter(existingLiveStock -> existingLiveStock.getId().equals(updatedLiveStock.getId()))
                            .findFirst();
                    if (existingLiveStockOptional.isPresent()) {
                        existingVillage.getLiveStockVillages().remove(existingLiveStockOptional.get());
                        existingVillage.getLiveStockVillages().add(updatedLiveStock);
                    }
                } else {
                    existingVillage.getLiveStockVillages().add(updatedLiveStock);
                }
            }
        }

        if (updatedVillage.getInstitutionsVillages() != null && !updatedVillage.getInstitutionsVillages().isEmpty()) {
            for (InstitutionsVillage updatedInstitution : updatedVillage.getInstitutionsVillages()) {
                if (updatedInstitution.getId() != null) {
                    Optional<InstitutionsVillage> existingInstitutionOptional = existingVillage.getInstitutionsVillages().stream()
                            .filter(existingInstitution -> existingInstitution.getId().equals(updatedInstitution.getId()))
                            .findFirst();
                    if (existingInstitutionOptional.isPresent()) {
                        existingVillage.getInstitutionsVillages().remove(existingInstitutionOptional.get());
                        existingVillage.getInstitutionsVillages().add(updatedInstitution);
                    }
                } else {
                    existingVillage.getInstitutionsVillages().add(updatedInstitution);
                }
            }
        }

        return villageDetailsRepository.save(existingVillage);
    }

    public Page<VillageDemographics> findByDistrictId(Long districtId, Pageable pageable) {
        return villageDetailsRepository.findByDistrictId(districtId, pageable);
    }

    public VillageDemographicsDTO villageProjectDetails(Integer villageId) {

        VillageDemographics villageDemographics = villageDetailsRepository.findByVillageId(villageId);
        VillageDemographicsDTO villageDemographicsDTO = new VillageDemographicsDTO();

        Page<ProjectResponseDto> projectResponseDtos = projectService.getProjectsByVillageId(Long.valueOf(villageId));
        if (!projectResponseDtos.isEmpty()) {
            villageDemographicsDTO.setProjectResponseList(projectResponseDtos.getContent());
        }

        if (villageDemographics != null && villageDemographics.getVillageId() != null) {
            villageDemographicsDTO = VillageDemographicsDTO.fromEntity(villageDemographics);
//            Page<ProjectResponseDto> projectResponseDtos = projectService.getProjectsByVillageId(Long.valueOf(villageDemographicsDTO.getVillageId()));
//            if (!projectResponseDtos.isEmpty()) {
//                villageDemographicsDTO.setProjectResponseList(projectResponseDtos.getContent());
//            }
       }

        return villageDemographicsDTO;
    }
}

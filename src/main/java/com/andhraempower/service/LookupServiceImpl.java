package com.andhraempower.service;

import java.util.List;
import java.util.Map;

import com.andhraempower.dto.VillageAndMandalAndDistrictInfoDto;
import com.andhraempower.entity.*;
import org.springframework.stereotype.Service;

import com.andhraempower.dao.LookupDAO;
import com.andhraempower.dto.ProjectCategoriesDto;
import com.andhraempower.repository.ProjectCategoryRepository;


@Service
public class LookupServiceImpl implements LookupService {

    private ProjectCategoryRepository projectCategoryRepository;
    private LookupDAO lookupDAO;

    public LookupServiceImpl(LookupDAO lookupDAO, ProjectCategoryRepository projectCategoryRepository) {
        this.lookupDAO = lookupDAO;
        this.projectCategoryRepository = projectCategoryRepository;
    }

    @Override
    public List<StateLookup> getStates() {
        return this.lookupDAO.getStates();
    }

    @Override
    public List<DistrictLookup> getDistrictsByState(Integer stateId) {
        return this.lookupDAO.getDistrictsByState(stateId);
    }

    @Override
    public List<MandalLookup> getMandalsByDistrict(Integer districtId) {
        return this.lookupDAO.getMandalsByDistrict(districtId);   
    }

    @Override
    public List<VillageLookup> getVillagesByMandal(Integer mandalId) {
        return this.lookupDAO.getVillagesByMandal(mandalId);   
    }

    @Override
    public List<CategoryLookup> getCategories() {
        return this.lookupDAO.getCategories();   
    }

    @Override
    public List<ProjectCategoriesDto> getCategoriesByProjects() {
        return this.projectCategoryRepository.getCategoriesByProjects();   
    }

    @Override
    public List<ProjectTypeLookup> getPrjectTypes() {
        return this.lookupDAO.getProjectTypes();
    }

    @Override
    public Map<String, List<?>> getVillageLooksUps() {
        return Map.of("community", this.lookupDAO.getCommunityLookUps(),
                "occupation", this.lookupDAO.getOccupationLookUps(),
                "landUtilization", this.lookupDAO.getLandUtilizationLookup(),
                "cultivationCrops", this.lookupDAO.getCultivationCropsLookup(),
                "liveStock", this.lookupDAO.getLiveStockLookUp(),
                "institutions", this.lookupDAO.getInstitutionsLookups());
    }

    @Override
    public List<VillageAndMandalAndDistrictInfoDto> getVillageAndMandalAndDistrictInfo(Integer page,Integer pageSize) {
        return this.lookupDAO.getVillageAndMandalAndDistrictInfo(page,pageSize);
    }

    @Override
    public long getTotalVillageCount() {
        return this.lookupDAO.getTotalVillageCount();
    }


}

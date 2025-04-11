package com.andhraempower.service;

import java.util.List;
import java.util.Map;

import com.andhraempower.dto.ProjectCategoriesDto;
import com.andhraempower.entity.*;

public interface LookupService {

    List<StateLookup> getStates();

    List<DistrictLookup> getDistrictsByState(Integer stateId);

    List<MandalLookup> getMandalsByDistrict(Integer districtId);

    List<VillageLookup> getVillagesByMandal(Integer mandalId);

    List<CategoryLookup> getCategories();

    List<ProjectCategoriesDto> getCategoriesByProjects();

    List<ProjectTypeLookup> getPrjectTypes();

    Map<String, List<?>> getVillageLooksUps();

    List<ProjectStatusLookup> getProjectStatusLookup();

}

package com.andhraempower.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.andhraempower.dto.VillageAndMandalAndDistrictInfoDto;
import com.andhraempower.entity.*;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

@Repository
public class LookupDAOImpl implements LookupDAO {

    private EntityManager entityManager;

    @Autowired
    public LookupDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<StateLookup> getStates() {
        TypedQuery<StateLookup> theQuery = entityManager.createQuery("FROM StateLookup order by name", StateLookup.class);
        return theQuery.getResultList();

    }

    @Override
    public List<DistrictLookup> getDistrictsByState(Integer stateId) {
        TypedQuery<DistrictLookup> theQuery = entityManager.createQuery("FROM DistrictLookup dl where dl.stateId = :stateId order by dl.name", DistrictLookup.class);
        theQuery.setParameter("stateId", stateId);
        return theQuery.getResultList();
    }

    @Override
    public List<MandalLookup> getMandalsByDistrict(Integer districtId) {
        TypedQuery<MandalLookup> theQuery = entityManager.createQuery("FROM MandalLookup ml where ml.districtId = :districtId order by ml.name", MandalLookup.class);
        theQuery.setParameter("districtId", districtId);
        return theQuery.getResultList();
    }

    @Override
    public List<VillageLookup> getVillagesByMandal(Integer mandalId) {
        TypedQuery<VillageLookup> theQuery = entityManager.createQuery("FROM VillageLookup vl where vl.mandalId = :mandalId order by vl.name", VillageLookup.class);
        theQuery.setParameter("mandalId", mandalId);
        return theQuery.getResultList();
    }

    @Override
    public List<CategoryLookup> getCategories() {

        // create query

        TypedQuery<CategoryLookup> query = entityManager.createQuery(
                "select i from CategoryLookup i "
                        + "LEFT JOIN FETCH i.projects ", CategoryLookup.class);

        // execute query
        List<CategoryLookup> categories = query.getResultList();
        return categories;
    }

    @Override
    public Optional<Integer> getVillageProposalIdByVillageId(Integer villageId) {
        TypedQuery<Integer> query = entityManager.createQuery(
                "SELECT vp.id FROM VillageProposal vp WHERE vp.villageId = :villageId", Integer.class
        );
        query.setParameter("villageId", villageId);

        List<Integer> result = query.getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public Optional<VillageLookup> getVillageById(Integer villageId) {
        TypedQuery<VillageLookup> query = entityManager.createQuery(
                "SELECT v FROM VillageLookup v WHERE v.id = :villageId", VillageLookup.class
        );
        query.setParameter("villageId", villageId);

        List<VillageLookup> result = query.getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public Optional<CategoryLookup> getCategorybyId(Integer categoryId) {
        TypedQuery<CategoryLookup> query = entityManager.createQuery(
                "SELECT v FROM CategoryLookup v WHERE v.id = :categoryid", CategoryLookup.class
        );
        query.setParameter("categoryid", categoryId);

        List<CategoryLookup> result = query.getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public List<ProjectTypeLookup> getProjectTypes() {
        TypedQuery<ProjectTypeLookup> theQuery = entityManager.createQuery("FROM ProjectTypeLookup order by id"
                , ProjectTypeLookup.class);
        return theQuery.getResultList();
    }

    @Override
    public List<CommunityLookUp> getCommunityLookUps() {
        TypedQuery<CommunityLookUp> theQuery = entityManager.createQuery("FROM CommunityLookUp order by name", CommunityLookUp.class);
        return theQuery.getResultList();
    }

    @Override
    public List<OccupationLookUp> getOccupationLookUps() {
        TypedQuery<OccupationLookUp> theQuery = entityManager.createQuery("FROM OccupationLookUp order by name", OccupationLookUp.class);
        return theQuery.getResultList();
    }

    @Override
    public List<LandUtilizationLookup> getLandUtilizationLookup() {
        TypedQuery<LandUtilizationLookup> theQuery = entityManager.createQuery("FROM LandUtilizationLookup order by name", LandUtilizationLookup.class);
        return theQuery.getResultList();
    }

    @Override
    public List<CultivationCropsLookUps> getCultivationCropsLookup() {
        TypedQuery<CultivationCropsLookUps> theQuery = entityManager.createQuery("FROM CultivationCropsLookUps order by name", CultivationCropsLookUps.class);
        return theQuery.getResultList();
    }

    @Override
    public List<LiveStockLookUp> getLiveStockLookUp() {
        TypedQuery<LiveStockLookUp> theQuery = entityManager.createQuery("FROM LiveStockLookUp order by name", LiveStockLookUp.class);
        return theQuery.getResultList();
    }

    @Override
    public List<InstitutionsLookups> getInstitutionsLookups() {
        TypedQuery<InstitutionsLookups> theQuery = entityManager.createQuery("FROM InstitutionsLookups order by name", InstitutionsLookups.class);
        return theQuery.getResultList();
    }

    @Override
    public long getTotalVillageCount() {
        return entityManager.createQuery("select count(*) FROM VillageLookup", Long.class).getSingleResult();
    }

    private static List<VillageAndMandalAndDistrictInfoDto> getVillageAndMandalAndDistrictInfoDtos(List<Object[]> resultList) {
        List<VillageAndMandalAndDistrictInfoDto> dtoList = new ArrayList<>();
        for (Object[] result : resultList) {
            Integer districtId = result[0] == null ? 0 : (Integer) result[0];
            String districtName = result[1] == null ? null : (String) result[1];
            Integer mandalId = result[2] == null ? 0 : (Integer) result[2];
            String mandalName = result[3] == null ? null : (String) result[3];
            Integer villageId = result[4] == null ? 0 : (Integer) result[4];
            String villageName = result[5] == null ? null : (String) result[5];
            Integer villageProjectId = result[6] == null ? 0 : (Integer) result[6];
            Long inProgress = result[7] == null ? 0 : (Long) result[7];
            Long completed = result[8] == null ? 0 : (Long) result[8];
            Long waitingSponsers = result[9] == null ? 0 : (Long) result[9];
            Long approvals = result[10] == null ? 0 : (Long) result[10];
            Integer villageDemographicsId = result[11] == null ? 0 : (Integer) result[11];
            Integer totalPopulation = result[12] == null ? 0 : (Integer) result[12];
            Integer scMalePopulation = result[13] == null ? 0 : (Integer) result[13];
            Integer scFemalePopulation = result[14] == null ? 0 : (Integer) result[14];
            Integer bcMalePopulation = result[15] == null ? 0 : (Integer) result[15];
            Integer bcFemalePopulation = result[16] == null ? 0 : (Integer) result[16];
            Integer ocMalePopulation = result[17] == null ? 0 : (Integer) result[17];
            Integer ocFemalePopulation = result[18] == null ? 0 : (Integer) result[18];
            Integer otherMalePopulation = result[19] == null ? 0 : (Integer) result[19];
            Integer otherFemalePopulation = result[20] == null ? 0 : (Integer) result[20];
            VillageAndMandalAndDistrictInfoDto dto = new VillageAndMandalAndDistrictInfoDto(districtId,districtName, mandalId,mandalName,villageId,villageName,villageProjectId,
                    inProgress,completed, waitingSponsers,approvals,villageDemographicsId,totalPopulation,
                    scMalePopulation,scFemalePopulation,bcMalePopulation,bcFemalePopulation,ocMalePopulation,
                    ocFemalePopulation,otherMalePopulation,otherFemalePopulation);
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public List<VillageAndMandalAndDistrictInfoDto> getVillageAndMandalAndDistrictInfo(Integer page,Integer pageSize) {

        String sql = "select dl.id as district_id,dl.name as district_name,ml.id as mandal_id,ml.name as mandal_name,vl.id as village_id,vl.name as village_name,vp.village_id as village_project_id, "+
                "COUNT(CASE WHEN vp.status_code like lower('%wip%') THEN 1 END) AS in_progress, "+
                "COUNT(CASE WHEN vp.status_code like lower('%completed%') THEN 1 END) AS completed, "+
                "COUNT(CASE WHEN vp.status_code like lower('%wfd%') THEN 1 END) AS waiting_sponsers, "+
                "COUNT(CASE WHEN vp.status_code like lower('%new%') THEN 1 END) AS approvals, "+
                "vd.id as village_demographics_id, vd.total_population as total_population, vd.sc_male as sc_male_population, "+
                "vd.sc_female as sc_female_population, "+
                "vd.bc_male as bc_male_population,vd.bc_female as bc_female_population, vd.oc_male as oc_male_population, vd.oc_female as oc_female_population, "+
                "vd.other_male as other_male_population, vd.other_female as other_female_population from district_lookup dl "+
                "LEFT join mandal_lookup ml on dl.id = ml.district_id "+
                "LEFT join village_lookup vl on ml.id =vl.mandal_id "+
                "LEFT join village_project vp on vl.id = vp.village_id "+
                "LEFT join village_demographics vd on vl.id = vd.village_id "+
                "group by dl.id,ml.id,vp.village_id,vl.id,vd.id ";


        // Create the query
        Query query = entityManager.createNativeQuery(sql);

        int firstResult = (page - 1) * pageSize; // calculate offset
        query.setFirstResult(firstResult);       // set offset
        query.setMaxResults(pageSize);
        List<Object[]> resultList = query.getResultList();
        List<VillageAndMandalAndDistrictInfoDto> dtoList = getVillageAndMandalAndDistrictInfoDtos(resultList);
        return dtoList;
    }

}

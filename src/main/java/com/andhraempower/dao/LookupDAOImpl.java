package com.andhraempower.dao;

import java.util.List;
import java.util.Optional;

import com.andhraempower.entity.*;
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
}

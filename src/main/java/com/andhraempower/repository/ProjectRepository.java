package com.andhraempower.repository;

import com.andhraempower.dto.ProjectResponseDto;
import com.andhraempower.entity.VillageProject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<VillageProject, Long> {

    @Query("SELECT new com.andhraempower.dto.ProjectResponseDto( " +
            "vp.id, vp.projectCategory.name,vp.projectCategory.id, vp.projectTypeLookup.description,vp.projectTypeLookup.id," +
            " vp.statusCode, vp.location, " +
            "vp.latitude, vp.longitude, vp.projectEstimation, vp.governmentShare, vp.publicShare, " +
            "vp.isNew, vp.description, vp.createdBy, vp.lastUpdatedBy, " +
            "vl.name,vl.id, ml.name,ml.id, dl.name,dl.id, vl.pinCode, vp.committeeAdded, vp.bankAccountAdded, vp.estimationCompleted,vp.estimateStartDate, vp.estimateEndDate,vp.actualStartDate,vp.actualEndDate, vp.statusCode ) " +
            "FROM VillageProject vp " +
            "JOIN VillageLookup vl ON vp.village.id = vl.id " +
            "JOIN MandalLookup ml ON vl.mandalId = ml.id " +
            "JOIN DistrictLookup dl ON ml.districtId = dl.id")
    Page<ProjectResponseDto> findAllProjects(Pageable pageable);

    @Query("SELECT new com.andhraempower.dto.ProjectResponseDto( " +
            "vp.id, vp.projectCategory.name,vp.projectCategory.id, vp.projectTypeLookup.description,vp.projectTypeLookup.id, vp.status, vp.location, " +
            "vp.latitude, vp.longitude, vp.projectEstimation, vp.governmentShare, vp.publicShare, " +
            "vp.isNew, vp.description, vp.createdBy, vp.lastUpdatedBy, " +
            "vl.name,vl.id, ml.name,ml.id, dl.name,dl.id, vl.pinCode, vp.committeeAdded, vp.bankAccountAdded, vp.estimationCompleted,vp.estimateStartDate, vp.estimateEndDate,vp.actualStartDate,vp.actualEndDate, vp.statusCode ) " +
            "FROM VillageProject vp " +
            "JOIN VillageLookup vl ON vp.village.id = vl.id " +
            "JOIN MandalLookup ml ON vl.mandalId = ml.id " +
            "JOIN DistrictLookup dl ON ml.districtId = dl.id " +
            "WHERE (:districtId IS NULL OR dl.id = :districtId) " +
            "AND (:mandalId IS NULL OR ml.id = :mandalId) " +
            "AND (:villageId IS NULL OR vl.id = :villageId) " +
            "AND (:statusCode IS NULL OR vp.statusCode = :statusCode) " +
            "AND (:projectTypeId IS NULL OR vp.projectTypeLookup.id = :projectTypeId)"
    )
    Page<ProjectResponseDto> searchProjects(@Param("districtId") Long districtId,
   @Param("mandalId") Long mandalId, @Param("villageId") Long villageId,
   @Param("projectTypeId") Long projectTypeId, @Param("statusCode") String statusCode, Pageable pageable);

    @Query("SELECT new com.andhraempower.dto.ProjectResponseDto( " +
            "vp.id, vp.projectCategory.name,vp.projectCategory.id, vp.projectTypeLookup.description,vp.projectTypeLookup.id," +
            " vp.status, vp.location, " +
            "vp.latitude, vp.longitude, vp.projectEstimation, vp.governmentShare, vp.publicShare, " +
            "vp.isNew, vp.description, vp.createdBy, vp.lastUpdatedBy, " +
            "vl.name,vl.id, ml.name,ml.id, dl.name,dl.id, vl.pinCode, vp.committeeAdded, vp.bankAccountAdded, vp.estimationCompleted,vp.estimateStartDate, vp.estimateEndDate,vp.actualStartDate,vp.actualEndDate,vp.statusCode ) " +
            "FROM VillageProject vp " +
            "JOIN VillageLookup vl ON vp.village.id = vl.id " +
            "JOIN MandalLookup ml ON vl.mandalId = ml.id " +
            "JOIN DistrictLookup dl ON ml.districtId = dl.id " +
    "WHERE vp.projectTypeLookup.id = :id")
    Page<ProjectResponseDto> findByProjectTypeLookupId(@Param("id") Long id, Pageable pageable);

    @Query("SELECT new com.andhraempower.dto.ProjectResponseDto( " +
            "vp.id, vp.projectCategory.name,vp.projectCategory.id, vp.projectTypeLookup.description,vp.projectTypeLookup.id," +
            " vp.status, vp.location, " +
            "vp.latitude, vp.longitude, vp.projectEstimation, vp.governmentShare, vp.publicShare, " +
            "vp.isNew, vp.description, vp.createdBy, vp.lastUpdatedBy, " +
            "vl.name,vl.id, ml.name,ml.id, dl.name,dl.id, vl.pinCode, vp.committeeAdded, vp.bankAccountAdded, vp.estimationCompleted,vp.estimateStartDate, vp.estimateEndDate,vp.actualStartDate,vp.actualEndDate, vp.statusCode ) " +
            "FROM VillageProject vp " +
            "JOIN VillageLookup vl ON vp.village.id = vl.id " +
            "JOIN MandalLookup ml ON vl.mandalId = ml.id " +
            "JOIN DistrictLookup dl ON ml.districtId = dl.id " +
            "WHERE vp.statusCode = :statusCode")
    Page<ProjectResponseDto> findByStatus(@Param("statusCode") String statusCode, Pageable pageable);

    Optional<VillageProject> findById(Long id);

    @Query("SELECT new com.andhraempower.dto.ProjectResponseDto( " +
            "vp.id, vp.projectCategory.name,vp.projectCategory.id, vp.projectTypeLookup.description,vp.projectTypeLookup.id," +
            " vp.status, vp.location, " +
            "vp.latitude, vp.longitude, vp.projectEstimation, vp.governmentShare, vp.publicShare, " +
            "vp.isNew, vp.description, vp.createdBy, vp.lastUpdatedBy, " +
            "vl.name,vl.id, ml.name,ml.id, dl.name,dl.id, vl.pinCode, vp.committeeAdded, vp.bankAccountAdded, vp.estimationCompleted,vp.estimateStartDate, vp.estimateEndDate,vp.actualStartDate,vp.actualEndDate,vp.statusCode ) " +
            "FROM VillageProject vp " +
            "JOIN VillageLookup vl ON vp.village.id = vl.id " +
            "JOIN MandalLookup ml ON vl.mandalId = ml.id " +
            "JOIN DistrictLookup dl ON ml.districtId = dl.id " +
            "WHERE vp.id = :id")
    ProjectResponseDto findByProjectId(Long id);




}
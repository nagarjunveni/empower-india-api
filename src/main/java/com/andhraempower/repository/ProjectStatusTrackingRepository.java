package com.andhraempower.repository;

import com.andhraempower.dto.GalleryDto;
import com.andhraempower.entity.ProjectStatusTracking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectStatusTrackingRepository extends JpaRepository<ProjectStatusTracking, Long> {

    List<ProjectStatusTracking> findByProjectId(Long projectId);

    @Query("SELECT new com.andhraempower.dto.GalleryDto(" +
            "dl.id ,dl.name,ml.id,ml.name, vl.id,vl.name,  " +
            "vp.id, pst.id, pst.image) "+
            "FROM ProjectStatusTracking pst "+
            "join VillageProject vp on vp.id = pst.projectId " +
            " join VillageLookup vl on vp.village.id = vl.id " +
            "JOIN MandalLookup ml ON vl.mandalId = ml.id " +
            "JOIN DistrictLookup dl ON ml.districtId = dl.id " +
            "WHERE pst.publishGallery = true " +
            "AND (:districtId IS NULL OR dl.id = :districtId) " +
            "AND (:mandalId IS NULL OR ml.id = :mandalId) " +
            "AND (:villageId IS NULL OR vl.id = :villageId) ")
    Page<GalleryDto> getGalleryImages(@Param("districtId") Long districtId, @Param("mandalId") Long mandalId
            , @Param("villageId") Long villageId, Pageable pageable);
}

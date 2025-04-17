package com.andhraempower.repository;
import com.andhraempower.dto.DonarDto;
import com.andhraempower.dto.DashBoardStatisticsDTO;
import com.andhraempower.dto.ProjectInfoDto;
import com.andhraempower.entity.Donar;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DonarsRepository extends JpaRepository<Donar, Long> {

  @Query("SELECT new com.andhraempower.dto.DonarDto( " +
          "d.id, d.image, d.firstName, d.lastName, d.phoneNumber, d.email, d.description, d.address, vl.id, vl.name, ml.id, ml.name, dl.id, dl.name, SUM(vpl.amount), " +
          "(SELECT sc.categoryName FROM SponsorCategory sc " +
          "WHERE SUM(vpl.amount) >= sc.minAmount " +
          "AND (sc.maxAmount IS NULL OR SUM(vpl.amount) <= sc.maxAmount) " +
          "AND sc.minAmount = (SELECT MAX(sc2.minAmount) FROM SponsorCategory sc2 " +
          "WHERE SUM(vpl.amount) >= sc2.minAmount) " +
          ") " +
          ") " +
          "FROM VillageProjectDonar vpl " +
          "JOIN Donar d ON vpl.donar.id = d.id " +
          "LEFT JOIN VillageLookup vl ON vpl.villageProjectId = vl.id " +
          "LEFT JOIN MandalLookup ml ON vl.mandalId = ml.id " +
          "LEFT JOIN DistrictLookup dl ON ml.districtId = dl.id "+
          "WHERE  d.image IS NOT NULL " +
          "AND (:districtId IS NULL OR dl.id = :districtId)  "+
          "AND (:mandalId IS NULL OR ml.id = :mandalId) " +
          "AND (:villageId IS NULL OR vl.id = :villageId) " +
          "GROUP BY d.id, d.image, d.firstName, d.lastName, d.phoneNumber, d.email, d.address, vl.id, vl.name, ml.id, ml.name, dl.id, dl.name " +
          "ORDER BY SUM(vpl.amount) DESC")
  Page<DonarDto> findDonars(@Param("districtId") Long districtId, @Param("mandalId") Long mandalId, @Param("villageId") Long villageId, Pageable pageable);

  @Query("SELECT new com.andhraempower.dto.DonarDto( " +
          "d.id, d.image, d.firstName, d.lastName, d.phoneNumber, d.email, d.description, d.address, vl.id, vl.name, ml.id, ml.name, dl.id, dl.name, SUM(vpl.amount), " +
          "(SELECT sc.categoryName FROM SponsorCategory sc " +
          "WHERE SUM(vpl.amount) >= sc.minAmount " +
          "AND (sc.maxAmount IS NULL OR SUM(vpl.amount) <= sc.maxAmount) " +
          "AND sc.minAmount = (SELECT MAX(sc2.minAmount) FROM SponsorCategory sc2 " +
          "WHERE SUM(vpl.amount) >= sc2.minAmount) " +
          ") " +
          ") " +
          "FROM VillageProjectDonar vpl " +
          "JOIN Donar d ON vpl.donar.id = d.id " +
          "LEFT JOIN VillageLookup vl ON d.village.id = vl.id " +
          "LEFT JOIN MandalLookup ml ON vl.mandalId = ml.id " +
          "LEFT JOIN DistrictLookup dl ON ml.districtId = dl.id "+
          "WHERE (d.id = :donarId)  "+
          "GROUP BY d.id, d.image, d.firstName, d.lastName, d.phoneNumber, d.email, d.address, vl.id, vl.name, ml.id, ml.name, dl.id, dl.name ")
  DonarDto findDonarInfo(
          @Param("donarId") Long donarId
  );

  @Query("SELECT new com.andhraempower.dto.ProjectInfoDto( " +
          "vpd.donar.id, vpd.memoryOf, vpd.modeOfPayment, vpd.amount, "+
          "vp.id, cl.name, ptl.description, "+
          "vp.location, vl.id, vl.name, ml.id, ml.name, dl.id, dl.name ) "+
          "FROM VillageProjectDonar vpd " +
          //"JOIN Donar d ON vpd.donar.id = d.id " +
          "JOIN VillageProject vp ON vp.id = vpd.villageProjectId "+
          "LEFT JOIN CategoryLookup cl ON vp.projectCategory.id = cl.id " +
          "LEFT JOIN ProjectTypeLookup ptl ON vp.projectTypeLookup.id = ptl.id " +
          "LEFT JOIN VillageLookup vl ON vp.village.id = vl.id " +
          "LEFT JOIN MandalLookup ml ON vl.mandalId = ml.id " +
          "LEFT JOIN DistrictLookup dl ON ml.districtId = dl.id "+
          "WHERE (vpd.donar.id = :donarId) " +
          "ORDER BY vp.id DESC")
  List<ProjectInfoDto> findProjectInfo(
          @Param("donarId") Long donarId
  );

  @Query("SELECT d FROM Donar d WHERE "
          + "(d.firstName LIKE CONCAT('%', :searchTerm, '%')) OR "
          + "(d.lastName LIKE CONCAT('%', :searchTerm, '%')) OR "
          + "(d.phoneNumber LIKE CONCAT('%', :searchTerm, '%')) OR "
          + "(d.email LIKE CONCAT('%', :searchTerm, '%'))")
  List<Donar> searchDonors(@Param("searchTerm") String searchTerm);



  @Query("SELECT new com.andhraempower.dto.DashBoardStatisticsDTO(" +
          "COUNT(d.id), SUM(vpd.amount), COUNT(vp.id), SUM(vd.totalPopulation)) " +
          "FROM Donar d " +
          "LEFT JOIN VillageProjectDonar vpd ON vpd.donar.id = d.id " +
          "LEFT JOIN VillageProject vp ON vp.id = vpd.villageProjectId AND vp.isDeleted = 0 " +
          "LEFT JOIN VillageDemographics vd ON vp.village.id = vd.villageId")
  DashBoardStatisticsDTO getDashBoardCounts();


}
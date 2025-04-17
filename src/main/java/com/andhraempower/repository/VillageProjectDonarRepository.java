package com.andhraempower.repository;

import com.andhraempower.dto.DonarDto;
import com.andhraempower.entity.VillageProjectCommitteeMembers;
import com.andhraempower.entity.VillageProjectDonar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VillageProjectDonarRepository extends JpaRepository<VillageProjectDonar, Long> {

    List<VillageProjectDonar> getByVillageProjectId(Long projectId);

    @Modifying
    @Query("DELETE FROM VillageProjectDonar vpd where vpd.villageProjectId = :projectId AND vpd.donar.id = :donarId")
    void deleteByIdAndVillageProjectId(@Param("donarId") Long id, @Param("projectId") Long villageProjectId);

    VillageProjectDonar findByDonarId(Long id);
}

package com.andhraempower.repository;

import com.andhraempower.entity.VillageProjectSignOff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VillageProjectSignOffRepository extends JpaRepository<VillageProjectSignOff, Integer> {

    List<VillageProjectSignOff> findByProjectId(Integer projectId);
}

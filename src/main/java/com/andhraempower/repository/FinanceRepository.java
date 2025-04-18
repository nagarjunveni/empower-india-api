package com.andhraempower.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.andhraempower.entity.Finance;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FinanceRepository extends JpaRepository<Finance, Integer> {


    List<Finance> findByVillageProjectId(Long projectId);

    Optional<Finance> findById(Long id);

    void deleteByIdAndVillageProjectId(Long transactionId, Long projectId);
}


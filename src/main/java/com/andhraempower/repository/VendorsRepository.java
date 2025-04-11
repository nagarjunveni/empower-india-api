package com.andhraempower.repository;

import com.andhraempower.entity.Vendors;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VendorsRepository extends JpaRepository<Vendors, Long> {


    List<Vendors> findByProjectId(Long projectId);
}

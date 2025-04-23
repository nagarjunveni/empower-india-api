package com.andhraempower.service;

import com.andhraempower.entity.Vendors;
import com.andhraempower.entity.VillageProject;
import com.andhraempower.exception.RequiredFieldMissingException;
import com.andhraempower.repository.ProjectRepository;
import com.andhraempower.repository.VendorsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
@Transactional
public class VendorsService {

    @Autowired
    private VendorsRepository vendorsRepository;

    @Autowired
    private ProjectRepository villageProjectRepository;

    public Vendors addVendors(Vendors vendors) {
        if (vendors.getId() != null) {
            Vendors dbVendor = vendorsRepository.findById(vendors.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Vendor not found with ID: " + vendors.getId()));

            updateNonNullFields(dbVendor, vendors);
            return vendorsRepository.save(dbVendor);

        } else if (vendors.getProjectId() != null) {
            villageProjectRepository.findById(vendors.getProjectId())
                    .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + vendors.getProjectId()));

            return vendorsRepository.save(vendors);
        }

        throw new RequiredFieldMissingException("Vendor ID or Project ID must be provided.");
    }

    private void updateNonNullFields(Vendors target, Vendors source) {
        if (source.getName() != null) target.setName(source.getName());
        if (source.getAddress() != null) target.setAddress(source.getAddress());
        if (source.getContractorName() != null) target.setContractorName(source.getContractorName());
        if (source.getPhone() != null) target.setPhone(source.getPhone());
        if (source.getIsSupplier() != null) target.setIsSupplier(source.getIsSupplier());
        if (source.getMaterial() != null) target.setMaterial(source.getMaterial());
    }


    public List<Vendors> getVendors(Long projectId) {
        return vendorsRepository.findByProjectId(projectId);
    }

    public boolean deleteVendor(Long vendorId) {
        if (vendorId != null) {
            vendorsRepository.deleteById(vendorId);
            return true;
        }
        return false;
    }
}

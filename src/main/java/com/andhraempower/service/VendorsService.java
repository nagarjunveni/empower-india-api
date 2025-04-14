package com.andhraempower.service;

import com.andhraempower.entity.Vendors;
import com.andhraempower.entity.VillageProject;
import com.andhraempower.repository.ProjectRepository;
import com.andhraempower.repository.VendorsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
@Slf4j
public class VendorsService {
    @Autowired
    private VendorsRepository vendorsRepository;
    @Autowired
    private ProjectRepository villageProjectRepository;

    public ResponseEntity<String>  addVendors(Vendors vendors) {
        if(vendors.getProjectId() != null){
            Optional<VillageProject> vp = villageProjectRepository.findById(vendors.getProjectId());
            if(vp.isPresent()){
                 vendorsRepository.save(vendors);
                return ResponseEntity.ok("New Vendor saved successfully!");
            }else{
                return ResponseEntity.ok("projectId are not available!");
            }
        }
        return ResponseEntity.ok("Vendor details/projectId are not available!");
    }

    public List<Vendors> getVendors(Long projectId) {
        return vendorsRepository.findByProjectId(projectId);
    }

    public boolean deleteVendor(Long vendorId) {
        if(vendorId != null ){
            vendorsRepository.deleteById(vendorId);
            return true;
        }
        return false;
    }
}

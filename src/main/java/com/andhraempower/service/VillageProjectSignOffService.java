package com.andhraempower.service;

import com.andhraempower.entity.VillageProjectSignOff;
import com.andhraempower.repository.VillageProjectSignOffRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class VillageProjectSignOffService {

    @Autowired
    private VillageProjectSignOffRepository repository;

    public List<VillageProjectSignOff> getAll() {
        return repository.findAll();
    }

    public VillageProjectSignOff getById(Integer id) {
        return (repository.findById(id).isPresent()) ? repository.findById(id).get() : new VillageProjectSignOff();
    }

    public VillageProjectSignOff create(VillageProjectSignOff signOff) {
        return repository.save(signOff);
    }

    public VillageProjectSignOff update(Integer id, VillageProjectSignOff updated) {
        VillageProjectSignOff existing = getById(id);

        if(updated.getFirstName() != null) {
            existing.setFirstName(updated.getFirstName());
        }

        if(updated.getLastName() != null) {
            existing.setLastName(updated.getLastName());
        }
        if(updated.getPhoneNumber() != null) {
            existing.setPhoneNumber(updated.getPhoneNumber());
        }

        if(updated.getEmail() != null) {
            existing.setEmail(updated.getEmail());
        }

        if(updated.getEmail() != null) {
            existing.setEmail(updated.getEmail());
        }
        return repository.save(existing);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    public List<VillageProjectSignOff> findByProjectId(Integer projectId) {
        return repository.findByProjectId(projectId);
    }

}

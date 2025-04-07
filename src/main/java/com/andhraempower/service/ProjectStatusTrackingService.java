package com.andhraempower.service;

import com.andhraempower.dto.ProjectTrackingRequestDto;
import com.andhraempower.entity.ProjectStatusTracking;
import com.andhraempower.repository.ProjectStatusTrackingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProjectStatusTrackingService {

    @Autowired
    private ProjectStatusTrackingRepository projectStatusTrackingRepository;

    public List<ProjectStatusTracking> getProjectStatus(long projectId) {
        return projectStatusTrackingRepository.findByProjectId(projectId);
    }

    public ProjectStatusTracking createProjectStatus(ProjectTrackingRequestDto projectTrackingRequestDto, MultipartFile file) throws IOException {
        ProjectStatusTracking projectStatusTracking = new ProjectStatusTracking();
        return updateAndSave(projectTrackingRequestDto, file, projectStatusTracking);
    }

    public ProjectStatusTracking updateProjectStatus(ProjectTrackingRequestDto projectTrackingRequestDto, MultipartFile file) throws IOException {
        if (projectStatusTrackingRepository.findById(projectTrackingRequestDto.getId()).isPresent()){
            ProjectStatusTracking projectStatusTracking = new ProjectStatusTracking();
            projectStatusTracking.setId(projectTrackingRequestDto.getId());
            return updateAndSave(projectTrackingRequestDto, file, projectStatusTracking);
        }
        throw new RuntimeException("Project status with given Id does not exists.");
    }

    public void deleteProjectStatus(long id){
        projectStatusTrackingRepository.deleteById(id);
    }

    private ProjectStatusTracking updateAndSave(ProjectTrackingRequestDto projectTrackingRequestDto, MultipartFile file, ProjectStatusTracking projectStatusTracking) throws IOException {
        projectStatusTracking.setStatus(projectTrackingRequestDto.getStatus());
        projectStatusTracking.setProjectId(projectTrackingRequestDto.getProjectId());
        projectStatusTracking.setCreatedBy(projectTrackingRequestDto.getCreatedBy());
        LocalDateTime createdDate = projectTrackingRequestDto.getCreatedDate();
        if(createdDate == null){
            createdDate = LocalDateTime.now();
        }

        projectStatusTracking.setCreatedDate(createdDate);

        if (file != null && !file.isEmpty()) {
            projectStatusTracking.setImage(file.getBytes());
        }

        return projectStatusTrackingRepository.saveAndFlush(projectStatusTracking);
    }
}

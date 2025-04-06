package com.andhraempower.service;

import com.andhraempower.entity.ProjectStatusTracking;
import com.andhraempower.repository.ProjectStatusTrackingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectStatusTrackingService {

    @Autowired
    private ProjectStatusTrackingRepository projectStatusTrackingRepository;

    public List<ProjectStatusTracking> getProjectStatus(long projectId){
        return projectStatusTrackingRepository.findByProjectId(projectId);
    }
}

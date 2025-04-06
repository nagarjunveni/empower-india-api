package com.andhraempower.service;

import com.andhraempower.constants.StatusEnum;
import com.andhraempower.dao.LookupDAO;
import com.andhraempower.dto.*;
import com.andhraempower.entity.*;
import com.andhraempower.events.StatusChangeEvent;
import com.andhraempower.events.StatusChangePublisher;
import com.andhraempower.repository.ProjectRepository;
import com.andhraempower.repository.VillageProjectDonarRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.andhraempower.constants.EmpowerConstants.USER_ADMIN;
import static com.andhraempower.constants.ProjectWorkFlowStatus.ESTIMATION_ADDED;
import static com.andhraempower.constants.ProjectWorkFlowStatus.NEW_PROJECT_CREATED;

@AllArgsConstructor
@Service
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final LookupDAO lookupDAO;
    private final StatusChangePublisher statusChangePublisher;
    @Autowired
    private VillageProjectDonarRepository villageProjectDonarRepository;

    @Autowired
    private CommitteeService committeeService;


    public void saveProject(ProjectRequestDto projectRequestDto) {
        log.info("Saving new project: {}", projectRequestDto);
        Optional<VillageLookup> village = getVillageLookup(projectRequestDto.getVillageId());
        Optional<CategoryLookup> category = getCategoryLookup(projectRequestDto.getProjectCategoryId());
        VillageProject project = getVillageProject(projectRequestDto, category, village);
        if(projectRequestDto.getProjectEstimation() != null &&
                projectRequestDto.getProjectEstimation() > 0) {
            project.setStatusCode(StatusEnum.WFD.name());
        } else {
            project.setStatusCode(StatusEnum.NEW.name());
        }
        VillageProject villageProject = projectRepository.save(project);
        log.info("New Project saved successfully!");
        statusChangePublisher.publishStatusChange(new StatusChangeEvent(villageProject.getId(), NEW_PROJECT_CREATED, USER_ADMIN, LocalDateTime.now()));
    }
    public void updateProject(ProjectRequestDto projectRequestDto) {
        projectRepository.findById(projectRequestDto.getId())
                .ifPresentOrElse(project -> {
                    Double existingProjectEstimation = project.getProjectEstimation();
                    projectRepository.save(getUpdatedProject(project, projectRequestDto));
                    if(!existingProjectEstimation.equals(projectRequestDto.getProjectEstimation())) {
                        statusChangePublisher.publishStatusChange(new StatusChangeEvent(project.getId(), ESTIMATION_ADDED, USER_ADMIN, LocalDateTime.now()));
                    }
                    log.info("New Project Updated successfully!");
                }, () -> {throw new IllegalArgumentException("Project Not found for the given Id : " + projectRequestDto.getId());});
    }

    private Optional<CategoryLookup> getCategoryLookup(Integer categoryId) {
        return lookupDAO.getCategorybyId(categoryId);
    }

    private Optional<VillageLookup> getVillageLookup(Integer villageId) {
        return lookupDAO.getVillageById(villageId);
    }

    public Page<ProjectResponseDto> getProjects(Pageable pageable) {
        log.info("Fetching all projects details.");
        Page<ProjectResponseDto> allProjects = projectRepository.findAllProjects(pageable);
        allProjects.stream().forEach(this::setAdditonalDetailsToProjectResponse);
        return allProjects;
    }

    private void setAdditonalDetailsToProjectResponse(ProjectResponseDto projectResponseDto) {
        projectResponseDto.setStatus(StatusEnum.valueOf(projectResponseDto.getStatus().toUpperCase()).getStatusDescription());
        Double sponsereddAmount = 0d;
        List<VillageProjectDonar> byVillageProjectId = villageProjectDonarRepository.getByVillageProjectId(projectResponseDto.getId());
        if(!CollectionUtils.isEmpty(byVillageProjectId)){
            List<DonarDto> sponserList =  byVillageProjectId.stream().map(donar -> {
                DonarDto.DonarDtoBuilder donarDtoBuilder = DonarDto.builder().id(donar.getId())
                        .firstName(donar.getDonar().getFirstName()).lastName(donar.getDonar().getLastName())
                        .phoneNumber(donar.getDonar().getPhoneNumber()).email(donar.getDonar().getEmail())
                        .address(donar.getDonar().getAddress()).amount(donar.getAmount()).modeOfPayment(donar.getModeOfPayment());
                if(donar.getDonar().getVillage() != null) {
                    donarDtoBuilder.villageId(donar.getDonar().getVillage().getId()).villageName(donar.getDonar().getVillage().getName());
                }
                return donarDtoBuilder.build();
            }).toList();
            projectResponseDto.setSponsersList(sponserList);
            sponsereddAmount = byVillageProjectId.stream().mapToDouble(VillageProjectDonar::getAmount).sum();
            if(projectResponseDto.getProjectEstimation() != null) {
                projectResponseDto.setRemainingRequiredAmount(projectResponseDto.getProjectEstimation() - sponsereddAmount);
            }
        } else {
            projectResponseDto.setRemainingRequiredAmount(projectResponseDto.getProjectEstimation());
            projectResponseDto.setSponsersList(new ArrayList<>());
        }

        List<CommitteeMembers> committeeMembersList =  committeeService.getCommittee(projectResponseDto.getId());

        if (committeeMembersList != null) {
            projectResponseDto.setCommitteeMembersList(
                    committeeMembersList.stream()
                            .map(this::convertEntityToDto)
                            .collect(Collectors.toList())
            );
        } else {
            projectResponseDto.setCommitteeMembersList(new ArrayList<>());
        }

    }

    public CommitteeMembersDto convertEntityToDto(CommitteeMembers committeeMember) {
        CommitteeMembersDto dto = new CommitteeMembersDto();

        dto.setId(committeeMember.getId());
        dto.setFirstName(committeeMember.getFirstName());
        dto.setLastName(committeeMember.getLastName());
        dto.setFatherName(committeeMember.getFatherName());
        dto.setPhoneNumber(committeeMember.getPhoneNumber());
        dto.setEmail(committeeMember.getEmail());
        dto.setAddress(committeeMember.getAddress());
        dto.setRecordType(committeeMember.getRecordType());
        dto.setVillageId(committeeMember.getVillageId());

        return dto;
    }

    public Page<ProjectResponseDto> searchProjects(Long districtCode, Long mandalCode, Long villageCode,Long id, String statusCode, Pageable pageable) {
        log.info("searchProjectsByDistrictMandalVillageCode districtCode {}, mandalCode{}, villageCode {}", districtCode, mandalCode, villageCode);
        Page<ProjectResponseDto> searchedProjects = projectRepository.searchProjects(districtCode, mandalCode, villageCode,id,statusCode, pageable);
        searchedProjects.stream().forEach(this::setAdditonalDetailsToProjectResponse);
        return searchedProjects;
    }

    private VillageProject getUpdatedProject(VillageProject villageProject, ProjectRequestDto projectRequestDto){
        villageProject.setLocation(projectRequestDto.getLocation());
        villageProject.setLatitude(projectRequestDto.getLatitude());
        villageProject.setLongitude(projectRequestDto.getLongitude());
        villageProject.setIsNew("New".equalsIgnoreCase(projectRequestDto.getProjectNeed()));
        villageProject.setGovernmentShare(projectRequestDto.getGovernmentShare());
        villageProject.setProjectEstimation(projectRequestDto.getProjectEstimation());
        villageProject.setPublicShare(projectRequestDto.getPublicShare());
        villageProject.setDescription(projectRequestDto.getDescription());
        villageProject.setEstimateStartDate(projectRequestDto.getEstimateStartDate());
        villageProject.setEstimateEndDate(projectRequestDto.getEstimateEndDate());
        villageProject.setActualStartDate(projectRequestDto.getActualStartDate());
        villageProject.setActualEndDate(projectRequestDto.getActualEndDate());

        if(!villageProject.getVillage().getId().equals(projectRequestDto.getVillageId())) {
            Optional<VillageLookup> villageLookup = getVillageLookup(projectRequestDto.getVillageId());
            if(villageLookup.isEmpty()){
                throw new IllegalArgumentException("Invalid Village Id : " + projectRequestDto.getVillageId());
            }
            villageProject.setVillage(villageLookup.get());
        }

        if(!villageProject.getProjectCategory().getId().equals(projectRequestDto.getProjectCategoryId())) {
            Optional<CategoryLookup> categoryLookup = getCategoryLookup(projectRequestDto.getProjectCategoryId());
            if(categoryLookup.isEmpty()) {
                throw new IllegalArgumentException("Invalid Category Id : " + projectRequestDto.getProjectCategoryId());
            }
            villageProject.setProjectCategory(categoryLookup.get());
            villageProject.setProjectTypeLookup(getProjectTypeLookup(projectRequestDto, categoryLookup));
        }
        return villageProject;

    }

    private static VillageProject getVillageProject(ProjectRequestDto projectRequestDto
            , Optional<CategoryLookup> category, Optional<VillageLookup> village) {
        if (village.isEmpty()) {
            throw new IllegalArgumentException("Invalid Village Id : " + projectRequestDto.getVillageId());
        }
        if(category.isEmpty()) {
            throw new IllegalArgumentException("Invalid Category Id : " + projectRequestDto.getProjectCategoryId());
        }
        ProjectTypeLookup projectTypeLookup = getProjectTypeLookup(projectRequestDto, category);
        return VillageProject.builder()
                .location(projectRequestDto.getLocation())
                .latitude(projectRequestDto.getLatitude())
                .longitude(projectRequestDto.getLongitude())
                .projectCategory(category.get())
                .projectTypeLookup(projectTypeLookup)
                .isNew("New".equalsIgnoreCase(projectRequestDto.getProjectNeed()))
                .projectEstimation(projectRequestDto.getProjectEstimation())
                .governmentShare(projectRequestDto.getGovernmentShare())
                .publicShare(projectRequestDto.getPublicShare())
                .description(projectRequestDto.getDescription())
                .village(village.get())
                .status("Open")
                .createdBy("Admin")
                .lastUpdatedBy("Admin")
                .estimateStartDate(projectRequestDto.getEstimateStartDate())
                .estimateEndDate(projectRequestDto.getEstimateEndDate())
                .actualStartDate(projectRequestDto.getActualStartDate())
                .actualEndDate(projectRequestDto.getActualEndDate())
                .build();
    }

    private static ProjectTypeLookup getProjectTypeLookup(ProjectRequestDto projectRequestDto, Optional<CategoryLookup> category) {
        return category.get().getProjects().stream().filter(projectType -> projectType.getId().equals(Long.parseLong(projectRequestDto.getProjectType())))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Invalid Project Type Id : " + projectRequestDto.getProjectCategoryId()));
    }

    public ProjectsCountDto getProjectsCount() {
        long count= projectRepository.count();
        ProjectsCountDto dto = new ProjectsCountDto();
        dto.setCount(count);
        return dto;
    }

    public Page<ProjectResponseDto> getProjectsByProjectType(Long projectTypeId, Pageable pageable) {
        Page<ProjectResponseDto> searchedProjects = projectRepository.findByProjectTypeLookupId(projectTypeId, pageable);
        searchedProjects.stream().forEach(this::setAdditonalDetailsToProjectResponse);
        return searchedProjects;
    }

    public Page<ProjectResponseDto> getProjectsByProjectStatus(String status, Pageable pageable) {
        Page<ProjectResponseDto> searchedProjects = projectRepository.findByStatus(status, pageable);
        searchedProjects.stream().forEach(this::setAdditonalDetailsToProjectResponse);
        return searchedProjects;
    }

    public void saveProjectStatusSteps(ProjectStatusSteps projectStatusSteps, Long projectId) {
        Optional.ofNullable(projectRepository.findById(projectId))
                .ifPresentOrElse(project -> {
                    VillageProject villageProject = project.get();
                    villageProject.setCommitteeAdded(projectStatusSteps.isCommitteeFormed());
                    villageProject.setEstimationCompleted(projectStatusSteps.isEstimationAdded());
                    villageProject.setBankAccountAdded(projectStatusSteps.isBankDetailsAdded());
                    projectRepository.save(villageProject);
                }, () -> {throw new IllegalArgumentException("Project not found for the project id "+ projectId);});

    }

    public void publishProject(ProjectStatusSteps projectStatusSteps, Long projectId) {
        if(!projectStatusSteps.isBankDetailsAdded() || !projectStatusSteps.isEstimationAdded() || !projectStatusSteps.isCommitteeFormed()) {
            throw new IllegalArgumentException("You can not publish the project untill all the steps are completed");
        }
        Optional.ofNullable(projectRepository.findById(projectId))
                .ifPresentOrElse(project -> {
                    VillageProject villageProject = project.get();
                    villageProject.setCommitteeAdded(projectStatusSteps.isCommitteeFormed());
                    villageProject.setEstimationCompleted(projectStatusSteps.isEstimationAdded());
                    villageProject.setBankAccountAdded(projectStatusSteps.isBankDetailsAdded());
                    villageProject.setStatusCode(StatusEnum.WFD.name());
                    villageProject.setStatus(StatusEnum.WFD.name());
                    projectRepository.save(villageProject);
                }, () -> {throw new IllegalArgumentException("Project not found for the project id "+ projectId);});

    }

    public void kickOffProject(Long projectId) {
        Optional.ofNullable(projectRepository.findById(projectId))
                .ifPresentOrElse(project -> {
                    VillageProject villageProject = project.get();
                    villageProject.setStatusCode(StatusEnum.WIP.name());
                    villageProject.setStatus(StatusEnum.WIP.name());
                    projectRepository.save(villageProject);
                }, () -> {
                    throw new EntityNotFoundException("Project not found for the project id " + projectId);
                });
    }


    public ProjectResponseDto getProjectById(String id) {
        log.info("searchProjectBy ID {}", id);
        ProjectResponseDto searchedProject = projectRepository.findByProjectId(Long.valueOf(id));
        if(searchedProject != null){
            setAdditonalDetailsToProjectResponse(searchedProject);
        }
        return  searchedProject;
    }

    public Page<DistrictMandalVillageProjectInfoDto> getDistrictMandalVillageProjects(Long districtId, Long mandalId, Long projectTypeId, String status, Pageable pageable) {
        log.info("Fetching all projects details.");
        Page<DistrictMandalVillageProjectInfoDto> allProjects = projectRepository.getDistrictMandalVillageProjects(districtId,mandalId,projectTypeId,status,pageable);
        return allProjects;
    }
}

package com.andhraempower.service;

import com.andhraempower.dao.LookupDAO;
import com.andhraempower.dto.DonarAndProjectInfoDto;
import com.andhraempower.dto.DonarDto;
import com.andhraempower.dto.ProjectInfoDto;
import com.andhraempower.entity.*;
import com.andhraempower.events.StatusChangeEvent;
import com.andhraempower.events.StatusChangePublisher;
import com.andhraempower.repository.DonarsRepository;
import com.andhraempower.repository.ProjectRepository;
import com.andhraempower.repository.VillageProjectDonarRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import static com.andhraempower.constants.EmpowerConstants.USER_ADMIN;
import static com.andhraempower.constants.ProjectWorkFlowStatus.SPONSOR_ADDED;

@AllArgsConstructor
@Service
@Slf4j
public class DonarsService {

    @Autowired
    private DonarsRepository donarsRepository;

    @Autowired
    private LookupDAO lookupDAO;

    @Autowired
    private VillageProjectDonarRepository villageProjectDonarRepository;

    @Autowired
    private ProjectRepository projectRepository;

    private final StatusChangePublisher statusChangePublisher;



    public Donar addDonars(DonarDto donars, MultipartFile file) throws IOException {
        Donar donar = donars.fromDto();

        if (file != null && !file.isEmpty()) {
            donar.setImage(file.getBytes());
        }

        if(donars.getVillageId() != null && donars.getVillageId() > 0 ) {
            Optional<VillageLookup> villageById = lookupDAO.getVillageById(donars.getVillageId().intValue());
            villageById.ifPresent(donar::setVillage);
        }
        return donarsRepository.save(donar);
    }

    public void associateDonarToProject(Long projectId, Donar donar, Double amount, String modeOfPayment, String memoryOf){
        Optional.ofNullable(projectId).ifPresent(id -> {
            VillageProjectDonar villageProjectDonar = new VillageProjectDonar();
            villageProjectDonar.setDonar(donar);
            villageProjectDonar.setVillageProjectId(projectId);
            villageProjectDonar.setAmount(amount);
            villageProjectDonar.setModeOfPayment(modeOfPayment);
            villageProjectDonar.setMemoryOf(memoryOf);
            villageProjectDonar.setCreatedBy(USER_ADMIN);
            villageProjectDonarRepository.save(villageProjectDonar);
            statusChangePublisher.publishStatusChange(new StatusChangeEvent(projectId, SPONSOR_ADDED, USER_ADMIN, LocalDateTime.now()));
        });


    }

    public List<Donar> getDonars(Long projectId) {
        return Optional.ofNullable(projectId)
                .map(id -> {
                    List<VillageProjectDonar> byVillageProjectId = villageProjectDonarRepository.getByVillageProjectId(id);
                    if(byVillageProjectId != null && !byVillageProjectId.isEmpty()){
                        return byVillageProjectId.stream().map(VillageProjectDonar::getDonar).collect(Collectors.toList());
                    }
                    return new ArrayList<Donar>();
                }).orElse(donarsRepository.findAll());
    }

    @Transactional
    public void removeCommitteeMemberFromProject(Long committeeId, Long projectId){
        villageProjectDonarRepository.deleteByIdAndVillageProjectId(committeeId, projectId);
    }

    public List<DonarDto> getDonars(Integer topN) {
        Pageable pageable = (topN != null && topN > 0) ? PageRequest.of(0, topN) : Pageable.unpaged();
        return donarsRepository.findDonars(pageable);
    }

    public DonarAndProjectInfoDto getDonarInfo(Long donarId) {
        DonarAndProjectInfoDto donarAndProjectInfo = new DonarAndProjectInfoDto();
        donarAndProjectInfo.setDonarInfo(donarsRepository.findDonarInfo(donarId));
        donarAndProjectInfo.setProjectsInfo(donarsRepository.findProjectInfo(donarId));
        return donarAndProjectInfo;
    }

    public List<DonarDto> searchDonors(String searchTerm) {
        return convertToDTOList(donarsRepository.searchDonors(searchTerm));
    }

    public List<DonarDto> convertToDTOList(List<Donar> donars) {
        return donars.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DonarDto convertToDTO(Donar donar) {
        if (donar == null) {
            return null;
        }
        return DonarDto.builder()
                .id(donar.getId())
                .firstName(donar.getFirstName())
                .lastName(donar.getLastName())
                .phoneNumber(donar.getPhoneNumber())
                .email(donar.getEmail())
                .address(donar.getAddress())
                .build();
    }
}
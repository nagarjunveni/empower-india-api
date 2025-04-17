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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
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

    private static final String[] units = {
            "", "One", "Two", "Three", "Four", "Five",
            "Six", "Seven", "Eight", "Nine", "Ten", "Eleven",
            "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen",
            "Seventeen", "Eighteen", "Nineteen"
    };

    private static final String[] tens = {
            "", "", "Twenty", "Thirty", "Forty", "Fifty",
            "Sixty", "Seventy", "Eighty", "Ninety"
    };

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

        VillageProjectDonar villageProjectDonar = villageProjectDonarRepository.findByDonarId(donar.getId());

        if(villageProjectDonar == null){
            villageProjectDonar = new VillageProjectDonar();
        }
        VillageProjectDonar finalVillageProjectDonar = villageProjectDonar;
        Optional.ofNullable(projectId).ifPresent(id -> {
            finalVillageProjectDonar.setDonar(donar);
            finalVillageProjectDonar.setVillageProjectId(projectId);
            finalVillageProjectDonar.setAmount(amount);
            finalVillageProjectDonar.setModeOfPayment(modeOfPayment);
            finalVillageProjectDonar.setMemoryOf(memoryOf);
            finalVillageProjectDonar.setCreatedBy(USER_ADMIN);
            villageProjectDonarRepository.save(finalVillageProjectDonar);
            statusChangePublisher.publishStatusChange(new StatusChangeEvent(projectId, SPONSOR_ADDED, USER_ADMIN, LocalDateTime.now()));
        });


    }

    public List<DonarDto> getDonars(Long projectId) {

        return Optional.ofNullable(projectId)
                .map(id -> {
                    List<VillageProjectDonar> byVillageProjectId = villageProjectDonarRepository.getByVillageProjectId(id);
                    if(byVillageProjectId != null && !byVillageProjectId.isEmpty()){
                        return byVillageProjectId.stream().map(villageProjectDonar -> new DonarDto(
                                villageProjectDonar.getDonar().getId(),villageProjectDonar.getDonar().getImage(),villageProjectDonar.getDonar().getFirstName(),villageProjectDonar.getDonar().getLastName(),
                                villageProjectDonar.getDonar().getPhoneNumber(),villageProjectDonar.getDonar().getEmail(),villageProjectDonar.getDonar().getDescription(),
                                villageProjectDonar.getDonar().getAddress(),villageProjectDonar.getAmount(),villageProjectDonar.getMemoryOf(),villageProjectDonar.getModeOfPayment()
                        )).collect(Collectors.toList());
                    }
                    return new ArrayList<DonarDto>();
                }).orElse(donarsRepository.findAll().stream().map(donar -> new DonarDto(
                        donar.getId(),donar.getImage(),donar.getFirstName(),donar.getLastName(),
                        donar.getPhoneNumber(),donar.getEmail(),donar.getDescription(),
                        donar.getAddress(),null
                )).collect(Collectors.toList()));
    }

    @Transactional
    public void removeCommitteeMemberFromProject(Long committeeId, Long projectId){
        villageProjectDonarRepository.deleteByIdAndVillageProjectId(committeeId, projectId);
    }

    public Page<DonarDto> getDonars(Long districtId, Long mandalId, Long villageId, Integer page, Integer size, Integer topN) {
        Pageable pageable;

        if (topN != null && topN > 0) {
            pageable = PageRequest.of(0, topN,  Sort.unsorted());
        } else {
            pageable = PageRequest.of(page, size, Sort.unsorted());
        }

        Page<DonarDto> donarList = donarsRepository.findDonars(districtId,mandalId,villageId,pageable);
        for(DonarDto donarDto : donarList){
            String amountInText = convertAmountToText(donarDto.getAmount());
            donarDto.setAmountText(amountInText);
        }
        return donarList;
    }

    public DonarAndProjectInfoDto getDonarInfo(Long donarId) {
        DonarAndProjectInfoDto donarAndProjectInfo = new DonarAndProjectInfoDto();
        donarAndProjectInfo.setDonarInfo(donarsRepository.findDonarInfo(donarId));
        donarAndProjectInfo.getDonarInfo().setAmountText(convertAmountToText(donarAndProjectInfo.getDonarInfo().getAmount()));
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

    private static String convertToWords(int n, String suffix) {
        String str = "";
        if (n > 19) {
            str += tens[n / 10] + " " + units[n % 10];
        } else {
            str += units[n];
        }

        if (!str.isEmpty()) {
            str += " " + suffix + " ";
        }
        return str;
    }

    public static String convertAmountToText(double amount) {
        long num = (long) amount;
        int paise = (int) Math.round((amount - num) * 100);

        String result = "";

        result += convertToWords((int) (num / 10000000), "Crore");
        result += convertToWords((int) ((num / 100000) % 100), "Lakh");
        result += convertToWords((int) ((num / 1000) % 100), "Thousand");
        result += convertToWords((int) ((num / 100) % 10), "Hundred");

        if (num > 100 && (num % 100) > 0) {
            result += "and ";
        }

        result += convertToWords((int) (num % 100), "");

        result = result.trim() + " Rupees";

        if (paise > 0) {
            result += " and " + convertToWords(paise, "") + " Paise";
        }

        return result.trim();
    }
}
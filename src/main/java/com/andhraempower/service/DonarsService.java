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
            "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"
    };
    private static final String[] tens = {
            "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"
    };
    private static final String[] powers = { "", "Thousand", "Lakh", "Crore" };

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

    public List<DonarDto> getDonars(Long projectId) {

        return Optional.ofNullable(projectId)
                .map(id -> {
                    List<VillageProjectDonar> byVillageProjectId = villageProjectDonarRepository.getByVillageProjectId(id);
                    if(byVillageProjectId != null && !byVillageProjectId.isEmpty()){
                        return byVillageProjectId.stream().map(villageProjectDonar -> new DonarDto(
                                villageProjectDonar.getId(),villageProjectDonar.getDonar().getImage(),villageProjectDonar.getDonar().getFirstName(),villageProjectDonar.getDonar().getLastName(),
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

    public Page<DonarDto> getDonars(Long districtId, Long mandalId, Long villageId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.unsorted());

        Page<DonarDto> donarList = donarsRepository.findDonars(districtId,mandalId,villageId,pageable);
        for(DonarDto donarDto : donarList){
            String amountInText = donorAmountUpdate(donarDto.getAmount());
            donarDto.setAmountText(amountInText);
        }
        return donarList;
    }

    public DonarAndProjectInfoDto getDonarInfo(Long donarId) {
        DonarAndProjectInfoDto donarAndProjectInfo = new DonarAndProjectInfoDto();
        donarAndProjectInfo.setDonarInfo(donarsRepository.findDonarInfo(donarId));
        donarAndProjectInfo.getDonarInfo().setAmountText(donorAmountUpdate(donarAndProjectInfo.getDonarInfo().getAmount()));
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

    private String donorAmountUpdate(double amount) {

        if (amount == 0) {
            return "Zero Rupees";
        }

        // Split into integer and fractional parts
        int integerPart = (int) amount;
        int fractionalPart = (int) ((amount - integerPart) * 100);

        // Convert integer part and fractional part to text
        String integerText = convertIntegerToText(integerPart,0) + " rupees" ;


        // Return final result
        return integerText;
    }

    private static String convertIntegerToText(int number, int placeIndex) {
        if (number == 0) {
            return "";
        }

        StringBuilder result = new StringBuilder();

        // Process groups of 3 digits
        while (number > 0) {
            if (number % 1000 >= 0 && placeIndex == 0) { // Thousand
                result.insert(0, convertLessThanThousand(number % 1000) + " " + powers[placeIndex] + " ");
                number /= 1000;
            }else if(number % 100 >= 0 && placeIndex > 0) { // lakh & crore
                //System.out.println( result.insert(0, convertLessThanThousand(number % 1000) + " " + powers[placeIndex] + " "));

                String text = convertLessThanThousand(number % 100);
                result.insert(0, text.trim() != "" ? (text + powers[placeIndex] + " " ): "");
                number /= 100;
            }

            placeIndex++;
        }

        return result.toString().trim();
    }

    // Converts numbers less than 1000 to words (handles hundreds, tens, and units)
    private static String convertLessThanThousand(int number) {
        StringBuilder result = new StringBuilder();

        if (number >= 100) {
            result.append(units[number / 100]).append(" Hundred ");
            number %= 100;
        }

        // Handling numbers from 10-19 (teen numbers)
        if (number >= 10 && number <= 19) {
            result.append(units[number]).append(" ");
        } else if (number >= 20) { // Handling numbers 20-99
            result.append(tens[number / 10]).append(" ");
            number %= 10;
        }else if (number > 0 && number < 10) {
            result.append(units[number]).append(" ");
        }

//	        if(number > 0) {
//	        	result.append(units[number]).append(" ");
//	        }

        return result.toString();
    }
}
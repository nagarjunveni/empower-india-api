package com.andhraempower.controller;

import java.util.List;
import java.util.Map;

import com.andhraempower.entity.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.andhraempower.constants.EmpowerConstants;
import com.andhraempower.dto.ProjectCategoriesDto;
import com.andhraempower.service.LookupService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/lookup")
public class LookupController {

    private final LookupService lookupService;

    public LookupController(LookupService lookupService) {
        this.lookupService = lookupService;
    }

    @GetMapping("/states")
    public ResponseEntity<List<StateLookup>> getStates() {
        return ResponseEntity.ok().body(lookupService.getStates());
    }

    @GetMapping("/districts")
    public ResponseEntity<List<DistrictLookup>> getDistrictsByState(@RequestParam("stateId") Integer stateId) {
        if (stateId == null || stateId <= 0) {
            return ResponseEntity.badRequest().body(null); 
        }
        return ResponseEntity.ok().body(lookupService.getDistrictsByState(stateId));
    }

    @GetMapping("/mandals")
    public ResponseEntity<List<MandalLookup>> getMandalsByDistrict(@RequestParam("districtId") Integer districtId) {
        if (districtId == null || districtId <= 0) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok().body(lookupService.getMandalsByDistrict(districtId));
    }

    @GetMapping("/villages")
    public ResponseEntity<List<VillageLookup>> getVillagesByMandal(@RequestParam("mandalId") Integer mandalId) {
        if (mandalId == null || mandalId <= 0) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok().body(lookupService.getVillagesByMandal(mandalId));
    }

    @GetMapping("/project-categories")
    public ResponseEntity<List<CategoryLookup>> getCategories() {
        return ResponseEntity.ok().body(lookupService.getCategories());
    }

    @GetMapping(value = "/projects/categories",produces = {EmpowerConstants.APPLICATION_JSON})
    @Operation(summary = "List of Categories in VillageProject")
    @ApiResponses(value = {
            @ApiResponse(responseCode = EmpowerConstants.SUCCESS_CODE, description = EmpowerConstants.SUCCESS_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.BAD_REQUEST_CODE, description = EmpowerConstants.BAD_REQUEST_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNAUTHORIZED_CODE, description = EmpowerConstants.UNAUTHORIZED_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.FORBIDDEN_CODE, description = EmpowerConstants.FORBIDDEN_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.RESOURCE_NOT_FOUND_CODE, description = EmpowerConstants.RESOURCE_NOT_FOUND_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE, description = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE_DESC)
    })
    public ResponseEntity<List<ProjectCategoriesDto>> getCategoriesByProjects() {
        return ResponseEntity.ok().body(lookupService.getCategoriesByProjects());
    }

    @GetMapping("/project-types")
    public ResponseEntity<List<ProjectTypeLookup>> getProjectTypes() {
        return ResponseEntity.ok().body(lookupService.getPrjectTypes());
    }

    @GetMapping(value = "/village-lookups",produces = {EmpowerConstants.APPLICATION_JSON})
    @Operation(summary = "List of Categories in VillageProject")
    @ApiResponses(value = {
            @ApiResponse(responseCode = EmpowerConstants.SUCCESS_CODE, description = EmpowerConstants.SUCCESS_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.BAD_REQUEST_CODE, description = EmpowerConstants.BAD_REQUEST_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNAUTHORIZED_CODE, description = EmpowerConstants.UNAUTHORIZED_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.FORBIDDEN_CODE, description = EmpowerConstants.FORBIDDEN_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.RESOURCE_NOT_FOUND_CODE, description = EmpowerConstants.RESOURCE_NOT_FOUND_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE, description = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE_DESC)
    })
    public ResponseEntity<Map<String,List<?>>> getVillageLooksUps() {
        return ResponseEntity.ok().body(lookupService.getVillageLooksUps());
    }


    @GetMapping(value = "/project-status",produces = {EmpowerConstants.APPLICATION_JSON})
    @Operation(summary = "List of project status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = EmpowerConstants.SUCCESS_CODE, description = EmpowerConstants.SUCCESS_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.BAD_REQUEST_CODE, description = EmpowerConstants.BAD_REQUEST_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNAUTHORIZED_CODE, description = EmpowerConstants.UNAUTHORIZED_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.FORBIDDEN_CODE, description = EmpowerConstants.FORBIDDEN_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.RESOURCE_NOT_FOUND_CODE, description = EmpowerConstants.RESOURCE_NOT_FOUND_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE, description = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE_DESC)
    })
    public ResponseEntity<List<ProjectStatusLookup>> getProjectStatus() {
        return ResponseEntity.ok().body(lookupService.getProjectStatusLookup());
    }

}


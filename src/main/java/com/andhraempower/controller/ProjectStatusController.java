package com.andhraempower.controller;

import com.andhraempower.constants.EmpowerConstants;
import com.andhraempower.dto.ProjectTrackingRequestDto;
import com.andhraempower.dto.ProjectTrackingResponseDto;
import com.andhraempower.entity.ProjectStatusTracking;
import com.andhraempower.service.ProjectStatusTrackingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/project/status")
public class ProjectStatusController {

    @Autowired
    private ProjectStatusTrackingService projectStatusTrackingService;

    @Operation(summary = "Get all project status for a given project")
    @GetMapping("{projectId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Resource not found"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error")
    })
    public ResponseEntity<List<ProjectTrackingResponseDto>> getProjectStatus(@PathVariable Long projectId) {
        List<ProjectStatusTracking> projectStatus = projectStatusTrackingService.getProjectStatus(projectId);
        return ResponseEntity.ok(projectStatus.stream()
                .map(ProjectTrackingResponseDto::new)
                .toList());
    }

    @Operation(summary = "Create a new project status tracking")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiResponses(value = {
            @ApiResponse(responseCode = EmpowerConstants.SUCCESS_CODE, description = EmpowerConstants.SUCCESS_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.BAD_REQUEST_CODE, description = EmpowerConstants.BAD_REQUEST_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNAUTHORIZED_CODE, description = EmpowerConstants.UNAUTHORIZED_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.FORBIDDEN_CODE, description = EmpowerConstants.FORBIDDEN_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.RESOURCE_NOT_FOUND_CODE, description = EmpowerConstants.RESOURCE_NOT_FOUND_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE, description = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE_DESC)
    })
    public ResponseEntity<ProjectTrackingResponseDto> createProjectStatusTracking(
            @RequestPart("projectStatus") ProjectTrackingRequestDto projectTrackingRequestDto,
            @RequestPart(value = "images", required = false) MultipartFile file) throws IOException {
        ProjectStatusTracking projectStatusTracking = projectStatusTrackingService.createProjectStatus(projectTrackingRequestDto, file);
        return ResponseEntity.ok(new ProjectTrackingResponseDto(projectStatusTracking));
    }

    @Operation(summary = "Updates the given project status tracking")
    @PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiResponses(value = {
            @ApiResponse(responseCode = EmpowerConstants.SUCCESS_CODE, description = EmpowerConstants.SUCCESS_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.BAD_REQUEST_CODE, description = EmpowerConstants.BAD_REQUEST_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNAUTHORIZED_CODE, description = EmpowerConstants.UNAUTHORIZED_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.FORBIDDEN_CODE, description = EmpowerConstants.FORBIDDEN_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.RESOURCE_NOT_FOUND_CODE, description = EmpowerConstants.RESOURCE_NOT_FOUND_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE, description = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE_DESC)
    })
    public ResponseEntity<ProjectTrackingResponseDto> updateProjectStatusTracking(
            @RequestPart("projectStatus") ProjectTrackingRequestDto projectTrackingRequestDto,
            @RequestPart(value = "images", required = false) MultipartFile file) throws IOException {
        if(projectTrackingRequestDto.getId() == null ){
            return ResponseEntity.badRequest().build();
        }
        ProjectStatusTracking projectStatusTracking = projectStatusTrackingService.updateProjectStatus(projectTrackingRequestDto, file);
        return ResponseEntity.ok(new ProjectTrackingResponseDto(projectStatusTracking));
    }

    @Operation(summary = "Deletes a project status tracking")
    @DeleteMapping("/{projectStatusId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = EmpowerConstants.SUCCESS_CODE, description = EmpowerConstants.SUCCESS_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.BAD_REQUEST_CODE, description = EmpowerConstants.BAD_REQUEST_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNAUTHORIZED_CODE, description = EmpowerConstants.UNAUTHORIZED_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.FORBIDDEN_CODE, description = EmpowerConstants.FORBIDDEN_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.RESOURCE_NOT_FOUND_CODE, description = EmpowerConstants.RESOURCE_NOT_FOUND_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE, description = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE_DESC)
    })
    public void deleteProjectStatusTracking(@PathVariable("projectStatusId") Long projectStatusId) {
        projectStatusTrackingService.deleteProjectStatus(projectStatusId);
    }

}

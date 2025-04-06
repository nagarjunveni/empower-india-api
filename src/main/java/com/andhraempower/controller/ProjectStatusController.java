package com.andhraempower.controller;

import com.andhraempower.dto.ProjectTrackingResponseDto;
import com.andhraempower.entity.ProjectStatusTracking;
import com.andhraempower.service.ProjectStatusTrackingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

}

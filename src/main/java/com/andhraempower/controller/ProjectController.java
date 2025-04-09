package com.andhraempower.controller;

import com.andhraempower.constants.EmpowerConstants;
import com.andhraempower.dto.*;
import com.andhraempower.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/project")
@RequiredArgsConstructor
@Slf4j
public class ProjectController {

    @Autowired
    private  ProjectService projectService;

    /**
     * GET API - Fetch all projects
     */
    @GetMapping(produces = {EmpowerConstants.APPLICATION_JSON, EmpowerConstants.TEXT_PLAIN})
    @Operation(summary = "Retrieves a list of all projects.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = EmpowerConstants.SUCCESS_CODE, description = EmpowerConstants.SUCCESS_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.BAD_REQUEST_CODE, description = EmpowerConstants.BAD_REQUEST_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNAUTHORIZED_CODE, description = EmpowerConstants.UNAUTHORIZED_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.FORBIDDEN_CODE, description = EmpowerConstants.FORBIDDEN_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.RESOURCE_NOT_FOUND_CODE, description = EmpowerConstants.RESOURCE_NOT_FOUND_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE, description = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE_DESC)
    })
    public ResponseEntity<Page<ProjectResponseDto>> getProjects(@RequestParam(name = "districtId", required = false) Long districtCode
            , @RequestParam(name = "mandalId", required = false) Long mandalCode, @RequestParam(name = "villageId", required = false) Long villageCode
            , @RequestParam(name="typeId", required = false) Long projectTypeId, @RequestParam(name="status", required = false) String status,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.unsorted());
            Page<ProjectResponseDto> projects;
            if(Objects.nonNull(status) || Objects.nonNull(projectTypeId) || Objects.nonNull(districtCode) || Objects.nonNull(mandalCode) || Objects.nonNull(villageCode)) {
                projects = projectService.searchProjects(districtCode, mandalCode, villageCode,projectTypeId,status,pageable);
            } else if(Objects.nonNull(projectTypeId)) {
                projects = projectService.getProjectsByProjectType(projectTypeId,pageable);
            } else if (Objects.nonNull(status)) {
                projects = projectService.getProjectsByProjectStatus(status, pageable);
            } else {
                projects = projectService.getProjects(pageable);
            }
            log.debug("Projects Page: {}", projects);
            return ResponseEntity.ok(projects);
        }catch (Exception e){
            log.error("Error while fetching projects", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(value = "/count",produces = {EmpowerConstants.APPLICATION_JSON, EmpowerConstants.TEXT_PLAIN})
    @Operation(summary = "Retrieves count of all projects.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = EmpowerConstants.SUCCESS_CODE, description = EmpowerConstants.SUCCESS_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.BAD_REQUEST_CODE, description = EmpowerConstants.BAD_REQUEST_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNAUTHORIZED_CODE, description = EmpowerConstants.UNAUTHORIZED_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.FORBIDDEN_CODE, description = EmpowerConstants.FORBIDDEN_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.RESOURCE_NOT_FOUND_CODE, description = EmpowerConstants.RESOURCE_NOT_FOUND_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE, description = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE_DESC)
    })
    public ResponseEntity<ProjectsCountDto> getProjectsCount() {
        try {
            ProjectsCountDto dto = projectService.getProjectsCount();
            return ResponseEntity.ok(dto);
        }catch (Exception e){
            log.error("Exception while fetching the projects", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    @PostMapping(consumes = EmpowerConstants.APPLICATION_JSON, produces = EmpowerConstants.TEXT_PLAIN)
    @Operation(summary = "Saves a new project to the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = EmpowerConstants.SUCCESS_CODE, description = "Project saved successfully"),
            @ApiResponse(responseCode = EmpowerConstants.BAD_REQUEST_CODE, description = "Invalid request data"),
            @ApiResponse(responseCode = EmpowerConstants.UNAUTHORIZED_CODE, description = EmpowerConstants.UNAUTHORIZED_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.FORBIDDEN_CODE, description = EmpowerConstants.FORBIDDEN_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.RESOURCE_NOT_FOUND_CODE, description = EmpowerConstants.RESOURCE_NOT_FOUND_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE, description = "Server error while saving project")
    })
    public ResponseEntity<String> saveProject(@RequestBody ProjectRequestDto projectRequestDto) {
        try {
            log.debug("Request for saving project : {}", projectRequestDto);
            projectService.saveProject(projectRequestDto, null);
            return ResponseEntity.ok("New project saved successfully!");
        }catch (IllegalArgumentException e){
            log.error("IllegalArgumentException while saving the project", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            log.error("Exception while saving the project", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("project-image")
    @Operation(summary = "Save a new project and project image to the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = EmpowerConstants.SUCCESS_CODE, description = "Project saved successfully"),
            @ApiResponse(responseCode = EmpowerConstants.BAD_REQUEST_CODE, description = "Invalid request data"),
            @ApiResponse(responseCode = EmpowerConstants.UNAUTHORIZED_CODE, description = EmpowerConstants.UNAUTHORIZED_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.FORBIDDEN_CODE, description = EmpowerConstants.FORBIDDEN_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.RESOURCE_NOT_FOUND_CODE, description = EmpowerConstants.RESOURCE_NOT_FOUND_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE, description = "Server error while saving project")
    })
    public ResponseEntity<String> saveProjectWithImage(@RequestPart("project") ProjectRequestDto projectRequestDto, @RequestPart(value = "projectImage", required = false) MultipartFile multipartFile) {
        try {
            log.debug("Request for saving project : {}", projectRequestDto);
            projectService.saveProject(projectRequestDto, multipartFile);
            return ResponseEntity.ok("New project saved successfully!");
        }catch (IllegalArgumentException e){
            log.error("IllegalArgumentException while saving the project", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            log.error("Exception while saving the project", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping(consumes = EmpowerConstants.APPLICATION_JSON, produces = EmpowerConstants.TEXT_PLAIN)
    @Operation(summary = "updates existing project to the database.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = EmpowerConstants.SUCCESS_CODE, description = "Project Updated successfully"),
            @ApiResponse(responseCode = EmpowerConstants.BAD_REQUEST_CODE, description = "Invalid request data"),
            @ApiResponse(responseCode = EmpowerConstants.UNAUTHORIZED_CODE, description = EmpowerConstants.UNAUTHORIZED_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.FORBIDDEN_CODE, description = EmpowerConstants.FORBIDDEN_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.RESOURCE_NOT_FOUND_CODE, description = EmpowerConstants.RESOURCE_NOT_FOUND_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE, description = "Server error while saving project")
    })
    public ResponseEntity<String> updateProject(@RequestPart("project") ProjectRequestDto projectRequestDto, @RequestPart(value = "projectImage", required = false) MultipartFile multipartFile) {
        try {
            log.debug("Request for updating project : {}", projectRequestDto);
            projectService.updateProject(projectRequestDto, multipartFile);
            return ResponseEntity.ok("Project updates successfully!");
        }catch (IllegalArgumentException e){
            log.error("IllegalArgumentException while updating the project", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            log.error("Exception while updating the project", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/save-project-steps/{projectId}")
    @Operation(summary = "Save the project completed steps like bank details added/committee formed/estimation added.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = EmpowerConstants.SUCCESS_CODE, description = "Project Updated successfully"),
            @ApiResponse(responseCode = EmpowerConstants.BAD_REQUEST_CODE, description = "Invalid request data"),
            @ApiResponse(responseCode = EmpowerConstants.UNAUTHORIZED_CODE, description = EmpowerConstants.UNAUTHORIZED_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.FORBIDDEN_CODE, description = EmpowerConstants.FORBIDDEN_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.RESOURCE_NOT_FOUND_CODE, description = EmpowerConstants.RESOURCE_NOT_FOUND_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE, description = "Server error while saving project")
    })
    public ResponseEntity<String> saveProjectCompletionSteps(@PathVariable("projectId") Long projectId
            , @RequestBody ProjectStatusSteps projectStatusSteps) {
        try {
            projectService.saveProjectStatusSteps(projectStatusSteps, projectId);
            return ResponseEntity.ok("Successfully saved");
        }catch (IllegalArgumentException e) {
            log.error("Error while saving project status steps ",e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/publish/{projectId}")
    @Operation(summary = "Publish project to Waiting For Donors. all the steps should be completed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = EmpowerConstants.SUCCESS_CODE, description = "Project Updated successfully"),
            @ApiResponse(responseCode = EmpowerConstants.BAD_REQUEST_CODE, description = "Invalid request data"),
            @ApiResponse(responseCode = EmpowerConstants.UNAUTHORIZED_CODE, description = EmpowerConstants.UNAUTHORIZED_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.FORBIDDEN_CODE, description = EmpowerConstants.FORBIDDEN_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.RESOURCE_NOT_FOUND_CODE, description = EmpowerConstants.RESOURCE_NOT_FOUND_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE, description = "Server error while saving project")
    })
    public ResponseEntity<String> publishProject(@PathVariable("projectId") Long projectId
            , @RequestBody ProjectStatusSteps projectStatusSteps) {
        try {
            projectService.publishProject(projectStatusSteps, projectId);
            return ResponseEntity.ok("Successfully published");
        }catch (IllegalArgumentException e) {
            log.error("Error while publishing project status steps ",e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/kick-off/{projectId}")
    @Operation(summary = "Kick off to Work in progress. all the steps should be completed")
    @ApiResponses(value = {
            @ApiResponse(responseCode = EmpowerConstants.SUCCESS_CODE, description = "Project Updated successfully"),
            @ApiResponse(responseCode = EmpowerConstants.BAD_REQUEST_CODE, description = "Invalid request data"),
            @ApiResponse(responseCode = EmpowerConstants.UNAUTHORIZED_CODE, description = EmpowerConstants.UNAUTHORIZED_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.FORBIDDEN_CODE, description = EmpowerConstants.FORBIDDEN_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.RESOURCE_NOT_FOUND_CODE, description = EmpowerConstants.RESOURCE_NOT_FOUND_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE, description = "Server error while saving project")
    })
    public ResponseEntity<String> kickOffProject(@PathVariable("projectId") Long projectId) {
        try {
            projectService.kickOffProject(projectId);
            return ResponseEntity.ok("Successfully published");
        } catch (IllegalArgumentException e) {
            log.error("Error while publishing project status steps ", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * GET API - Fetch single projects
     */
    @GetMapping(value = "/getProjectById", produces = {EmpowerConstants.APPLICATION_JSON, EmpowerConstants.TEXT_PLAIN})
    @Operation(summary = "Retrieves a project based on Id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = EmpowerConstants.SUCCESS_CODE, description = EmpowerConstants.SUCCESS_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.BAD_REQUEST_CODE, description = EmpowerConstants.BAD_REQUEST_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNAUTHORIZED_CODE, description = EmpowerConstants.UNAUTHORIZED_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.FORBIDDEN_CODE, description = EmpowerConstants.FORBIDDEN_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.RESOURCE_NOT_FOUND_CODE, description = EmpowerConstants.RESOURCE_NOT_FOUND_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE, description = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE_DESC)
    })
    public ResponseEntity<Object> getProjectById(@RequestParam ("projectId") String projectId) {
        try {
            if (projectId != null & projectId != "") {
                Integer.parseInt(projectId);
                ProjectResponseDto project = projectService.getProjectById(projectId);
                log.debug("Projects Page: {}", project);
                if(project == null){
                    return ResponseEntity.ok("Project not found for the project id "+projectId);
                }else{
                    return ResponseEntity.ok(project);
                }

            } else {
                return ResponseEntity.badRequest().body("Project Id empty or blank");
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid number: " + projectId);
        } catch (Exception e){
            log.error("Error while fetching project {}", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * GET API - Fetch all projects
     */
    @GetMapping(value ="/districts-mandal-villages",produces = {EmpowerConstants.APPLICATION_JSON, EmpowerConstants.TEXT_PLAIN})
    @Operation(summary = "Retrieves all districts, Mandals, Villages with project status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = EmpowerConstants.SUCCESS_CODE, description = EmpowerConstants.SUCCESS_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.BAD_REQUEST_CODE, description = EmpowerConstants.BAD_REQUEST_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNAUTHORIZED_CODE, description = EmpowerConstants.UNAUTHORIZED_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.FORBIDDEN_CODE, description = EmpowerConstants.FORBIDDEN_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.RESOURCE_NOT_FOUND_CODE, description = EmpowerConstants.RESOURCE_NOT_FOUND_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE, description = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE_DESC)
    })
    public ResponseEntity<Page<DistrictMandalVillageProjectInfoDto>> getDistrictMandalVillageProjects(@RequestParam(name = "districtId", required = false) Long districtId
            , @RequestParam(name = "mandalId", required = false) Long mandalId
            , @RequestParam(name = "typeId", required = false) Long typeId,
                                                                                                      @RequestParam(name="status", required = false) String status,
                                                                                                      @RequestParam(name = "page", defaultValue = "0") int page,
                                                                                                      @RequestParam(name = "size", defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.unsorted());
            Page<DistrictMandalVillageProjectInfoDto> projects;

                projects = projectService.getDistrictMandalVillageProjects(districtId,mandalId,typeId,status,pageable);

            log.debug("Projects Page: {}", projects);
            return ResponseEntity.ok(projects);
        }catch (Exception e){
            log.error("Error while fetching projects", e);
            return ResponseEntity.internalServerError().build();
        }
    }

}

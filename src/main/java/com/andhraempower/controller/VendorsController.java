package com.andhraempower.controller;

import com.andhraempower.constants.EmpowerConstants;
import com.andhraempower.entity.Vendors;
import com.andhraempower.service.VendorsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.xml.ws.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/vendors")
@RequiredArgsConstructor
@Slf4j
public class VendorsController {

    @Autowired
    private VendorsService VendorsService;

    @GetMapping(value ="getVendorByProjectId",produces = {EmpowerConstants.APPLICATION_JSON, EmpowerConstants.TEXT_PLAIN})
    @Operation(summary = "Fetches Vendors.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = EmpowerConstants.SUCCESS_CODE, description = EmpowerConstants.SUCCESS_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.BAD_REQUEST_CODE, description = EmpowerConstants.BAD_REQUEST_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNAUTHORIZED_CODE, description = EmpowerConstants.UNAUTHORIZED_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.FORBIDDEN_CODE, description = EmpowerConstants.FORBIDDEN_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.RESOURCE_NOT_FOUND_CODE, description = EmpowerConstants.RESOURCE_NOT_FOUND_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE, description = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE_DESC)
    })
    public List<Vendors> getVendors(@RequestParam(value = "projectId") Long projectId){
        return VendorsService.getVendors(projectId);
    }


    @PostMapping(produces = {EmpowerConstants.APPLICATION_JSON, EmpowerConstants.TEXT_PLAIN})
    @Operation(summary = "Adds new Vendors .")
    @ApiResponses(value = {
            @ApiResponse(responseCode = EmpowerConstants.SUCCESS_CODE, description = EmpowerConstants.SUCCESS_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.BAD_REQUEST_CODE, description = EmpowerConstants.BAD_REQUEST_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNAUTHORIZED_CODE, description = EmpowerConstants.UNAUTHORIZED_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.FORBIDDEN_CODE, description = EmpowerConstants.FORBIDDEN_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.RESOURCE_NOT_FOUND_CODE, description = EmpowerConstants.RESOURCE_NOT_FOUND_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE, description = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE_DESC)
    })
    public ResponseEntity<String> addVendors(@RequestBody Vendors vendors) {

        try {
            log.debug("Request for saving vendor : {}", vendors);
            return VendorsService.addVendors(vendors);
        }catch (IllegalArgumentException e){
            log.error("IllegalArgumentException while saving the vendor", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e){
            log.error("Exception while saving the vendor", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Deletes a vendor from project")
    @DeleteMapping("/{vendorId}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = EmpowerConstants.SUCCESS_CODE, description = EmpowerConstants.SUCCESS_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.BAD_REQUEST_CODE, description = EmpowerConstants.BAD_REQUEST_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNAUTHORIZED_CODE, description = EmpowerConstants.UNAUTHORIZED_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.FORBIDDEN_CODE, description = EmpowerConstants.FORBIDDEN_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.RESOURCE_NOT_FOUND_CODE, description = EmpowerConstants.RESOURCE_NOT_FOUND_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE, description = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE_DESC)
    })
    public ResponseEntity<String> deleteProjectStatusTracking(@PathVariable("vendorId") Long vendorId) {


        try {
            log.debug("Deleting from vendor : {}", vendorId);
            boolean status = VendorsService.deleteVendor(vendorId);
            if(status){
                return ResponseEntity.ok("Vendor deleted successfully!");
            }else{
                return ResponseEntity.ok("Vendor details not available!");
            }
        } catch (Exception e){
            log.error("Exception while deleting the vendor", e);
            return ResponseEntity.internalServerError().build();
        }

    }
}

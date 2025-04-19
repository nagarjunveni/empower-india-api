package com.andhraempower.controller;

import com.andhraempower.constants.EmpowerConstants;
import com.andhraempower.dto.UserRequestDto;
import com.andhraempower.dto.UserResponseDto;
import com.andhraempower.entity.User;
import com.andhraempower.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Create a new user or update with profile photo")
    @PostMapping()
    @ApiResponses(value = {
            @ApiResponse(responseCode = EmpowerConstants.SUCCESS_CODE, description = EmpowerConstants.SUCCESS_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.BAD_REQUEST_CODE, description = EmpowerConstants.BAD_REQUEST_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNAUTHORIZED_CODE, description = EmpowerConstants.UNAUTHORIZED_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.FORBIDDEN_CODE, description = EmpowerConstants.FORBIDDEN_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.RESOURCE_NOT_FOUND_CODE, description = EmpowerConstants.RESOURCE_NOT_FOUND_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE, description = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE_DESC)
    })
    public ResponseEntity<UserResponseDto> createUser(
            @RequestPart("user") UserRequestDto userRequestDto,
            @RequestPart(value = "profilePhoto", required = false) MultipartFile file) throws IOException {

        UserResponseDto user;
        if (userRequestDto.getId() != null) {
            user = userService.updateUser(userRequestDto, file);
        } else {
            user = userService.createUser(userRequestDto, file);
        }
        return ResponseEntity.ok(user);
    }


    @Operation(summary = "Get all available users.")
    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Resource not found"),
            @ApiResponse(responseCode = "500", description = "Unexpected server error")
    })
    public ResponseEntity<List<UserResponseDto>> getUsers(@RequestParam(required = false) String searchTerm, @RequestParam(required = false) Long districtId, @RequestParam(required = false) Long roleId) {

        List<UserResponseDto> userResponseDtos = userService.getAllUsers(searchTerm, districtId, roleId);
        return ResponseEntity.ok(userResponseDtos);
    }

    @Operation(summary = "Deactivate a user (soft delete).")
    @PutMapping("/{userId}/status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = EmpowerConstants.SUCCESS_CODE, description = EmpowerConstants.SUCCESS_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.BAD_REQUEST_CODE, description = EmpowerConstants.BAD_REQUEST_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNAUTHORIZED_CODE, description = EmpowerConstants.UNAUTHORIZED_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.FORBIDDEN_CODE, description = EmpowerConstants.FORBIDDEN_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.RESOURCE_NOT_FOUND_CODE, description = EmpowerConstants.RESOURCE_NOT_FOUND_CODE_DESC),
            @ApiResponse(responseCode = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE, description = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE_DESC)
    })

    public ResponseEntity<String> updateUserStatus(@PathVariable Long userId, @RequestParam Integer isActive) {
        if (isActive != 0 && isActive != 1) {
            return ResponseEntity.badRequest().body("Invalid status value. Use 0 for inactive, 1 for active.");
        }
        userService.deactivateUser(userId, isActive);
        return ResponseEntity.ok("User status updated successfully.");
    }

    @Operation(summary = "Request password reset via email or phone")
    @PostMapping("/forgot-password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = EmpowerConstants.SUCCESS_CODE, description = "Reset link sent successfully"),
            @ApiResponse(responseCode = EmpowerConstants.BAD_REQUEST_CODE, description = "Invalid request"),
            @ApiResponse(responseCode = EmpowerConstants.UNAUTHORIZED_CODE, description = "Unauthorized access"),
            @ApiResponse(responseCode = EmpowerConstants.FORBIDDEN_CODE, description = "Forbidden request"),
            @ApiResponse(responseCode = EmpowerConstants.RESOURCE_NOT_FOUND_CODE, description = "User not found"),
            @ApiResponse(responseCode = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE, description = "Unexpected server error")
    })
    public ResponseEntity<String> forgotPassword(@RequestParam String userNameOrEmailOrPhone) {
        userService.findByEmailOrPhone(userNameOrEmailOrPhone);
        return ResponseEntity.ok("Your password has been reset and sent to your email.");
    }


    @Operation(summary = "Reset password")
    @PostMapping("/reset-password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = EmpowerConstants.SUCCESS_CODE, description = "Reset link sent successfully"),
            @ApiResponse(responseCode = EmpowerConstants.BAD_REQUEST_CODE, description = "Invalid request"),
            @ApiResponse(responseCode = EmpowerConstants.UNAUTHORIZED_CODE, description = "Unauthorized access"),
            @ApiResponse(responseCode = EmpowerConstants.FORBIDDEN_CODE, description = "Forbidden request"),
            @ApiResponse(responseCode = EmpowerConstants.RESOURCE_NOT_FOUND_CODE, description = "User not found"),
            @ApiResponse(responseCode = EmpowerConstants.UNEXPECTED_SERVER_ERROR_CODE, description = "Unexpected server error")
    })
    public ResponseEntity<UserResponseDto> resetPassword(@RequestParam String userNameOrEmailOrPhone) {
        return ResponseEntity.ok(userService.resetPassword(userNameOrEmailOrPhone));
    }

}

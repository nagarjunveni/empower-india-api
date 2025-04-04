package com.andhraempower.service;

import com.andhraempower.dto.LoginRequestDto;
import com.andhraempower.dto.UserRequestDto;
import com.andhraempower.dto.UserResponseDto;
import com.andhraempower.entity.Role;
import com.andhraempower.entity.User;
import com.andhraempower.exception.InvalidCredentialsException;
import com.andhraempower.exception.UserAlreadyExistsException;
import com.andhraempower.exception.UserNotFoundException;
import com.andhraempower.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    public UserResponseDto loadUserWithRoles(LoginRequestDto loginRequestDto) {
        Optional<User> userOptional = userRepository.findByUserName(loginRequestDto.getUserName());

        if (!userOptional.isPresent()) {
            throw new UserNotFoundException("User does not exist with username: " + loginRequestDto.getUserName());
        }

        User user = userOptional.get();

        if (!loginRequestDto.getPassword().equals(user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials for user: " + loginRequestDto.getUserName());
        }
        return new UserResponseDto(user);
    }

    public User createUser(UserRequestDto userRequestDto, MultipartFile file) throws IOException {

        if (userRepository.existsByUserName(userRequestDto.getUserName())) {
            throw new UserAlreadyExistsException("Username already exists. Please choose another one.");
        }

        if (userRepository.existsByEmail(userRequestDto.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists. Please choose another one.");
        }

        if (userRepository.existsByPhoneNumber(userRequestDto.getPhoneNumber())) {
            throw new UserAlreadyExistsException("PhoneNumber already exists. Please choose another one.");
        }


        User user = new User();
        user.setFirstName(userRequestDto.getFirstName());
        user.setLastName(userRequestDto.getLastName());
        user.setPhoneNumber(userRequestDto.getPhoneNumber());
        user.setEmail(userRequestDto.getEmail());
        user.setUserName(userRequestDto.getUserName());
        user.setPassword(userRequestDto.getPassword());
        user.setAboutYourSelf(userRequestDto.getAboutYourSelf());
        if (!userRequestDto.getRoles().isEmpty()) {
            List<Role> roles = userRequestDto.getRoles().stream()
                    .map(role -> new Role(role.getId(), role.getName()))
                    .collect(Collectors.toList());
            user.setRoles(roles);
        }
        if (file != null && !file.isEmpty()) {
            user.setProfilePhoto(file.getBytes());
        }
        user.setDistrictId(userRequestDto.getDistrictId());
        user.setIsEnabled(1);  //for active user
        return userRepository.save(user);
    }


    public User updateUser(UserRequestDto userRequestDto, MultipartFile file) throws IOException {

        Optional<User> optionalUser = userRepository.findById(userRequestDto.getId());

        if (!optionalUser.isPresent()) {
            throw new EntityNotFoundException("User not found with id: " + userRequestDto.getId());
        }

        User user = optionalUser.get();

        if (userRequestDto.getFirstName() != null) {
            user.setFirstName(userRequestDto.getFirstName());
        }
        if (userRequestDto.getLastName() != null) {
            user.setLastName(userRequestDto.getLastName());
        }
        if (userRequestDto.getPhoneNumber() != null) {
            user.setPhoneNumber(userRequestDto.getPhoneNumber());
        }
        if (userRequestDto.getEmail() != null) {
            user.setEmail(userRequestDto.getEmail());
        }
        if (userRequestDto.getUserName() != null) {
            user.setUserName(userRequestDto.getUserName());
        }
        if (userRequestDto.getPassword() != null) {
            user.setPassword(userRequestDto.getPassword());
        }
        if (userRequestDto.getAboutYourSelf() != null) {
            user.setAboutYourSelf(userRequestDto.getAboutYourSelf());
        }

        if (userRequestDto.getDistrictId() != null) {
            user.setDistrictId(userRequestDto.getDistrictId());
        }
        if (userRequestDto.getRoles() != null && !userRequestDto.getRoles().isEmpty()) {
            Map<Long, Role> existingRolesMap = user.getRoles().stream()
                    .collect(Collectors.toMap(Role::getId, role -> role));

            List<Role> updatedRoles = userRequestDto.getRoles().stream()
                    .map(roleDto -> {
                        if (existingRolesMap.containsKey(roleDto.getId())) {
                            Role existingRole = existingRolesMap.get(roleDto.getId());
                            existingRole.setName(roleDto.getName());
                            return existingRole;
                        } else {
                            return new Role(roleDto.getId(), roleDto.getName());
                        }
                    })
                    .collect(Collectors.toList());
            user.setRoles(updatedRoles);
        }
        if (file != null && !file.isEmpty()) {
            user.setProfilePhoto(file.getBytes());
        }

        return userRepository.save(user);
    }

    public List<UserResponseDto> getAllUsers(String searchTerm, Long districtId, Long roleId) {
        return userRepository.findUsers(searchTerm, districtId, roleId).stream().map(UserResponseDto::new).collect(Collectors.toList());
    }

    public void deactivateUser(Long userId, Integer isActive) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User does not exist with ID: " + userId));

        user.setIsEnabled(isActive);
        userRepository.save(user);
    }

    public void findByEmailOrPhone(String emailOrPhone) {
        User user = userRepository.findByEmailOrPhoneNumber(emailOrPhone, emailOrPhone).orElseThrow(() -> new UserNotFoundException("User not found"));
        String token = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
        user.setPassword(token);
        emailService.sendEmail(user);
    }

    public UserResponseDto resetPassword(String emailOrPhone) {
        User user = userRepository.findByEmailOrPhoneNumber(emailOrPhone, emailOrPhone).orElseThrow(() -> new UserNotFoundException("User not found"));
        return new UserResponseDto(user);
    }
}

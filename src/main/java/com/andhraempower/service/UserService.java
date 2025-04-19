package com.andhraempower.service;

import com.andhraempower.config.CustomUserDetails;
import com.andhraempower.config.SecurityCustomUserDetailsService;
import com.andhraempower.dto.UserRequestDto;
import com.andhraempower.dto.UserResponseDto;
import com.andhraempower.entity.Role;
import com.andhraempower.entity.User;
import com.andhraempower.exception.InvalidCredentialsException;
import com.andhraempower.exception.UserAlreadyExistsException;
import com.andhraempower.exception.UserNotFoundException;
import com.andhraempower.repository.RolesRepository;
import com.andhraempower.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SecurityCustomUserDetailsService securityCustomUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenGenerationService tokenGenService;

    @Autowired
    private RolesRepository rolesRepository;

    public UserResponseDto loadUserWithRoles(String userName, String password) {

        Authentication authentication = getAuthentication(userName, password);

        if (!authentication.isAuthenticated()) {
            throw new UsernameNotFoundException("Invalid Credentials");
        }

        CustomUserDetails userDetails = (CustomUserDetails) securityCustomUserDetailsService.loadUserByUsername(userName);

        String jwtToken = tokenGenService.generateToken(userDetails);

        UserResponseDto userResponseDto = new UserResponseDto(userRepository.findByUserName(userName).get());
        userResponseDto.setJwtToken(jwtToken);
        return userResponseDto;
    }

    public UserResponseDto createUser(UserRequestDto dto, MultipartFile file) throws IOException {
        validateUniqueFields(dto.getUserName(), dto.getEmail(), dto.getPhoneNumber());

        User user = new User();
        populateUserFields(user, dto, file);
        user.setIsEnabled(1);

        User persistedUser = userRepository.save(user);
        UserResponseDto userResponseDto = new UserResponseDto(persistedUser);
        userResponseDto.setJwtToken(generateToken(securityCustomUserDetailsService.loadUserByUsername(persistedUser.getUserName())));
        return userResponseDto;
    }

    private String generateToken(UserDetails userDetails) {
        return tokenGenService.generateToken(userDetails);
    }



    public UserResponseDto updateUser(UserRequestDto dto, MultipartFile file) throws IOException {
        User user = userRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + dto.getId()));

        populateUserFields(user, dto, file);
        User persistedUserObj = userRepository.save(user);
        UserResponseDto userResponseDto = new UserResponseDto(persistedUserObj);
        userResponseDto.setJwtToken(generateToken(securityCustomUserDetailsService.loadUserByUsername(persistedUserObj.getUserName())));
        return userResponseDto;
    }

    private void populateUserFields(User user, UserRequestDto dto, MultipartFile file) throws IOException {
        if (dto.getFirstName() != null) user.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) user.setLastName(dto.getLastName());
        if (dto.getPhoneNumber() != null) user.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getEmail() != null) user.setEmail(dto.getEmail());
        if (dto.getUserName() != null) user.setUserName(dto.getUserName());
        if (dto.getPassword() != null) user.setPassword(passwordEncoder.encode(dto.getPassword()));
        if (dto.getAboutYourSelf() != null) user.setAboutYourSelf(dto.getAboutYourSelf());
        if (dto.getDistrictId() != null) user.setDistrictId(dto.getDistrictId());

        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            List<Role> rolesFromDb = dto.getRoles().stream()
                    .map(roleDto -> rolesRepository.findById(roleDto.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + roleDto.getId())))
                    .collect(Collectors.toList());

            user.setRoles(rolesFromDb);
        }

        if (file != null && !file.isEmpty()) {
            user.setProfilePhoto(file.getBytes());
        }
    }

    private void validateUniqueFields(String userName, String email, String phone) {
        if (userRepository.existsByUserName(userName)) {
            throw new UserAlreadyExistsException("Username already exists. Please choose another one.");
        }
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("Email already exists. Please choose another one.");
        }
        if (userRepository.existsByPhoneNumber(phone)) {
            throw new UserAlreadyExistsException("PhoneNumber already exists. Please choose another one.");
        }
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

    private Authentication getAuthentication(String userName, String password) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException(e.getMessage());
        }
        return authentication;
    }

}

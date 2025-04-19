package com.andhraempower.config;

import com.andhraempower.entity.User;
import com.andhraempower.exception.UserInActiveException;
import com.andhraempower.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SecurityCustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if(Objects.nonNull(user) && user.getIsEnabled().equals(0)) {
            throw  new UserInActiveException("User "+username+" has been inactive, Please contact us");
        }


        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> (GrantedAuthority) role::getName)
                .collect(Collectors.toList());

        return new CustomUserDetails(
                user.getId(),
                user.getUserName(),
                user.getPassword(),
                authorities
        );
    }
}

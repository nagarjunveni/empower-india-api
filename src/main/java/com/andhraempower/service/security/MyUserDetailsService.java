package com.andhraempower.service.security;

import com.andhraempower.entity.Role;
import com.andhraempower.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.andhraempower.entity.User> user = userRepository.findByUserName(username);
        if (user.isPresent()) {
            var userObj = user.get();
            return User.builder()
                    .username(userObj.getUserName())
                    .password(userObj.getPassword())
                    .roles(getRoles(user.get()))
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
         }
    }

    private String[] getRoles(com.andhraempower.entity.User userObj) {
        if (userObj == null) {
            return new String[]{"USER"};
        }
        return userObj.getRoles()
                .stream()
                .map(Role::getName)
                .toList()
                .toArray(new String[0]);
    }
}

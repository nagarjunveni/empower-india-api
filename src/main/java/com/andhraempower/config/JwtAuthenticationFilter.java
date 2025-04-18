package com.andhraempower.config;

import com.andhraempower.constants.EmpowerConstants;
import com.andhraempower.exception.MissingBearerTokenException;
import com.andhraempower.exception.TokenExpiredException;
import com.andhraempower.service.TokenGenerationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Configuration
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenGenerationService tokenGenService;

    @Autowired
    private SecurityCustomUserDetailsService securityCustomUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String method = request.getMethod();
        String path = request.getRequestURI();
        String authHeader = request.getHeader("Authorization");

        if (method.equals("GET") || isPublicEndpoint(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (Objects.equals("POST,PUT,DELETE", method) || authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new MissingBearerTokenException("Bearer token is required");
        }
        String jwt = authHeader.substring(7);
        String username = tokenGenService.extractUsername(jwt);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            CustomUserDetails userDetails = (CustomUserDetails) securityCustomUserDetailsService.loadUserByUsername(username);
            if (userDetails != null && tokenGenService.isTokenValid(jwt)) {
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token);
            } else {
                throw new TokenExpiredException("Token was expired. Please login again.");
            }
        }
        filterChain.doFilter(request, response);

    }

    private boolean isPublicEndpoint(String path) {
        return EmpowerConstants.ENDPOINTS_FOR_ALL_USERS.stream().anyMatch(path::endsWith);
    }
}

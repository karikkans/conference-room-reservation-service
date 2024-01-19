package com.karikkans.reservation.service;

import com.karikkans.reservation.model.AuthenticationRequest;
import com.karikkans.reservation.model.AuthenticationResponse;
import com.karikkans.reservation.util.TokenUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authManager;
    private final TokenUtils tokenUtils;
    private final UserDetailsService userDetailsService;

    public void authenticate(AuthenticationRequest request) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        log.info("Successfully authenticated {}", request.getUsername());
    }

    public AuthenticationResponse createAuthToken(AuthenticationRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = tokenUtils.generateToken(userDetails);
        log.debug("Auth token generate for the user");
        return new AuthenticationResponse(token);
    }
}

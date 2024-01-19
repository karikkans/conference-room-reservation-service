package com.karikkans.reservation.service;

import com.karikkans.reservation.model.AuthenticationRequest;
import com.karikkans.reservation.model.AuthenticationResponse;

public interface AuthService {

    void authenticate(AuthenticationRequest request);

    AuthenticationResponse createAuthToken(AuthenticationRequest request);
}

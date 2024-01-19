package com.karikkans.reservation.controller;

import com.karikkans.reservation.model.AuthenticationRequest;
import com.karikkans.reservation.model.AuthenticationResponse;
import com.karikkans.reservation.model.RegistrationRequest;
import com.karikkans.reservation.model.Response;
import com.karikkans.reservation.service.AuthService;
import com.karikkans.reservation.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.karikkans.reservation.util.ReservationUtil.buildErrorResponse;
import static com.karikkans.reservation.util.ReservationUtil.buildSuccessResponse;
import static java.util.Collections.emptyList;

@Slf4j
@RestController
@RequestMapping("/v1/auth/")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    ResponseEntity<Response<AuthenticationResponse>> authenticateAndCreateToken(@RequestBody AuthenticationRequest request) {

        try {
            authService.authenticate(request);
        } catch (BadCredentialsException e) {
            log.error("Invalid credentials", e);
            return ResponseEntity.ok(buildErrorResponse(emptyList(), "Invalid credentials"));
        } catch (Exception e) {
            log.error("Error occurred while authenticating user: {} ", request.username, e);
            return ResponseEntity.ok(buildErrorResponse(emptyList(), "Error occurred while authenticating user"));
        }
        return ResponseEntity.ok(buildSuccessResponse(authService.createAuthToken(request)));

    }

    @PostMapping("/sign-up")
    public ResponseEntity<Response<AuthenticationResponse>> signUp(@RequestBody RegistrationRequest user) {

        try {
            String password = user.getPassword();
            userService.signUpUser(user);
            return authenticateAndCreateToken(new AuthenticationRequest(user.getUsername(), password));
        } catch (Exception e) {
            log.error("Error occurred while registering user details ", e);
        }
        return ResponseEntity.ok(buildErrorResponse(emptyList(), "Error occurred during registration"));
    }
}

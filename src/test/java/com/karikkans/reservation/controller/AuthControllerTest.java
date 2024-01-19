package com.karikkans.reservation.controller;

import com.karikkans.reservation.model.AuthenticationRequest;
import com.karikkans.reservation.model.AuthenticationResponse;
import com.karikkans.reservation.model.Response;
import com.karikkans.reservation.service.AuthServiceImpl;
import com.karikkans.reservation.service.UserServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class AuthControllerTest {

    @Mock
    private AuthServiceImpl authService;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    AuthController authController;

    @Test
    public void should_authenticate_user() {
        //Given
        AuthenticationRequest request = new AuthenticationRequest("name", "password");
        doNothing().when(authService).authenticate(request);
        when(authService.createAuthToken(request)).thenReturn(new AuthenticationResponse("token"));

        //When
        ResponseEntity<Response<AuthenticationResponse>> response = authController.authenticateAndCreateToken(request);

        //Then
        assertNotNull(response);
        assertThat(response).isNotNull();
        Response<AuthenticationResponse> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertNotNull(responseBody.getData());
        assertThat(responseBody.getData().getToken()).isEqualTo("token");
    }

    @Test
    public void should_handle_bad_credential_exception_when_username_or_password_incorrect() {

        //Given
        AuthenticationRequest request = new AuthenticationRequest("name", "password");
        doThrow(new BadCredentialsException("bad credentials")).when(authService).authenticate(request);

        //When
        ResponseEntity<Response<AuthenticationResponse>> response  = authController.authenticateAndCreateToken(request);

        //Then
        assertThat(response).isNotNull();
        Response<AuthenticationResponse> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertThat(responseBody.getMessage()).isEqualTo("Invalid credentials");
    }

    @Test
    public void should_handle_exception_when_user_authentication_fails() {

        //Given
        AuthenticationRequest request = new AuthenticationRequest("name", "password");
        doThrow(new RuntimeException("Error occurred while authenticating user")).when(authService).authenticate(request);

        //When
        ResponseEntity<Response<AuthenticationResponse>> response  = authController.authenticateAndCreateToken(request);

        //Then
        assertThat(response).isNotNull();
        Response<AuthenticationResponse> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertThat(responseBody.getMessage()).isEqualTo("Error occurred while authenticating user");
    }

    @Test
    public void test() {
        String string = "aaabccedccaa";
        StringBuilder builder = new StringBuilder();
        int count;
        for (int index = 0; index < string.length(); ) {
            char c = string.charAt(index);
            count = 0;
            while (c == string.charAt(index)) {
                count++;
                index++;
                if ((index) >= string.length()) {
                    break;
                }
            }
            builder.append(c);
            builder.append(count);
        }
        System.out.println("result:: " + builder.toString());
    }

    @Test
    public void testSort() {
        List<Integer> integers = new ArrayList<>();
        integers.add(4);
        integers.add(40);
        integers.add(3);
        integers.add(12);
        integers.add(120);

        System.out.println(integers.stream().sorted(Collections.reverseOrder()).collect(Collectors.toList()));
        System.out.println(integers.stream().sorted(Collections.reverseOrder()).limit(3).collect(Collectors.toList()));
        System.out.println(integers.stream().sorted(Collections.reverseOrder()).limit(4).skip(2).collect(Collectors.toList()));

        List<Integer> lkIntegers = new LinkedList<>();
        lkIntegers.add(4);
        lkIntegers.add(40);
        lkIntegers.add(3);
        lkIntegers.add(12);
        lkIntegers.add(120);

        System.out.println(lkIntegers.stream().sorted(Collections.reverseOrder()).collect(Collectors.toList()));
        System.out.println(lkIntegers.stream().sorted().collect(Collectors.toList()));
        System.out.println(lkIntegers.stream().sorted(Collections.reverseOrder()).limit(3).collect(Collectors.toList()));
        System.out.println(lkIntegers.stream().sorted(Collections.reverseOrder()).limit(4).skip(2).collect(Collectors.toList()));
    }

    @Test
    public void testSum() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
        int result = numbers
                .stream()
                .reduce(0, Integer::sum);
        System.out.println("Result " + result);
    }

}
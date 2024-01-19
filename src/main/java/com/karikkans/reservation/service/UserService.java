package com.karikkans.reservation.service;

import com.karikkans.reservation.model.RegistrationRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {
    UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException;

    void signUpUser(RegistrationRequest user);
}

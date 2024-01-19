package com.karikkans.reservation.service;

import com.karikkans.reservation.enums.UserRole;
import com.karikkans.reservation.model.RegistrationRequest;
import com.karikkans.reservation.model.User;
import com.karikkans.reservation.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        final Optional<User> optionalUser = userRepository.findByUsername(userName);

        return optionalUser.orElseThrow(() ->
                new UsernameNotFoundException(MessageFormat.format("User with userName {0} cannot be found.",
                        userName)));
    }

    public void signUpUser(RegistrationRequest user) {

        final String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());

        user.setPassword(encryptedPassword);

        Optional<User> userName = userRepository.findByUsername(user.getUsername());

        if (userName.isPresent()) {
            log.warn("User has been already registered in the application");
            throw new RuntimeException("User already exists");
        }
        userRepository.save(buildUser(user));
    }

    private User buildUser(RegistrationRequest request) {
        return User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .userRole(UserRole.USER)
                .build();
    }

}

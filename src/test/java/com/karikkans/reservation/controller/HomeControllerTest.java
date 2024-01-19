package com.karikkans.reservation.controller;

import com.karikkans.reservation.service.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;

@RunWith(SpringRunner.class)
public class HomeControllerTest {

    @Mock
    UserServiceImpl userService;

    @InjectMocks
    HomeController homeController;

    @Test
    public void should_return_home() {
        //When
        String home = homeController.home();

        //Then
        assertThat(home).isEqualTo("home");
    }
}
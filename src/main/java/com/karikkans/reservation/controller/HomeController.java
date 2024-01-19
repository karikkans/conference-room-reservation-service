package com.karikkans.reservation.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "home";
    }


}

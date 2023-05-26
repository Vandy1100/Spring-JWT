package com.istad.jwt.rsa.controller;

import ch.qos.logback.classic.Logger;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
public class HomeController {
    @GetMapping("/home")
    public String homePage(Authentication authentication){
        var user=authentication.getPrincipal();
        log.info("here is the user principle :{}"+authentication.getPrincipal());
        log.info("here is the user principle :{}"+authentication.getCredentials());
        log.info("here is the user principle :{}"+authentication.getDetails());
        log.info("here is the user principle :{}"+authentication.getAuthorities());
        return "Hello "+authentication.getName();
        //61b03ab4-752e-4d3e-bb70-530540759c7d
    }
}

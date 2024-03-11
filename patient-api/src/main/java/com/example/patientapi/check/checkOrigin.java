package com.example.patientapi.check;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class checkOrigin{

    @GetMapping("/check-origin")
    public String checkOrigins(@RequestHeader("Origin") final String origin) {
        if ("http://localhost:8081".equals(origin)) {
            return "Request originated from http://localhost:8081";
        } else {
            return "Request did not originate from http://localhost:8081";
        }
    }
}


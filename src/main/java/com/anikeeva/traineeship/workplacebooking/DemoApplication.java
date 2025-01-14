package com.anikeeva.traineeship.workplacebooking;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@ConfigurationPropertiesScan
@RestController
public class DemoApplication {

    @GetMapping("/")
    String home() {
        return "Spring is here!";
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
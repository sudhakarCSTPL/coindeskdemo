package com.example.democoindeskapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class DemocoindeskapiApplication {

    @Autowired
    Environment env;

    public static void main(String[] args) {

        SpringApplication.run(DemocoindeskapiApplication.class, args);
    }


}

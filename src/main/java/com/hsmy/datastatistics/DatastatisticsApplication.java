package com.hsmy.datastatistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class DatastatisticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DatastatisticsApplication.class, args);
    }

}

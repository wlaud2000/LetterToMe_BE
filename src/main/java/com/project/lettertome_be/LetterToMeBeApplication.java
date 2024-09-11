package com.project.lettertome_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing //JPA Auditing 기능 활성화
@EnableScheduling
public class LetterToMeBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(LetterToMeBeApplication.class, args);
    }

}

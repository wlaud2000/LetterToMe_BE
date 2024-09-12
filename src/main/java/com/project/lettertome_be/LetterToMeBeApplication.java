package com.project.lettertome_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing //JPA Auditing 기능 활성화
@EnableScheduling //스케줄링 기능 활성화
@EnableAsync //비동기 작업 활성화
@EnableRetry
public class LetterToMeBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(LetterToMeBeApplication.class, args);
    }

}

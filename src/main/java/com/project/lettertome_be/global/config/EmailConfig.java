package com.project.lettertome_be.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.properties.smtp.auth}")
    private boolean auth;

    @Value("${spring.mail.properties.smtp.starttls.enable}")
    private boolean starttlsEnable;

    @Value("${spring.mail.properties.smtp.starttls.required}")
    private boolean starttlsRequired;

    @Value("${spring.mail.properties.smtp.connection-timeout}")
    private int connectionTimeout;

    @Value("${spring.mail.properties.smtp.timeout}")
    private int timeout;

    @Value("${spring.mail.properties.smtp.write-timeout}")
    private int writeTimeout;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", starttlsEnable);
        props.put("mail.smtp.starttls.required", starttlsRequired);
        props.put("mail.smtp.connection-timeout", connectionTimeout);
        props.put("mail.smtp.timeout", timeout);
        props.put("mail.smtp.write-timeout", writeTimeout);

        return mailSender;
    }
}

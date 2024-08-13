package com.project.lettertome_be.global.config;

import com.project.lettertome_be.domain.user.jwt.filter.CustomLoginFilter;
import com.project.lettertome_be.domain.user.jwt.filter.JwtAuthorizationFilter;
import com.project.lettertome_be.domain.user.jwt.util.JwtUtil;
import com.project.lettertome_be.domain.user.repository.LocalUserRepository;
import com.project.lettertome_be.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final LocalUserRepository localUserRepository;

    //인증이 필요하지 않은 url
    private final String[] allowedUrls = {
            "/api/users/signup", //회원가입은 인증이 필요하지 않음
            "/api/users/login/local", //로그인은 인증이 필요하지 않음
            "/api/users/login/kakao",
            "/api/users/login/naver",
            "/api/auth/**", //토큰 재발급이나 이메일 인증은 AccessToken이 필요하지 않음
            "api/usage", //Swagger 관련 url
            "/swagger-ui/**",
            "v3/api-docs/**"
    };

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // CORS 정책 설정
        http
                .cors(cors -> cors
                        .configurationSource(CorsConfig.apiConfigurationSource()));

        // csrf 비활성화
        http
                .csrf(AbstractHttpConfigurer::disable);

        // form 로그인 방식 비활성화 -> REST API 로그인을 사용할 것이기 때문에
        http
                .formLogin(AbstractHttpConfigurer::disable);

        // http basic 인증 방식 비활성화
        http
                .httpBasic(AbstractHttpConfigurer::disable);

        // 세션을 사용하지 않음. (세션 생성 정책을 Stateless 설정.)
        http
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 경로별 인가
        http
                .authorizeHttpRequests(auth -> auth
                        //위에서 정의했던 allowedUrls 들은 인증이 필요하지 않음 -> permitAll
                        .requestMatchers(allowedUrls).permitAll()
                        .anyRequest().authenticated() // 그 외의 url 들은 인증이 필요함
                );

        // CustomLoginFilter 인스턴스를 생성하고 필요한 의존성을 주입
        CustomLoginFilter customLoginFilter = new CustomLoginFilter(
                authenticationManager(authenticationConfiguration), jwtUtil);
        // Login Filter URL 지정
        customLoginFilter.setFilterProcessesUrl("/api/users/login/local");

        // CustomLoginFilter를 UsernamePasswordAuthenticationFilter 전에 추가
        http
                .addFilterBefore(customLoginFilter, UsernamePasswordAuthenticationFilter.class);

        // JwtFilter를 CustomLoginFilter 앞에서 동작하도록 필터 체인에 추가
        http
                .addFilterBefore(new JwtAuthorizationFilter(jwtUtil, userRepository, localUserRepository), CustomLoginFilter.class);

        return http.build();
    }

}
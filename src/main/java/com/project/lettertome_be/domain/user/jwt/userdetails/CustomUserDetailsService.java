package com.project.lettertome_be.domain.user.jwt.userdetails;

import com.project.lettertome_be.domain.user.entity.User;
import com.project.lettertome_be.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // Username(Email)으로 CustomUserDetails 가져오는 메서드
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // users 테이블에서 사용자 정보를 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자가 존재하지 않습니다."));

        // CustomUserDetails로 변환하여 반환
        return new CustomUserDetails(user.getEmail(), user.getPassword());
    }
}

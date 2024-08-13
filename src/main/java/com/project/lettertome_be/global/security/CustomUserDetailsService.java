package com.project.lettertome_be.global.security;

import com.project.lettertome_be.domain.user.entity.User;
import com.project.lettertome_be.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    //Username(Email)으로 CustomUserDetails 가져오는 메서드
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userEntity = userRepository.findByEmail(email);
        if (userEntity.isPresent()) {
            User user = userEntity.get(); //Optional로 한번 감쌌기 때문에 .get()으로 꺼내줘야함
            return new CustomUserDetails(user.getEmail());
        }
        throw new UsernameNotFoundException("사용자가 존재하지 않습니다.");
    }
}

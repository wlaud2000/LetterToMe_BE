package com.project.lettertome_be.domain.user.service;

import com.project.lettertome_be.domain.user.dto.response.UserResponseDto;
import com.project.lettertome_be.domain.user.entity.User;
import com.project.lettertome_be.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {

    private final UserRepository userRepository;

    //회원 정보 조회
    public UserResponseDto getUserByEmail(User user) {

        //controller에서 넘어온 user는 CustomUserDetailsService의 loadUserByUsername 메서드에서 트랜잭션이 이미 종료된 상태로 넘어옴.
        //따라서 user는 영속성 컨텍스트에서 관리되고 있지 않기 때문에, user를 영속성 컨텍스트에서 다시 조회해야 함.
        User dbUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(()-> new NoSuchElementException("사용자가 존재하지 않습니다."));

        return UserResponseDto.from(dbUser);
    }

}

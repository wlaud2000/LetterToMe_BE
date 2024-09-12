package com.project.lettertome_be.domain.user.service;

import com.project.lettertome_be.domain.user.dto.request.ChangeEmailRequestDto;
import com.project.lettertome_be.domain.user.dto.request.ChangeNickNameRequestDto;
import com.project.lettertome_be.domain.user.dto.request.ChangePasswordRequestDto;
import com.project.lettertome_be.domain.user.dto.request.SignUpRequestDto;
import com.project.lettertome_be.domain.user.dto.response.SignUpResponseDto;
import com.project.lettertome_be.domain.user.entity.User;
import com.project.lettertome_be.domain.user.repository.UserRepository;
import com.project.lettertome_be.global.common.exception.CustomException;
import com.project.lettertome_be.global.common.response.UserErrorCode;
import com.project.lettertome_be.global.jwt.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; //비밀번호 암호화
    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    @Value("${cloud.aws.region}")
    private String region;

    // 회원가입
    public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {

        // 이메일 중복 확인
        if(userRepository.existsByEmail(signUpRequestDto.email())) {
            throw new CustomException(UserErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // User 엔티티 생성 및 저장
        User user = User.builder()
                .email(signUpRequestDto.email())
                .nickname(signUpRequestDto.nickName())
                .password(passwordEncoder.encode(signUpRequestDto.password()))
                .build();

        User savedUser = userRepository.save(user);

        // 저장된 User 엔티티를 DTO로 변환하여 반환
        return SignUpResponseDto.from(savedUser);
    }

    //비밀번호 변경
    public void changePassword(AuthUser authUser, ChangePasswordRequestDto changePasswordRequestDto) {
        //Controller에서 넘어온 AuthUser에서 email을 추출해서 DB에서 유저 조회를 해서 영속상태로 만듦
        User user = userRepository.findByEmail(authUser.getEmail())
                .orElseThrow(()-> new CustomException(UserErrorCode.USER_NOT_FOUND_404));

        String newPassword = changePasswordRequestDto.newPassword();
        user.changePassword(passwordEncoder.encode(newPassword));
        log.info("[User Service] 사용자의 비밀번호가 변경되었습니다.");
    }

    //유저 이메일 변경
    public void changeEmail(AuthUser authUser, ChangeEmailRequestDto changeEmailRequestDto) {
        User user = userRepository.findByEmail(authUser.getEmail())
                .orElseThrow(()-> new CustomException(UserErrorCode.USER_NOT_FOUND_404));

        String newEmail = changeEmailRequestDto.newEmail();
        user.changeEmail(newEmail);
        log.info("[User Service] 이메일이 변경되었습니다 -> {}", newEmail);
    }

    //유저 이름 변경
    public void changeNickName(AuthUser authUser, ChangeNickNameRequestDto changeNickNameRequestDto) {
        User user = userRepository.findByEmail(authUser.getEmail())
                .orElseThrow(()-> new CustomException(UserErrorCode.USER_NOT_FOUND_404));

        String newNickName = changeNickNameRequestDto.newNickName();
        user.changeNickName(newNickName);
        log.info("[User Service] 이름이 변경되었습니다 -> {}", newNickName);
    }

    //유저 삭제
    public void deleteUser(AuthUser authUser) {
        User user = userRepository.findByEmail(authUser.getEmail())
                .orElseThrow(()-> new CustomException(UserErrorCode.USER_NOT_FOUND_404));

        //user 삭제
        userRepository.delete(user);

        log.info("[User Service] 사용자가 성공적으로 삭제되었습니다.");
    }

    public void uploadProfileImage(AuthUser authUser, MultipartFile file) throws IOException {
        User user = userRepository.findByEmail(authUser.getEmail())
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND_404));

        // 고유한 파일명 생성
        String originalFilename = file.getOriginalFilename(); //기존 파일명
        String fileExtension = Objects.requireNonNull(originalFilename).substring(originalFilename.lastIndexOf(".")); //확장자 추출
        String newFileName = UUID.randomUUID() + fileExtension; //고유한 파일명 + 확장자 -> 새로운 고유한 파일명 생성

        // S3에 파일 업로드
        String s3Url = uploadFileToS3(file, newFileName);

        // 업로드된 파일의 URL을 User 엔티티에 저장
        user.changeProfileImg(s3Url);
        log.info("[User Service] 프로필 사진이 S3에 업로드되었습니다 -> {}", s3Url);
    }

    private String uploadFileToS3(MultipartFile file, String fileName) throws IOException {
        String filePath = "profile-images/" + fileName;

        // 확장자별 Content-Type 설정
        String contentType = getContentType(fileName);

        // S3에 파일 업로드 (Content-Type 포함)
        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(filePath)
                        .contentType(contentType)  // Content-Type 설정
                        .build(),
                RequestBody.fromBytes(file.getBytes()));

        // S3 URL 반환
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, filePath);
    }

    // 확장자별로 Content-Type을 설정하는 메서드
    private String getContentType(String fileName) {
        // 확장자 추출
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        // 확장자별 Content-Type 매핑
        Map<String, String> contentTypeMap = new HashMap<>();
        contentTypeMap.put("jpeg", "image/jpeg");
        contentTypeMap.put("jpg", "image/jpeg");
        contentTypeMap.put("png", "image/png");

        // 맵에서 파일 확장자에 맞는 MIME 타입을 찾아 반환, 기본 Content-Type은 binary/octet-stream
        return contentTypeMap.getOrDefault(fileExtension, "application/octet-stream");
    }

}

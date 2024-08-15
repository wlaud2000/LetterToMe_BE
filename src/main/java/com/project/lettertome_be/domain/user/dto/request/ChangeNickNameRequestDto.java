package com.project.lettertome_be.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangeNickNameRequestDto (

        @Size(min = 1, max = 12, message = "[ERROR] 닉네임은 1자 이상, 12글자 이하여야 합니다.")
        @NotBlank(message = "[ERROR] 닉네임은 필수 입력값입니다.")
        String newNickName
) {

}

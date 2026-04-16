package com.qgtest.diary.dto.userDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {
    @NotBlank
    private String keyword;   // 邮箱或手机号
    @NotBlank private String password;
}

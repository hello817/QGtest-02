package com.qgtest.diary.dto.userDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDTO {
    @NotBlank
    private String account;
    @NotBlank
    @Email private String email;//限制格式注解
    @NotBlank
    private String phone;
    @NotBlank
    @Size(min=6, max=20) private String password;
    @NotBlank
    private String confirmPassword;
}
